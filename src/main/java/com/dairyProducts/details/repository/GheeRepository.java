package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.Ghee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GheeRepository extends JpaRepository<Ghee, Long> {
    // Custom query methods if needed
}

