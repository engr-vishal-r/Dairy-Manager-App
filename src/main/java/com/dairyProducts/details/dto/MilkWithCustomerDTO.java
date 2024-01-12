package com.dairyProducts.details.dto;

import com.dairyProducts.details.entity.Milk;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.stereotype.Component;

import java.util.List;

public class MilkWithCustomerDTO {

        private List<Milk> milkList;
        private double pendingAmount;
        private long cardNumber;
        private String customerName;

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
}
