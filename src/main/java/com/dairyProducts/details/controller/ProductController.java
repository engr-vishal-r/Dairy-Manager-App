package com.dairyProducts.details.controller;

import com.dairyProducts.details.dto.ProductDTO;
import com.dairyProducts.details.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;


    @PostMapping(value = "/{cardNumber}")
    public ResponseEntity<String> addProduct(@PathVariable long cardNumber, @RequestBody ProductDTO productDTO, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to add product details" + " -> " + correlationId);

        return productService.addProductDetailsService(cardNumber, productDTO);
    }

    @GetMapping(value = "/{cardNumber}")
    public ResponseEntity<?> getProductDetails(@PathVariable long cardNumber, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to get product details" + " -> " + correlationId);
        return productService.getProductDetailsService(cardNumber);
    }


    @PutMapping(value = "/{cardNumber}")
    public ResponseEntity<String> updateProductDetails(@PathVariable long cardNumber, @RequestBody ProductDTO productDTO, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to update product details" + " -> " + correlationId);
        return productService.updateProductDetailsService(productDTO);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProductDetails(@PathVariable int id, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to delete product details" + " -> " + correlationId);
        return productService.deleteProductDetailsService(id);
    }
}



