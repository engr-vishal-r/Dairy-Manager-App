package com.dairyProducts.details.service;

import com.dairyProducts.details.dao.CustomerDAO;
import com.dairyProducts.details.dto.CustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    CustomerDAO customerDao;


    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public ResponseEntity<String> addCustomerDetailsService(CustomerDTO customerDTO) {
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
    }

    public Optional<Customer> getCustomerDetailsService(long cardNumber) {

        return customerDao.getCustomerDetailsDao(cardNumber);

    }

    public ResponseEntity<String> updateCustomerDetailsService(CustomerDTO customerDTO) {

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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Defaulter can be 'N' or 'Y'");
            }
            // Save the updated entity back to the database
            existingCustomer.setAddressLine1(customerDTO.getAddressLine1());
            existingCustomer.setAddressLine2(customerDTO.getAddressLine2());
            existingCustomer.setArea(customerDTO.getArea());

            customerRepo.save(existingCustomer);
            return ResponseEntity.status(HttpStatus.OK).body("Details successfully updated in the database");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
        }
    }

    public ResponseEntity<String> deleteCustomerDetailsService(@PathVariable(required = false) long cardNumber) {

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

    }
}
