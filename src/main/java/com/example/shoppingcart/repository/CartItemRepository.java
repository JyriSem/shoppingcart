package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    CartItem findByUserAndName(User user, String name);
    void deleteByUserAndName(User user, String name);
    long countByUserAndDiscountIsNotNull(User user);
}