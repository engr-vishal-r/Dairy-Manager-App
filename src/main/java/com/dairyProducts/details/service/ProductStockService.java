package com.dairyProducts.details.service;


import com.dairyProducts.details.dto.ProductStockDTO;
import com.dairyProducts.details.dto.ProductWithCustomerDTO;
import com.dairyProducts.details.entity.ProductStock;
import com.dairyProducts.details.handler.ProductNotFoundException;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.ProductStockRepository;
import com.dairyProducts.details.utility.CSVGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductStockService {
    private static final Logger logger = LoggerFactory.getLogger(ProductStockService.class);

    private ProductStockDTO productStockDTO;
    @Autowired
    private ProductStockRepository productStockRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public ProductStockService(ProductStockRepository productStockRepo) {
        this.productStockRepo = productStockRepo;
    }

    public ResponseEntity<?> getProductStockDetailsService(String employeeId) {
        CSVGenerator csvGenerator = new CSVGenerator();
        List<ProductStock> existingProductList = productStockRepo.findByEmployeeId(employeeId);

        if (!existingProductList.isEmpty()) {
            List<String> csvFileLinks = Collections.singletonList(csvGenerator.generateProductStockCSV(existingProductList));

            // Handle multiple CSV file links if needed
            // List<String> csvFileLinks = existingProductList.stream()
            //         .map(productStock -> csvGenerator.generateProductStockCSV(productStock))
            //         .collect(Collectors.toList());

            List<ProductStockDTO> productDetailsResponseList = existingProductList.stream()
                    .map(productStock -> new ProductStockDTO(
                            productStock.getId(),
                            productStock.getEmployeeId(),
                            productStock.getProductName().toUpperCase(),
                            productStock.getLoadedDate(),
                            productStock.getBalanceQuantity(),
                            productStock.getLoadedQuantity()))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(productDetailsResponseList);
        } else {
            throw new ProductNotFoundException("No details found for the provided id: " + employeeId);
        }
    }

    @Transactional
    public ResponseEntity<String> addProductStockDetailsService(ProductStockDTO productStockDTO) {
        try {
            ProductStock productStock = new ProductStock();
            productStock.setLoadedQuantity(productStockDTO.getLoadedQuantity());
            productStock.setEmployeeId(productStockDTO.getEmployeeId());
            productStock.setProductName(productStockDTO.getProductName().toUpperCase());
            productStock.setBalanceQuantity(productStockDTO.getLoadedQuantity());
            String quantity = Integer.toString((int) productStockDTO.getLoadedQuantity());

            if (productStockDTO.getEmployeeId().isEmpty() || !productStockDTO.getEmployeeId().matches("[0-9]{6}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee id is mandatory & please check 6 digit employee id to add details");
            } else if (productStockDTO.getLoadedQuantity() == 0 || !quantity.matches("[0-9]+([.][0-9]+)?")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is mandatory and it should be in numeric to add details");
            } else if (productStockDTO.getProductName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product Name is mandatory to add details");
            } else {
                productStockRepo.save(productStock);
                return ResponseEntity.status(HttpStatus.OK).body("Stock Details successfully added in the database " + "Please save the reference id for future for purpose -> " + productStockDTO.getId());
            }

        } catch (Exception e) {
            logger.error("Error Occurred while adding stock details: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred while adding stock details");
        }
    }
    public ResponseEntity<?> getProductStockByDateService(Date fromDate) {
        LocalDateTime fromDateTime = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<ProductStock> existingProductList = productStockRepo.findByLoadedDateAfter(fromDateTime);

        if (!existingProductList.isEmpty()) {
            CSVGenerator csvGenerator = new CSVGenerator();
            List<String> csvFileLinks = Collections.singletonList(csvGenerator.generateProductStockCSV(existingProductList));

            List<ProductStockDTO> productDetailsResponseList = existingProductList.stream()
                    .map(productStock -> new ProductStockDTO(
                            productStock.getId(),
                            productStock.getEmployeeId(),
                            productStock.getProductName().toUpperCase(),
                            productStock.getLoadedDate(),
                            productStock.getBalanceQuantity(),
                            productStock.getLoadedQuantity()))
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(productDetailsResponseList);
        } else {
            throw new ProductNotFoundException("No details found for the provided fromDate: " + fromDate);
        }
    }
}
