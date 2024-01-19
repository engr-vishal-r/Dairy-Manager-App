package com.dairyProducts.details.service;

import com.dairyProducts.details.dto.ProductDTO;
import com.dairyProducts.details.dto.ProductWithCustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.dairyProducts.details.entity.ProductStock;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.ProductRepository;
import com.dairyProducts.details.repository.ProductStockRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductDTO productDTO;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductStockRepository productStockRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public ResponseEntity<?> getProductDetailsService(long cardNumber) {
        List<Product> productList = productRepo.findByCustomerCardNumber(cardNumber);

        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No details found for cardNumber: " + cardNumber);
        } else {
            // Calculate and set the total price for each Product entity
            double totalPendingAmount = productList.stream()
                    .filter(product -> "N".equalsIgnoreCase(product.getPaid()))
                    .mapToDouble(product -> product.getQuantity() * product.getUnitPrice())
                    .sum();

            Customer customer = customerRepo.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new RuntimeException("Customer not found for cardNumber: " + cardNumber));

            // Update the total pending amount in Customer entity
            customer.setPendingAmount(totalPendingAmount);
            // Check if total pending amount exceeds 10000 and update defaulter column
            customer.setDefaulter(customer.getPendingAmount() >= 10000 ? "Y" : "N");
            customerRepo.save(customer);
            System.out.println("Customer details retrieved successfully.");

            ProductWithCustomerDTO productWithCustomerDTO = new ProductWithCustomerDTO();
            productWithCustomerDTO.setProductList(productList);
            productWithCustomerDTO.setCardNumber(customer.getCardNumber());
            productWithCustomerDTO.setCustomerName(customer.getCustomerName());
            productWithCustomerDTO.setPendingAmount(customer.getPendingAmount());

            return ResponseEntity.status(HttpStatus.OK).body(productWithCustomerDTO);
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
        if (existingCustomer.getDefaulter().equals("Y") &&
                (existingCustomer.getStatus().equals("CANCELLED") || existingCustomer.getStatus().equals("INACTIVE"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Customer has pending dues: " + existingCustomer.getPendingAmount() +
                            " (OR) Customer account is in CANCELLED or INACTIVE state");
        } else if (productDTO.getQuantity() == 0 || productDTO.getProductName().isBlank()) {
            // Check if quantity and productName are provided
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity and Product Name are mandatory to add details");
        } else {
            // Create new Product instance
            Product product = new Product();
            product.setQuantity(productDTO.getQuantity());
            product.setCustomer(existingCustomer);
            product.setProductName(productDTO.getProductName());

            // Calculate total price
            product.setUnitPrice(getDefaultUnitPrice(productDTO.getProductName()));
            double totalPrice = product.getUnitPrice() * product.getQuantity();
            product.setTotalPrice(totalPrice);

            // Check if there is sufficient balance quantity in the product stock
            double purchasedQuantity = productDTO.getQuantity();
            double remainingBalanceQuantity = updateProductStockBalanceQuantity(purchasedQuantity);

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
            existingCustomer.setDefaulter(existingCustomer.getPendingAmount() >= 10000 ? "Y" : "N");
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
            if (productDTO.getQuantity() != 0) {
                existingProduct.setQuantity(productDTO.getQuantity());
                existingProduct.setUnitPrice(getDefaultUnitPrice(productDTO.getProductName()));
                double totalPrice = existingProduct.getUnitPrice() * existingProduct.getQuantity();
                existingProduct.setTotalPrice(totalPrice);
                // Update pending amount in customer table
                Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(existingProduct.getCustomer().getCardNumber());
                if (existingCustomerOptional.isPresent()) {
                    Customer existingCustomer = existingCustomerOptional.get();
                    // Check if payment is marked as "Y"
                    if (productDTO.getPaid() != null && productDTO.getPaid().equals("Y")) {
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
            } else if (productDTO.getPaid() != null && (productDTO.getPaid().equals("Y") || productDTO.getPaid().equals("N"))) {
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

    private double updateProductStockBalanceQuantity(double purchasedQuantity) {
        ProductStock productStock = productStockRepo.findTopByOrderByLoadedDateDesc();

        if (productStock != null) {
            double remainingBalanceQuantity = productStock.getBalanceQuantity() - purchasedQuantity;

            // Check if there is sufficient balance quantity
            if (remainingBalanceQuantity >= 0) {
                productStock.setBalanceQuantity(remainingBalanceQuantity);
                productStockRepo.save(productStock);
                return remainingBalanceQuantity;
            } else {
                return -0.1; // Indicates insufficient balance quantity
            }
        } else {
            return -0.1; // Handle the case where there is no ProductStock record
        }
    }

    private double getDefaultUnitPrice(String productName) {
        switch (productName.toLowerCase()) {
            case "milk":
                return 50.0;
            case "curd":
                return 60.0;
            case "ghee":
                return 500.0;
            case "sugar":
                return 80.0;
            default:
                return 0.0; // Handle default case
        }
    }
}



