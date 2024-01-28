package com.dairyProducts.details.controller;


import com.dairyProducts.details.dto.ProductStockDTO;
import com.dairyProducts.details.repository.ProductStockRepository;
import com.dairyProducts.details.service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<?> getProductStockDetails(@PathVariable String employeeId) {
        return productStockService.getProductStockDetailsService(employeeId);
    }

    @RequestMapping(value="/fetch" , method=RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> fetchResult(@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate) {
        return productStockService.getProductStockByDateService(fromDate);
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
