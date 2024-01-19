package com.dairyProducts.details.utility;

import com.dairyProducts.details.dto.ProductDTO;
import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.dairyProducts.details.repository.CustomerRepository;
import com.dairyProducts.details.repository.ProductRepository;
import com.dairyProducts.details.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomerStatusUpdater {

    private static final Logger logger = LoggerFactory.getLogger(CustomerStatusUpdater.class);
    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    ProductRepository productRepo;


    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st day of every month
    public void updateCustomerStatus() {

        logger.info("Job Started to update customer status    "  + "Correlation-id:  " +generateCorrelationId());
        // Fetch customers, check their purchase history, and update status accordingly
        try {
            List<Customer> activeCustomers = customerRepo.findByStatus("ACTIVE");

            List<Customer> customersToUpdate = activeCustomers.stream()
                    .filter(customer -> !hasPurchaseInLast30Days(customer))
                    .collect(Collectors.toList());

            // Print the list of customers to be updated
            for (Customer customer : customersToUpdate) {
                logger.info("Customers to be updated with 'INACTIVE' status:");
                logger.info("Customer Name: " + customer.getCustomerName() + ", Card Number: " + customer.getCardNumber());
            }

            // Perform the actual updates
            customersToUpdate.forEach(customer -> {
                customer.setStatus("INACTIVE");
                customerRepo.save(customer);
            });
        } catch (Exception e) {
            logger.error("Error Occurred while adding customer details: " + e.getMessage(), e);

        }
    }

    private boolean hasPurchaseInLast30Days(Customer customer) {
        // Logic to check if the customer made a purchase in the last 30 days
        // You can use the productRepo to fetch the purchase history and compare dates
        LocalDate currentDate = LocalDate.now().minusDays(30);
        List<Product> purchases = productRepo.findByCustomerCardNumberAndPurchasedDateAfter(customer.getCardNumber(), currentDate.atStartOfDay());
        return !purchases.isEmpty();
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}

