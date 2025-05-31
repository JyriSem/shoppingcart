package com.example.shoppingcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double percentage;

    public Discount() {
    }

    public Discount(double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("Discount percentage cannot be negative");
        }
        this.percentage = percentage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("Discount percentage cannot be negative");
        }
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return percentage + "%";
    }
}