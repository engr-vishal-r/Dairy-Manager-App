package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.MilkStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MilkStockRepository extends JpaRepository<MilkStock, Integer> {

    Optional<MilkStock> findById(int id);

}
