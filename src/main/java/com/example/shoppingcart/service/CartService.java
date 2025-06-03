package com.example.shoppingcart.service;

import com.example.shoppingcart.model.*;
import com.example.shoppingcart.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
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

    public double getTaxRate() {
        return taxRateRepository.findById(1L)
                .map(TaxRate::getRate)
                .orElse(22.0);
    }

    public void addProduct(CartItem item) {
        User user = getCurrentUser();
        if (cartItemRepository.findByUserAndName(user, item.getName()) != null) {
            throw new IllegalArgumentException("Product with this name already exists in the cart");
        }
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

    public void updateProductQuantity(String name, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findByUserAndName(user, name);
        if (item == null) {
            throw new IllegalArgumentException("Product not found in cart");
        }
        cartItemRepository.updateQuantityByUserAndName(user, name, quantity);
    }

    public List<CartItem> getCartItems(String sortBy, String sortDirection) {
        User user = getCurrentUser();
        switch (sortBy.toLowerCase()) {
            case "name":
                return sortDirection.equalsIgnoreCase("desc") ?
                       cartItemRepository.findByUserOrderByNameDesc(user) :
                       cartItemRepository.findByUserOrderByNameAsc(user);
            case "quantity":
                return sortDirection.equalsIgnoreCase("desc") ?
                       cartItemRepository.findByUserOrderByQuantityDesc(user) :
                       cartItemRepository.findByUserOrderByQuantityAsc(user);
            case "price":
                return sortDirection.equalsIgnoreCase("desc") ?
                       cartItemRepository.findByUserOrderByPriceDesc(user) :
                       cartItemRepository.findByUserOrderByPriceAsc(user);
            default:
                return cartItemRepository.findByUserOrderByIdAsc(user);
        }
    }

    public double cartTotal() {
        User user = getCurrentUser();
        return cartItemRepository.findByUserOrderByIdAsc(user).stream()
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
        return cartItemRepository.countByUserAndDiscountIdIn(user, Arrays.asList(2L, 3L, 4L));
    }

    public double cartTotalWithDiscountSelections(List<DiscountSelection> discounts) {
        User user = getCurrentUser();
        Map<String, Double> discountMap = discounts.stream()
                .collect(Collectors.toMap(DiscountSelection::getName, DiscountSelection::getDiscountPercent,
                        (existing, replacement) -> existing));

        double total = cartItemRepository.findByUserOrderByIdAsc(user).stream()
                .mapToDouble(item -> {
                    double price = item.getPrice() * item.getQuantity();
                    double discountPercent = discountMap.getOrDefault(item.getName(), 
                        item.getDiscount() != null ? item.getDiscount().getPercentage() : 0.0);
                    double discounted = price * (1 - discountPercent / 100.0);
                    return discounted * (1 + getTaxRate() / 100.0);
                }).sum();
        return total;
    }
}