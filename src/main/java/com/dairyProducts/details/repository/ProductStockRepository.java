package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Integer> {

    Optional<ProductStock> findById(int id);

    ProductStock findTopByOrderByLoadedDateDesc();

}
