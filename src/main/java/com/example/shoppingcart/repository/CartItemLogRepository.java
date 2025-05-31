package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.CartItemLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemLogRepository extends JpaRepository<CartItemLog, Long> {
}