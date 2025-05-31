package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}