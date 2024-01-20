package com.dairyProducts.details.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="product_stock")
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private int id;
    @Column(name = "emp_id")
    private String employeeId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "loaded_date")
    @CreationTimestamp
    private LocalDateTime loadedDate;
    @Column(name = "loaded_quantity")
    private double loadedQuantity;

    @Column(name = "balance_quantity")
    private double balanceQuantity;

    public ProductStock() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getLoadedDate() {
        return loadedDate;
    }

    public void setLoadedDate(LocalDateTime loadedDate) {
        this.loadedDate = loadedDate;
    }

    public double getLoadedQuantity() {
        return loadedQuantity;
    }

    public void setLoadedQuantity(double loadedQuantity) {
        this.loadedQuantity = loadedQuantity;
    }

    public double getBalanceQuantity() {
        return balanceQuantity;
    }

    public void setBalanceQuantity(double balanceQuantity) {
        this.balanceQuantity = balanceQuantity;
    }

    @Override
    public String toString() {
        return "ProductStock{" +
                "id=" + id +
                ", employeeId='" + employeeId + '\'' +
                ", productName='" + productName + '\'' +
                ", loadedDate=" + loadedDate +
                ", loadedQuantity=" + loadedQuantity +
                ", balanceQuantity=" + balanceQuantity +
                '}';
    }
}