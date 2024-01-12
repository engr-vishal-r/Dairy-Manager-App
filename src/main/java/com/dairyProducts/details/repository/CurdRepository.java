package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.Curd;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CurdRepository extends JpaRepository<Curd, Long> {
    // Custom query methods if needed
}

