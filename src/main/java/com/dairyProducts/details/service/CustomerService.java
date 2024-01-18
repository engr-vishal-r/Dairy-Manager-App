package com.dairyProducts.details.service;


import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    CustomerRepository customerRepo;


    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public ResponseEntity<String> addCustomerDetailsService(CustomerDTO customerDTO) {
        try {
            Customer customer = new Customer();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setAddressLine1(customerDTO.getAddressLine1());
            customer.setAddressLine2(customerDTO.getAddressLine2());
            customer.setMobileNo(customerDTO.getMobileNo());
            customer.setArea(customerDTO.getArea());

            String area = Integer.toString(customerDTO.getArea());
            String mobileNo = Long.toString(customerDTO.getMobileNo());

            Optional<Customer> customerNumber = customerRepo.findByMobileNo(customerDTO.getMobileNo());
            Optional<Customer> customerName = customerRepo.findByCustomerName(customerDTO.getCustomerName());

            if (customerNumber.isPresent() || customerName.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer is already exist");
            } else if (customerDTO.getCustomerName().isEmpty() || mobileNo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Name/ mobile no. is mandatory to add details");
            } else if (!mobileNo.matches("[0-9]{10}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile no. should be 10 digits");
            } else if (!area.matches("[0-9]{6}")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Area code should be 6 digits");
            } else {
                customerRepo.save(customer);
                return ResponseEntity.status(HttpStatus.OK).body("Details successfully added in the database");
            }
        } catch (Exception e) {
            logger.error("Error Occurred while adding customer details: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred while adding customer details");
        }
    }


    public ResponseEntity<?> getCustomerDetailsService(long cardNumber) {
        try {
            Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(cardNumber);
            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();
                customerRepo.findByCardNumber(cardNumber);
                logger.info("Fetched customer details from db : " + existingCustomer);
                return ResponseEntity.status(HttpStatus.OK).body(existingCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database for provided" + cardNumber);
            }
        } catch (Exception e) {
            logger.error("Error Occurred while getting customer details: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred while getting customer details");
        }

    }

    public ResponseEntity<String> updateCustomerDetailsService(CustomerDTO customerDTO) {

        logger.info("Received customer update request : " + customerDTO);
        try {
            Customer customer = new Customer();
            String mobileNo = Long.toString(customerDTO.getMobileNo());

            if (customerDTO.getCardNumber() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Number is mandatory for updating details.");
            }

            Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(customerDTO.getCardNumber());

            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();

                // Update fields only if they are present in the DTO
                if (customerDTO.getCustomerName().isEmpty() || mobileNo.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer name/mobile no. is mandatory for updating details.");

                } else if (customerDTO.getDefaulter().equals("Y") || customerDTO.getDefaulter().equals("N")) {
                    existingCustomer.setCustomerName(customerDTO.getCustomerName());
                    existingCustomer.setMobileNo(customerDTO.getMobileNo());
                    existingCustomer.setDefaulter(customerDTO.getDefaulter());

                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Defaulter should be 'N' or 'Y'");
                }
                if (customerDTO.getStatus().isEmpty() || (!customerDTO.getStatus().equals("ACTIVE") && !customerDTO.getStatus().equals("CANCELLED"))) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("status field can't be empty, should be either 'ACTIVE' or 'CANCELLED'");
                } else {
                    existingCustomer.setStatus(customerDTO.getStatus());
                }
                // Save the updated entity back to the database
                existingCustomer.setAddressLine1(customerDTO.getAddressLine1());
                existingCustomer.setAddressLine2(customerDTO.getAddressLine2());
                existingCustomer.setArea(customerDTO.getArea());

                logger.info("customer update request was processed : " + customerDTO);
                customerRepo.save(existingCustomer);
                return ResponseEntity.status(HttpStatus.OK).body("Details successfully updated in the database");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
            }
        } catch (Exception e) {
            logger.error("Error Occurred while updating customer details : " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred while updating customer details");
        }
    }

    public ResponseEntity<String> deleteCustomerDetailsService(@PathVariable(required = false) long cardNumber) {

        logger.info("Received cardNumber to delete customer details ->" + cardNumber);
        try {
            if (cardNumber == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card number is required to delete details.");
            }

            Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(cardNumber);

            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();
                customerRepo.delete(existingCustomer);
                return ResponseEntity.status(HttpStatus.OK).body("Details deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
            }
        } catch (Exception e) {
            logger.error("Error Occurred while deleting customer details: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Occurred while deleting customer details");
        }
    }
}

