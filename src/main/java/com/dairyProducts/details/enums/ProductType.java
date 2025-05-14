package com.dairyProducts.details.enums;

import java.util.Arrays;

public enum  ProductType {
    MILK(50.0),
    CURD(60.0),
    GHEE(500.0),
    SUGAR(80.0);

    private final double price;

    ProductType(double price) { this.price = price; }

    public double getPrice() { return price; }

    public static ProductType fromName(String name) {
        return Arrays.stream(values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid product name: " + name));
    }
}