package com.dairyProducts.details.controller;

import java.util.Optional;

import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dairyProducts.details.entity.Customer;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDTO customerDTO;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws Exception {
        return customerService.addCustomerDetailsService(customerDTO);


    }

    @GetMapping(value = "/{cardNumber}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable long cardNumber) {
        Optional<Customer> customer = customerService.getCustomerDetailsService(cardNumber);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found for cardNumber: " + cardNumber);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(customer);
        }
    }
    @PutMapping(value = "/update")
    public ResponseEntity<String> updateCustomerDetails(@Valid @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomerDetailsService(customerDTO);

    }

    @DeleteMapping(value = "/delete/{cardNumber}")
    public ResponseEntity<String> deleteCustomerDetails(@PathVariable long cardNumber) {

        return customerService.deleteCustomerDetailsService(cardNumber);
    }
}


