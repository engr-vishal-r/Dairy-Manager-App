package com.dairyProducts.details.dto;

import com.dairyProducts.details.entity.Customer;
import com.dairyProducts.details.entity.Milk;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MilkDTO {

    @Id
    @Column(name = "id")
    private int id;
    @DecimalMin(value = "0.0", message = "Quantity must be a positive number")
    @Column(name = "quantity")
    private double quantity;
    private List<Milk> milkList;
    private double pendingAmount;
    private long cardNumber;
    private String customerName;
    private String paid;
    @ManyToOne
    @JoinColumn(name = "card_number", referencedColumnName = "card_number")
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Milk> getMilkList() {
        return milkList;
    }

    public void setMilkList(List<Milk> milkList) {
        this.milkList = milkList;
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

    @Override
    public String toString() {
        return "MilkDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", milkList=" + milkList +
                ", pendingAmount=" + pendingAmount +
                ", cardNumber=" + cardNumber +
                ", customerName='" + customerName + '\'' +
                ", paid='" + paid + '\'' +
                ", customer=" + customer +
                '}';
    }
}

