package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCustomerCardNumber(long cardNumber);

    Optional<Product> findById(int id);
}


