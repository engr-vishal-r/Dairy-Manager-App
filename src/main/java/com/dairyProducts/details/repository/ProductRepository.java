package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCustomerCardNumber(long cardNumber);

    Optional<Product> findById(int id);

    List<Product> findByCustomerCardNumberAndPurchasedDateAfter(long cardNumber, LocalDateTime purchasedDate);
}


