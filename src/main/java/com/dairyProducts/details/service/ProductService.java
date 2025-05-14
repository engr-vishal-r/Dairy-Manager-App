package com.dairyProducts.details.service;

import com.dairyProducts.details.constants.Constants;
import com.dairyProducts.details.dto.ProductDTO;
import com.dairyProducts.details.dto.ProductWithCustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.dairyProducts.details.entity.ProductStock;
import com.dairyProducts.details.enums.ProductType;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.ProductRepository;
import com.dairyProducts.details.repository.ProductStockRepository;
import com.dairyProducts.details.utility.CSVGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final ProductStockRepository productStockRepo;
    private final CSVGenerator csvGenerator;

    public ProductService(ProductRepository productRepo,
                          ProductStockRepository productStockRepo,
                          CustomerRepository customerRepo,
                          CSVGenerator csvGenerator) {
        this.productRepo = productRepo;
        this.productStockRepo = productStockRepo;
        this.customerRepo = customerRepo;
        this.csvGenerator = csvGenerator;
    }
    public ResponseEntity<?> getProductDetailsService(long cardNumber) {
        List<Product> productList = productRepo.findByCustomerCardNumber(cardNumber);

        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No details found for cardNumber: " + cardNumber);
        } else {
            double totalPendingAmount = productList.stream()
                    .filter(product -> Constants.PAID_NO.equalsIgnoreCase(product.getPaid()))
                    .mapToDouble(product -> product.getQuantity() * product.getUnitPrice())
                    .sum();

            Customer customer = customerRepo.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new RuntimeException("Customer not found for cardNumber: " + cardNumber));

            customer.setPendingAmount(totalPendingAmount);
            customer.setDefaulter(customer.getPendingAmount() >= Constants.DEFAULT_PENDING_THRESHOLD
                    ? Constants.PAID_YES
                    : Constants.PAID_NO);
            customerRepo.save(customer);


            logger.info("About to call generateCSV for customer: " + customer.getCustomerName());
            List<String> csvFileLinks = Collections.singletonList(csvGenerator.generateCSV(customer, productList));

            ProductWithCustomerDTO productDetailsResponse = new ProductWithCustomerDTO(
                    productList,
                    customer.getPendingAmount(),
                    customer.getCardNumber(),
                    customer.getCustomerName(),
                    csvFileLinks
            );

            return ResponseEntity.status(HttpStatus.OK)
                    .body(productDetailsResponse);
        }
    }

    @Transactional
    public ResponseEntity<String> addProductDetailsService(long cardNumber, ProductDTO productDTO) {
        logger.info("Request received to add product details " + productDTO);

        Customer customer = new Customer();

        // Use cardNumber from the URL path
        customer.setCardNumber(cardNumber);

        // Check if card number exists in the database
        Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(cardNumber);
        if (existingCustomerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No customer details found for the given card number  :" + cardNumber);
        }

        Customer existingCustomer = existingCustomerOptional.get();

        // Check the eligibility to purchase product
        if (Constants.PAID_YES.equals(customer.getDefaulter()) &&
                (Constants.STATUS_CANCELLED.equals(customer.getStatus()) ||
                        Constants.STATUS_INACTIVE.equals(customer.getStatus()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Customer has pending dues: " + customer.getPendingAmount() +
                            " (OR) Customer account is in CANCELLED or INACTIVE state");
        } else if (productDTO.getQuantity() == 0 || productDTO.getProductName().isBlank()) {
            // Check if quantity and productName are provided
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity and Product Name are mandatory to add details");
        } else {
            // Create new Product instance
            Product product = new Product();
            product.setQuantity(productDTO.getQuantity());
            product.setCustomer(existingCustomer);
            try {
                product.setProductName(ProductType.fromName(productDTO.getProductName()).name());
                System.out.println("Product name  -" + ProductType.fromName(productDTO.getProductName()).name());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid product name: " + productDTO.getProductName());
            }

            // Calculate total price
            product.setUnitPrice(getDefaultUnitPrice(productDTO.getProductName()));
            double totalPrice = product.getUnitPrice() * product.getQuantity();
            product.setTotalPrice(totalPrice);

            // Check if there is sufficient balance quantity in the product stock
            double purchasedQuantity = productDTO.getQuantity();
            double remainingBalanceQuantity = updateProductStockBalanceQuantity(productDTO.getProductName(), purchasedQuantity);

            if (remainingBalanceQuantity < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product stock is not available");
            }

            // Update customer table with pending amount details
            if (existingCustomer.getPendingAmount() == 0) {
                existingCustomer.setPendingAmount(0.0); // Set a default value if it's null
            }

            // Save the product details
            Product savedProduct = productRepo.save(product);

            // Update customer table with pending amount details
            existingCustomer.setPendingAmount(existingCustomer.getPendingAmount() + totalPrice);
            existingCustomer.setDefaulter(existingCustomer.getPendingAmount() >= Constants.DEFAULT_PENDING_THRESHOLD ? Constants.PAID_YES : Constants.PAID_NO);
            customerRepo.save(existingCustomer);

            return ResponseEntity.status(HttpStatus.OK).body("Details successfully added in the database. Remaining Balance Quantity ->  " +
                    remainingBalanceQuantity + " Please save the reference id for future reference  -> " + savedProduct.getId());
        }
    }

    @Transactional
    public ResponseEntity<String> updateProductDetailsService(ProductDTO productDTO) {
        if (productDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is mandatory for updating details.");
        }

        Optional<Product> existingProductOptional = productRepo.findById(productDTO.getId());

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            double existingTotalAmount = existingProduct.getTotalPrice();
            // Update fields only if they are present in the DTO
            if (productDTO.getQuantity() > 0) {
                existingProduct.setQuantity(productDTO.getQuantity());
                existingProduct.setUnitPrice(getDefaultUnitPrice(productDTO.getProductName()));
                double totalPrice = existingProduct.getUnitPrice() * existingProduct.getQuantity();
                existingProduct.setTotalPrice(totalPrice);
                // Update pending amount in customer table
                Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(existingProduct.getCustomer().getCardNumber());
                if (existingCustomerOptional.isPresent()) {
                    Customer existingCustomer = existingCustomerOptional.get();
                    // Check if payment is made already
                    if (productDTO.getPaid() != null && Constants.PAID_YES.equalsIgnoreCase(productDTO.getPaid())) {
                        double newPendingAmountAfterPayment = existingCustomer.getPendingAmount() - existingTotalAmount;
                        existingCustomer.setPendingAmount(newPendingAmountAfterPayment);
                    } else {
                        double newPendingAmount = existingCustomer.getPendingAmount() - existingTotalAmount + totalPrice;
                        existingCustomer.setPendingAmount(newPendingAmount);
                    }
                    customerRepo.save(existingCustomer);
                    System.out.println("Customer details updated successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found for cardNumber: " + existingProduct.getCustomer().getCardNumber());
                }
            }

            if (productDTO.getCustomer() != null) {
                existingProduct.setCustomer(productDTO.getCustomer());
            } else if (productDTO.getPaid() != null &&
                    (Constants.PAID_YES.equalsIgnoreCase(productDTO.getPaid()) ||
                            Constants.PAID_NO.equalsIgnoreCase(productDTO.getPaid()))) {
                existingProduct.setPaid(productDTO.getPaid());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer details or paid status is mandatory for updating details.");
            }

            // Save the updated entity back to the database
            productRepo.save(existingProduct);

            logger.info("Received customer update request : " + productDTO);
            System.out.println("Product details updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body("Details successfully updated in the database " + productDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database try with other id");
        }
    }

    public ResponseEntity<String> deleteProductDetailsService(@PathVariable(required = false) String id) {

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required to delete details.");
        }

        Optional<Product> existingProductOptional = productRepo.findById(id);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            productRepo.delete(existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body("Details deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
        }
    }

    private double updateProductStockBalanceQuantity(String productName, double purchasedQuantity) {

        Optional<ProductStock> stockOptional = productStockRepo.findTopByProductNameIgnoreCaseOrderByLoadedDateDesc(productName);
        System.out.println(">> Searching for product: " + productName);
        productStockRepo.findAll().forEach(stock ->
                System.out.println(">> DB has: " + stock.getProductName() + " | Qty: " + stock.getBalanceQuantity())
        );
        if (stockOptional.isPresent()) {
            ProductStock productStock = stockOptional.get();
            System.out.println("Found stock: " + productStock.getProductName() + " | Available: " + productStock.getBalanceQuantity() + " | Requested: " + purchasedQuantity);
            double remainingBalanceQuantity = productStock.getBalanceQuantity() - purchasedQuantity;

            if (remainingBalanceQuantity >= 0) {
                productStock.setBalanceQuantity(remainingBalanceQuantity);
                productStockRepo.save(productStock);
                return remainingBalanceQuantity;
            } else {
                return -0.1; // Insufficient stock
            }
        } else {
            return -0.1; // No stock found for product
        }
    }


    private double getDefaultUnitPrice(String productName) {
        try {
            return ProductType.fromName(productName).getPrice();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid product name provided: {}", productName);
            return 0.0;
        }
    }
}



