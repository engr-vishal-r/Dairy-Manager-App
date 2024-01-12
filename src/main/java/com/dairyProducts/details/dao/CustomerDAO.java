package com.dairyProducts.details.dao;

import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerDAO {

    @Autowired
    CustomerRepository customerRepo;

    public Optional<Customer> getCustomerDetailsDao(long cardNumber) {
        return customerRepo.findByCardNumber(cardNumber);
    }
}
