package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserOrderByIdAsc(User user);
    List<CartItem> findByUserOrderByNameAsc(User user);
    List<CartItem> findByUserOrderByNameDesc(User user);
    List<CartItem> findByUserOrderByQuantityAsc(User user);
    List<CartItem> findByUserOrderByQuantityDesc(User user);
    List<CartItem> findByUserOrderByPriceAsc(User user);
    List<CartItem> findByUserOrderByPriceDesc(User user);
    CartItem findByUserAndName(User user, String name);
    void deleteByUserAndName(User user, String name);
    long countByUserAndDiscountIdIn(User user, List<Long> discountIds);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.user = :user AND c.name = :name")
    void updateQuantityByUserAndName(User user, String name, int quantity);
}