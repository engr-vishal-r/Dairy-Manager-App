package com.dairyProducts.details.dto;


import com.dairyProducts.details.entity.Product;

import java.util.List;

public class ProductWithCustomerDTO {
    private List<Product> productList;
    private double pendingAmount;
    private Long cardNumber;
    private String customerName;
    private List<String> csvFileLink;

    public ProductWithCustomerDTO(List<Product> productList, double pendingAmount, Long cardNumber, String customerName, List<String> csvFileLink) {
        this.productList = productList;
        this.pendingAmount = pendingAmount;
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.csvFileLink = csvFileLink;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getCsvFileLink() {
        return csvFileLink;
    }

    public void setCsvFileLink(List<String> csvFileLink) {
        this.csvFileLink = csvFileLink;
    }

    @Override
    public String toString() {
        return "ProductWithCustomerDTO{" +
                "productList=" + productList +
                ", pendingAmount=" + pendingAmount +
                ", cardNumber=" + cardNumber +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
