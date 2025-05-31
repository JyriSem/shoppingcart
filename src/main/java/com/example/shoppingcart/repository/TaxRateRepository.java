package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
}