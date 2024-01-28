package com.dairyProducts.details.dto;



import java.time.LocalDateTime;


public class ProductStockDTO {

    private int id;

    private String employeeId;

    private String productName;

    private LocalDateTime loadedDate;

    private double loadedQuantity;

    private double balanceQuantity;

    public ProductStockDTO(int id, String employeeId, String productName, LocalDateTime loadedDate, double loadedQuantity, double balanceQuantity) {
        this.id = id;
        this.employeeId = employeeId;
        this.productName = productName;
        this.loadedDate = loadedDate;
        this.loadedQuantity = loadedQuantity;
        this.balanceQuantity = balanceQuantity;
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
