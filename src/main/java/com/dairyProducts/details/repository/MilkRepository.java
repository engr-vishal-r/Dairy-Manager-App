package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.Milk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MilkRepository extends JpaRepository<Milk, Integer> {
    List<Milk> findByCustomerCardNumber(long cardNumber);
}


