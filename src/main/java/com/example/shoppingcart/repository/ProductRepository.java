package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteByName(String name);
}