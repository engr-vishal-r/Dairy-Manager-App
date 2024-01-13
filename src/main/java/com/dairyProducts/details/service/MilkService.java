package com.dairyProducts.details.service;

import com.dairyProducts.details.dao.MilkDAO;
import com.dairyProducts.details.dto.MilkDTO;
import com.dairyProducts.details.dto.MilkWithCustomerDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Milk;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.MilkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MilkService {

    @Autowired
    MilkDAO milkDao;
    @Autowired
    MilkDTO milkDTO;
    @Autowired
    private MilkRepository milkRepository;

    @Autowired
    private CustomerRepository customerRepo;

    public MilkService(MilkRepository milkRepository) {
        this.milkRepository = milkRepository;
    }

    public ResponseEntity<?> getMilkDetailsService(long cardNumber) {
        List<Milk> milkList = milkRepository.findByCustomerCardNumber(cardNumber);

        if (milkList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No details found for cardNumber: " + cardNumber);
        } else {
            // Calculate and set the total price for each Milk entity
            double totalPendingAmount = milkList.stream()
                    .filter(milk -> "N".equalsIgnoreCase(milk.getPaid()))
                    .mapToDouble(milk -> milk.getQuantity() * milk.getUnitPrice())
                    .sum();

            Customer customer = customerRepo.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new RuntimeException("Customer not found for cardNumber: " + cardNumber));

            // Update the total pending amount in Customer entity
            customer.setPendingAmount(totalPendingAmount);
            // Check if total pending amount exceeds 10000 and update defaulter column
            if (totalPendingAmount >= 10000) {
                customer.setDefaulter("Y");
            } else {
                customer.setDefaulter("N");
            }
            customerRepo.save(customer);

            MilkWithCustomerDTO milkWithCustomerDTO = new MilkWithCustomerDTO();
            milkWithCustomerDTO.setMilkList(milkList);
            milkWithCustomerDTO.setCardNumber(customer.getCardNumber());
            milkWithCustomerDTO.setCustomerName(customer.getCustomerName());
            milkWithCustomerDTO.setPendingAmount(customer.getPendingAmount());

            return ResponseEntity.status(HttpStatus.OK).body(milkWithCustomerDTO);
        }
    }

    public ResponseEntity<String> addMilkDetailsService(MilkDTO milkDTO) {
        Customer customer = new Customer();
        // Check if customer is provided
        if (milkDTO.getCustomer() == null || milkDTO.getCustomer().getCardNumber() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card Number is mandatory to add details");
        }

        // Check if card number exists in the database
        Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(milkDTO.getCustomer().getCardNumber());
        if (existingCustomerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No details found in the database for the given card number");
        }

        Customer existingCustomer = existingCustomerOptional.get();

        // Check the eligibility to purchase milk
        if (existingCustomer.getDefaulter().equals("Y")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Customer has pending dues :" + existingCustomer.getPendingAmount());
        } else if (milkDTO.getQuantity() == 0) { // Check if quantity is provided
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is mandatory to add details");
        } else {
            // Create new Milk instance
            Milk milk = new Milk();
            milk.setQuantity(milkDTO.getQuantity());
            milk.setCustomer(existingCustomer);

            // Calculate total price
            double totalPrice = milk.getUnitPrice() * milk.getQuantity();
            milk.setTotalPrice(totalPrice);

            // Save the milk details
            milkRepository.save(milk);
            //update customer table with pending amount details
            existingCustomer.setPendingAmount(existingCustomer.getPendingAmount() + totalPrice);
            customerRepo.save(existingCustomer);

            // Check if the pending amount exceeds 10000 and update isDefaulter field
            existingCustomer.setDefaulter(existingCustomer.getPendingAmount() >= 10000 ? "Y" : "N");
            customerRepo.save(existingCustomer);

            return ResponseEntity.status(HttpStatus.OK).body("Details successfully added in the database");
        }
    }


    public ResponseEntity<String> updateMilkDetailsService(MilkDTO milkDTO) {

        Milk milk = new Milk();
        if (milkDTO.getId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is mandatory for updating details.");
        }

        Optional<Milk> existingMilkOptional = milkRepository.findById(milkDTO.getId());

        if (existingMilkOptional.isPresent()) {
            Milk existingMilk = existingMilkOptional.get();

            // Update fields only if they are present in the DTO
            if (milkDTO.getQuantity() != 0) {
                existingMilk.setQuantity(milkDTO.getQuantity());
                double totalPrice = milk.getUnitPrice() * existingMilk.getQuantity();
                existingMilk.setTotalPrice(totalPrice);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is mandatory for updating details.");
            }
            if (milkDTO.getCustomer() != null) {
                existingMilk.setCustomer(milkDTO.getCustomer());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer number is mandatory for updating details.");
            }

            // Save the updated entity back to the database
            milkRepository.save(existingMilk);

            return ResponseEntity.status(HttpStatus.OK).body("Details successfully updated in the database");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
        }
    }

    public ResponseEntity<String> deleteMilkDetailsService(@PathVariable(required = false) Integer id) {

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is required to delete details.");
        }

        Optional<Milk> existingMilkOptional = milkRepository.findById(id);

        if (existingMilkOptional.isPresent()) {
            Milk existingMilk = existingMilkOptional.get();
            milkRepository.delete(existingMilk);
            return ResponseEntity.status(HttpStatus.OK).body("Details deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
        }
    }
}



