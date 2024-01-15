package com.dairyProducts.details.controller;

import com.dairyProducts.details.dto.MilkDTO;
import com.dairyProducts.details.dto.MilkStockDTO;
import com.dairyProducts.details.repository.MilkRepository;
import com.dairyProducts.details.repository.MilkStockRepository;
import com.dairyProducts.details.service.MilkService;
import com.dairyProducts.details.service.MilkStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/milkStock")
public class MilkStockController {
    @Autowired
    private MilkStockRepository milkStockRepo;
    @Autowired
    private MilkStockService milkStockService;
    @Autowired
    private MilkStockDTO milkStockDTO;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addMilkStock(@RequestBody MilkStockDTO milkStockDTO) {
        return milkStockService.addMilkStockDetailsService(milkStockDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getMilkStockDetails(@PathVariable int id) {
        return milkStockService.getMilkStockDetailsService(id);
    }


//    @PutMapping(value = "/{id}")
//    public ResponseEntity<String> updateMilkDetails(@PathVariable int id, @RequestBody MilkStockDTO milkStockDTO) {
//        return milkStockService.updateMilkStockDetailsService(milkStockDTO);
//
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<String> deleteMilkDetails(@PathVariable int id) {
//
//        return milkStockService.deleteMilkStockDetailsService(id);
//    }
}
