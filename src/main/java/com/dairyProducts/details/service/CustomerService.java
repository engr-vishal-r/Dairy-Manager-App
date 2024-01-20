package com.dairyProducts.details.service;


import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
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
            customer.setCustomerName(customerDTO.getCustomerName().toUpperCase());
            customer.setAddressLine1(customerDTO.getAddressLine1().toUpperCase());
            customer.setAddressLine2(customerDTO.getAddressLine2().toUpperCase());
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
                Customer savedCustomer = customerRepo.save(customer);
                return ResponseEntity.status(HttpStatus.OK).body("Details successfully added in the database    " + " Save the customer card number for future reference " + savedCustomer.getCardNumber());
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
        logger.info("Received customer update request: " + customerDTO);

        try {
            if (customerDTO.getCardNumber() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Number is mandatory for updating details.");
            }

            Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(customerDTO.getCardNumber());

            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();

                // Update fields only if they are present in the DTO
                if (isEmptyOrNullOrWhitespace(customerDTO.getCustomerName()) || customerDTO.getMobileNo() ==0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer name/mobile no. is mandatory for updating details.");
                }

                if (!isValidDefaulter(customerDTO.getDefaulter())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Defaulter should be 'N' or 'Y'");
                }

                if (isEmptyOrNullOrWhitespace(customerDTO.getStatus()) || !isValidStatus(customerDTO.getStatus())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status field can't be empty, should be either 'ACTIVE' or 'CANCELLED'");
                }

                // Update customer details
                existingCustomer.setCustomerName(customerDTO.getCustomerName().toUpperCase());
                existingCustomer.setMobileNo(customerDTO.getMobileNo());
                existingCustomer.setDefaulter(customerDTO.getDefaulter());
                existingCustomer.setStatus(customerDTO.getStatus());

                // Update other fields
                existingCustomer.setAddressLine1(customerDTO.getAddressLine1().toUpperCase());
                existingCustomer.setAddressLine2(customerDTO.getAddressLine2().toUpperCase());
                existingCustomer.setArea(customerDTO.getArea());

                logger.info("Customer update request was processed: " + customerDTO);
                Customer updatedCustomer = customerRepo.save(existingCustomer);

                return ResponseEntity.status(HttpStatus.OK).body("Details successfully updated in the database" + updatedCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating customer details: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating customer details");
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

    // Helper methods for checking conditions
    private boolean isEmptyOrNullOrWhitespace(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidDefaulter(String defaulter) {
        return defaulter != null && (defaulter.equals("Y") || defaulter.equals("N"));
    }

    private boolean isValidStatus(String status) {
        return status != null && (status.equals("ACTIVE") || status.equals("CANCELLED"));
    }
}
