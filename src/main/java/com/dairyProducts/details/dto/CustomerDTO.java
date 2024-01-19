package com.dairyProducts.details.dto;




public class CustomerDTO {


    private long cardNumber;

    private String customerName;


    private String addressLine1;

    private String addressLine2;

    private int area;

    private long mobileNo;

    private String defaulter ;

    private String status ;


    private double pendingAmount;

    public CustomerDTO(long cardNumber, String customerName, String addressLine1, String addressLine2, int area, long mobileNo, String defaulter, String status, double pendingAmount) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.area = area;
        this.mobileNo = mobileNo;
        this.defaulter = defaulter;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
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
                ", status='" + status + '\'' +
                ", pendingAmount=" + pendingAmount +
                '}';
    }
}
