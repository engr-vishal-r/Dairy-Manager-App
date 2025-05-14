package com.dairyProducts.details.service;


import com.dairyProducts.details.dto.ProductStockDTO;
import com.dairyProducts.details.entity.ProductStock;
import com.dairyProducts.details.enums.ProductType;
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
import java.util.Date;
import java.util.List;
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
            // Validate Employee ID
            if (productStockDTO.getEmployeeId() == null || !productStockDTO.getEmployeeId().matches("[0-9]{6}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee ID is mandatory & should be a 6-digit number.");
            }

            // Validate Quantity
            if (productStockDTO.getLoadedQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Quantity is mandatory and must be a positive number.");
            }

            // Validate Product Name
            ProductType productType;
            try {
                productType = ProductType.fromName(productStockDTO.getProductName());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid product name. Accepted values: MILK, CURD, GHEE, SUGAR.");
            }

            // Create and save ProductStock
            ProductStock productStock = new ProductStock();
            productStock.setEmployeeId(productStockDTO.getEmployeeId());
            productStock.setLoadedQuantity(productStockDTO.getLoadedQuantity());
            productStock.setBalanceQuantity(productStockDTO.getLoadedQuantity());
            productStock.setProductName(productType.name()); // Use normalized enum name

            productStockRepo.save(productStock);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stock details successfully added. Reference ID: " + productStock.getId());

        } catch (Exception e) {
            logger.error("Error occurred while adding stock details: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while adding stock details");
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
