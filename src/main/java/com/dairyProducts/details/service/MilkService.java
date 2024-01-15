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
import org.springframework.transaction.annotation.Transactional;
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
            customer.setDefaulter(customer.getPendingAmount() >= 10000 ? "Y" : "N");
            customerRepo.save(customer);
            System.out.println("Customer details updated successfully.");

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
            System.out.println("Milk details updated successfully.");
            //update customer table with pending amount details
            existingCustomer.setPendingAmount(existingCustomer.getPendingAmount() + totalPrice);
            existingCustomer.setDefaulter(existingCustomer.getPendingAmount() >= 10000 ? "Y" : "N");
            customerRepo.save(existingCustomer);
            System.out.println("Customer details updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body("Details successfully added in the database");
        }
    }

    @Transactional
    public ResponseEntity<String> updateMilkDetailsService(MilkDTO milkDTO) {
        if (milkDTO.getId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID is mandatory for updating details.");
        }

        Optional<Milk> existingMilkOptional = milkRepository.findById(milkDTO.getId());

        if (existingMilkOptional.isPresent()) {
            Milk existingMilk = existingMilkOptional.get();
            double existingTotalAmount = existingMilk.getTotalPrice();

            // Update fields only if they are present in the DTO
            if (milkDTO.getQuantity() != 0) {
                existingMilk.setQuantity(milkDTO.getQuantity());
                double totalPrice = existingMilk.getUnitPrice() * existingMilk.getQuantity();
                existingMilk.setTotalPrice(totalPrice);

                // Update pending amount in customer table
                Optional<Customer> existingCustomerOptional = customerRepo.findByCardNumber(existingMilk.getCustomer().getCardNumber());
                if (existingCustomerOptional.isPresent()) {
                    Customer existingCustomer = existingCustomerOptional.get();

                    // Check if payment is marked as "Y"
                    if (milkDTO.getPaid() != null && milkDTO.getPaid().equals("Y")) {
                        double newPendingAmountAfterPayment = existingCustomer.getPendingAmount() - existingTotalAmount;
                        existingCustomer.setPendingAmount(newPendingAmountAfterPayment);
                    } else {
                        double newPendingAmount = existingCustomer.getPendingAmount() - existingTotalAmount + totalPrice;
                        existingCustomer.setPendingAmount(newPendingAmount);
                    }

                    customerRepo.save(existingCustomer);
                    System.out.println("Customer details updated successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found for cardNumber: " + existingMilk.getCustomer().getCardNumber());
                }
            }

            if (milkDTO.getCustomer() != null) {
                existingMilk.setCustomer(milkDTO.getCustomer());
            } else if (milkDTO.getPaid() != null && (milkDTO.getPaid().equals("Y") || milkDTO.getPaid().equals("N"))) {
                existingMilk.setPaid(milkDTO.getPaid());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer details or paid status is mandatory for updating details.");
            }

            // Save the updated entity back to the database
            milkRepository.save(existingMilk);

            System.out.println("Milk details updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body("Details successfully updated in the database");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Details not found in the database");
        }
    }

    public ResponseEntity<String> deleteMilkDetailsService (@PathVariable(required = false) Integer id){

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



