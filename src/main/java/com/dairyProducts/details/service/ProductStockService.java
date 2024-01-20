package com.dairyProducts.details.service;


import com.dairyProducts.details.dto.ProductStockDTO;
import com.dairyProducts.details.entity.ProductStock;
import com.dairyProducts.details.handler.ProductNotFoundException;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.ProductStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

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

    public ResponseEntity<?> getProductStockDetailsService(int id) {
        Optional<ProductStock> existingProductOptional = productStockRepo.findById(id);

        if (existingProductOptional.isPresent()) {
            ProductStock productStock = existingProductOptional.get();
            productStockDTO.setId(productStock.getId());
            productStockDTO.setEmployeeId(productStock.getEmployeeId());
            productStockDTO.setProductName(productStock.getProductName());
            productStockDTO.setLoadedDate(productStock.getLoadedDate());
            productStockDTO.setBalanceQuantity(productStock.getBalanceQuantity());
            productStockDTO.setLoadedQuantity(productStock.getLoadedQuantity());
            return ResponseEntity.status(HttpStatus.OK).body(productStockDTO);
        } else {
            throw new ProductNotFoundException("No details found for the provided id: " + id);

        }
    }

    @Transactional
    public ResponseEntity<String> addProductStockDetailsService(ProductStockDTO productStockDTO) {
        try {
            ProductStock productStock = new ProductStock();
            productStock.setLoadedQuantity(productStockDTO.getLoadedQuantity());
            productStock.setEmployeeId(productStockDTO.getEmployeeId());
            productStock.setProductName(productStockDTO.getProductName());
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
}
