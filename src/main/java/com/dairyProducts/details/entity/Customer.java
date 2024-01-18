package com.dairyProducts.details.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(generator = "custom_card_number_generator")
    @GenericGenerator(name = "custom_card_number_generator", strategy = "com.dairyProducts.details.controller.CustomerCardNumberGenerator")
    @Column(name = "card_number")
    private long cardNumber;
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_line2")
    private String addressLine2;
    @Column(name = "area")
    private int area;
    @Column(name = "mobileNo")
    private long mobileNo;

    @Column(name="pending_amount")
    private Double pendingAmount=0.0;
    @Column(name = "defaulter")
    private String defaulter = "N";
    @Column(name = "status")
    private String status = "ACTIVE";

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Milk> milkList;
    public Customer() {

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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(String defaulter) {
        this.defaulter = defaulter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Milk> getMilkList() {
        return milkList;
    }

    public void setMilkList(List<Milk> milkList) {
        this.milkList = milkList;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cardNumber=" + cardNumber +
                ", customerName='" + customerName + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", area=" + area +
                ", mobileNo=" + mobileNo +
                ", pendingAmount=" + pendingAmount +
                ", defaulter='" + defaulter + '\'' +
                ", status='" + status + '\'' +
                ", milkList=" + milkList +
                '}';
    }
}




