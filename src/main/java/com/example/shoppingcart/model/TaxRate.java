package com.example.shoppingcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax_rate")
public class TaxRate {
    @Id
    private Long id = 1L;

    private double rate;

    public TaxRate() {
    }

    public TaxRate(double rate) {
        this.rate = rate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
}