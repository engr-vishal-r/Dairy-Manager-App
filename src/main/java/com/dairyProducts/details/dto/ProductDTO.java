package com.dairyProducts.details.dto;

import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ProductDTO {

    private int id;
    private double quantity;
    private List<Product> productList;
    private double pendingAmount;
    private long cardNumber;
    private String customerName;
    private String paid;

    private String productName;

    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductDTO(){
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", productList=" + productList +
                ", pendingAmount=" + pendingAmount +
                ", cardNumber=" + cardNumber +
                ", customerName='" + customerName + '\'' +
                ", paid='" + paid + '\'' +
                ", productName='" + productName + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }
}

