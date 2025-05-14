package com.dairyProducts.details.repository;

import com.dairyProducts.details.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Integer> {

    Optional<ProductStock> findById(int id);

    List<ProductStock> findByEmployeeId(String employeeId);

    Optional<ProductStock> findTopByProductNameIgnoreCaseOrderByLoadedDateDesc(String productName);

    @Query("SELECT p FROM ProductStock p WHERE p.loadedDate >= :fromDate")
    List<ProductStock> findByLoadedDateAfter(LocalDateTime fromDate);
}