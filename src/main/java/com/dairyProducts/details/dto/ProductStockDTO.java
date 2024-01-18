package com.dairyProducts.details.dto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductStockDTO {

    private int id;

    private String employeeId;

    private String productName;

    private LocalDateTime loadedDate;

    private double loadedQuantity;

    private double balanceQuantity;

    public ProductStockDTO() {
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
        return "ProductStockDTO{" +
                "id=" + id +
                ", employeeId='" + employeeId + '\'' +
                ", productName='" + productName + '\'' +
                ", loadedDate=" + loadedDate +
                ", loadedQuantity=" + loadedQuantity +
                ", balanceQuantity=" + balanceQuantity +
                '}';
    }
}
