package com.dairyProducts.details.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Component
public class CustomerDTO {

    @Id
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

    @Column(name = "defaulter")
    private String defaulter = "N";

    @Column(name="pending_amount")
    private Double pendingAmount;

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

    public String getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(String defaulter) {
        this.defaulter = defaulter;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "cardNumber=" + cardNumber +
                ", customerName='" + customerName + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", area=" + area +
                ", mobileNo=" + mobileNo +
                ", defaulter='" + defaulter + '\'' +
                ", pendingAmount=" + pendingAmount +
                '}';
    }
}
