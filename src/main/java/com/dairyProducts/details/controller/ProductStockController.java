package com.dairyProducts.details.controller;


import com.dairyProducts.details.dto.ProductStockDTO;
import com.dairyProducts.details.repository.ProductStockRepository;
import com.dairyProducts.details.service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productStock")
public class ProductStockController {
    @Autowired
    private ProductStockRepository productStockRepo;
    @Autowired
    private ProductStockService productStockService;

    private ProductStockDTO productStockDTO;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addProductStock(@RequestBody ProductStockDTO productStockDTO) {
        return productStockService.addProductStockDetailsService(productStockDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProductStockDetails(@PathVariable int id) {
        return productStockService.getProductStockDetailsService(id);
    }


//    @PutMapping(value = "/{id}")
//    public ResponseEntity<String> updateMilkDetails(@PathVariable int id, @RequestBody ProductStockDTO productStockDTO) {
//        return productStockService.updateProductStockDetailsService(productStockDTO);
//
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<String> deleteMilkDetails(@PathVariable int id) {
//
//        return productStockService.deleteProductStockDetailsService(id);
//    }
}
