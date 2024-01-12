package com.dairyProducts.details.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Milk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milk_id")
    private int id;
    @Column(name = "quantity")
    private double quantity;

    @Column(name = "unit_Price")
    private double unitPrice = 50.0;

    @Column(name = "purchased_date")
    @CreationTimestamp
    private LocalDateTime purchasedDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "total_Price")
    private double totalPrice;

    @Column(name = "paid")
    private String paid="N";

    @ManyToOne
    @JoinColumn(name = "card_number", referencedColumnName = "card_number")
    @JsonIgnore
    private Customer customer;

    public Milk() {

    }

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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDateTime getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDateTime purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Milk{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", purchasedDate=" + purchasedDate +
                ", updatedDate=" + updatedDate +
                ", totalPrice=" + totalPrice +
                ", customer=" + customer +
                ", paid=" + paid +
                '}';
    }
}

