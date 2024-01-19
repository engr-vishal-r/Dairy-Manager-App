package com.dairyProducts.details.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dairyProducts.details.entity.Customer;

import java.util.List;
import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMobileNo(long mobileNo);

    Optional<Customer> findByCustomerName(String customerName);
    Optional<Customer> findByCardNumber(long cardNumber);

    List<Customer> findByStatus(String status);

}
