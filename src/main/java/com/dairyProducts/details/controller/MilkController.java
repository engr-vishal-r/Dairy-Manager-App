package com.dairyProducts.details.controller;

import java.util.Optional;

import com.dairyProducts.details.dto.MilkDTO;
import com.dairyProducts.details.service.MilkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dairyProducts.details.entity.Milk;
import com.dairyProducts.details.repository.MilkRepository;

@RestController
@RequestMapping("/milk")
public class MilkController {
    @Autowired
    private MilkRepository milkRepository;
    @Autowired
    private MilkService milkService;
    @Autowired
    private MilkDTO milkDTO;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addMilk(@Valid @RequestBody MilkDTO milkDTO) {
        return milkService.addMilkDetailsService(milkDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getMilkDetails(@PathVariable long cardNumber) {
        Optional<Milk> milk = milkService.getMilkDetailsService(cardNumber);
        if (milk.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found for cardNumber: " + cardNumber);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(milk);
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<String> updateMilkDetails(@Valid @RequestBody MilkDTO milkDTO) {
        return milkService.updateMilkDetailsService(milkDTO);

    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteMilkDetails(@PathVariable int id) {

        return milkService.deleteMilkDetailsService(id);
    }
}



