package com.dairyProducts.details.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.dairyProducts.details.dto.MilkDTO;
import com.dairyProducts.details.service.MilkService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dairyProducts.details.entity.Milk;
import com.dairyProducts.details.repository.MilkRepository;

@RestController
@RequestMapping("/milk")
public class MilkController {
    private static final Logger logger = LoggerFactory.getLogger(MilkController.class);
    @Autowired
    private MilkRepository milkRepository;
    @Autowired
    private MilkService milkService;
    @Autowired
    private MilkDTO milkDTO;

    @PostMapping(value = "/{cardNumber}")
    public ResponseEntity<String> addMilk(@PathVariable long cardNumber, @RequestBody MilkDTO milkDTO, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to add milk details" +" -> "+ correlationId);
        return milkService.addMilkDetailsService(milkDTO);
    }

    @GetMapping(value = "/{cardNumber}")
    public ResponseEntity<?> getMilkDetails(@PathVariable long cardNumber, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to get milk details" +" -> "+ correlationId);
       return milkService.getMilkDetailsService(cardNumber);
        }


    @PutMapping(value = "/{cardNumber}")
    public ResponseEntity<String> updateMilkDetails(@PathVariable long cardNumber, @RequestBody MilkDTO milkDTO, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to update milk details" +" -> "+ correlationId);
        return milkService.updateMilkDetailsService(milkDTO);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteMilkDetails(@PathVariable int id, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to delete milk details" +" -> "+ correlationId);
        return milkService.deleteMilkDetailsService(id);
    }
}



