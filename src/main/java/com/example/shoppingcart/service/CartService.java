package com.example.shoppingcart.service;

import com.example.shoppingcart.model.*;
import com.example.shoppingcart.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;
    private final CartItemLogRepository cartItemLogRepository;
    private final TaxRateRepository taxRateRepository;

    public CartService(CartItemRepository cartItemRepository, DiscountRepository discountRepository,
                       UserRepository userRepository, CartItemLogRepository cartItemLogRepository,
                       TaxRateRepository taxRateRepository) {
        this.cartItemRepository = cartItemRepository;
        this.discountRepository = discountRepository;
        this.userRepository = userRepository;
        this.cartItemLogRepository = cartItemLogRepository;
        this.taxRateRepository = taxRateRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private double getTaxRate() {
        return taxRateRepository.findById(1L)
                .map(TaxRate::getRate)
                .orElse(22.0);
    }

    public void addProduct(CartItem item) {
        User user = getCurrentUser();
        item.setUser(user);
        cartItemRepository.save(item);
    }

    public void removeProductByName(String name) {
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findByUserAndName(user, name);
        if (item != null) {
            cartItemRepository.deleteByUserAndName(user, name);
            cartItemLogRepository.save(new CartItemLog(user, name));
        }
    }

    public List<CartItem> getCartItems() {
        User user = getCurrentUser();
        return cartItemRepository.findByUser(user);
    }

    public double cartTotal() {
        User user = getCurrentUser();
        return cartItemRepository.findByUser(user).stream()
                .mapToDouble(CartItem::totalPrice)
                .sum();
    }

    public double cartTax() {
        return cartTotal() * (getTaxRate() / 100.0);
    }

    public double cartTotalTaxed() {
        return cartTotal() * (1 + getTaxRate() / 100.0);
    }

    public void assignDiscountToProduct(String name, Long discountId) {
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findByUserAndName(user, name);
        Discount discount = discountId != null ? discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found")) : null;
        if (item != null) {
            item.setDiscount(discount);
            cartItemRepository.save(item);
        }
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public long getDiscountedProductsCount() {
        User user = getCurrentUser();
        return cartItemRepository.countByUserAndDiscountIsNotNull(user);
    }

    public double cartTotalWithDiscountSelections(List<DiscountSelection> discounts) {
        User user = getCurrentUser();
        Map<String, Double> discountMap = discounts.stream()
                .collect(Collectors.toMap(DiscountSelection::getName, DiscountSelection::getDiscountPercent,
                        (existing, replacement) -> existing));

        double total = cartItemRepository.findByUser(user).stream()
                .mapToDouble(item -> {
                    double price = item.getPrice() * item.getQuantity();
                    double discountPercent = discountMap.getOrDefault(item.getName(), 0.0);
                    double discounted = price * (1 - discountPercent / 100.0);
                    return discounted * (1 + getTaxRate() / 100.0);
                }).sum();
        return total;
    }
}