package com.dairyProducts.details.controller;


import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    private CustomerDTO customerDTO;


    @PostMapping(value = "/add")
    public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerDTO, @RequestHeader(name = "correlation_id", required = false) String correlationId) throws Exception  {

        logger.info("Received request to add customer" +" -> "+  correlationId + customerDTO );
        return customerService.addCustomerDetailsService(customerDTO);

    }

    @GetMapping(value = "/{cardNumber}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable long cardNumber) {

        return customerService.getCustomerDetailsService(cardNumber);
        }

    @PutMapping(value = "/{cardNumber}")
    public ResponseEntity<String> updateCustomerDetails(@PathVariable long cardNumber, @Valid @RequestBody CustomerDTO customerDTO, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to update customer details" +" -> "+ correlationId);
        return customerService.updateCustomerDetailsService(customerDTO);

    }

    @DeleteMapping(value = "/{cardNumber}")
    public ResponseEntity<String> deleteCustomerDetails(@PathVariable long cardNumber, @RequestHeader(name = "correlation_id", required = false) String correlationId) {
        logger.info("Received request to delete customer" +" -> "+ correlationId);
        return customerService.deleteCustomerDetailsService(cardNumber);
    }
//    private String generateCorrelationId() {
//        return UUID.randomUUID().toString();
//    }
}


