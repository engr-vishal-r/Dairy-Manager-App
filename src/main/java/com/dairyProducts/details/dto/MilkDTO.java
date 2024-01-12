package com.dairyProducts.details.dto;

import com.dairyProducts.details.entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.stereotype.Component;

@Component
public class MilkDTO {

    @Id
    @Column(name = "id")
    private int id;
    @DecimalMin(value = "0.0", message = "Quantity must be a positive number")
    @Column(name = "quantity")
    private double quantity;

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

    @Override
    public String toString() {
        return "MilkDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", customer=" + customer +
                '}';
    }
}

