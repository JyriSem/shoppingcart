package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Discount;
import com.example.shoppingcart.model.DiscountSelection;
import com.example.shoppingcart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestBody CartItem item) {
        if (item.getName() == null || item.getName().isBlank() || item.getPrice() <= 0 || item.getQuantity() < 1
                || !item.isValidName()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error",
                            "Invalid product: Name must be non-empty, alphanumeric with spaces, hyphens, or periods, and price/quantity must be positive"));
        }
        try {
            cartService.addProduct(item);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove-product/{name}")
    public void removeProductByName(@PathVariable String name) {
        cartService.removeProductByName(name);
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateQuantity(@RequestParam String name, @RequestParam int quantity) {
        try {
            cartService.updateProductQuantity(name, quantity);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/get-cart-items")
    public List<CartItem> getCartItems(@RequestParam(defaultValue = "id") String sortBy,
                                       @RequestParam(defaultValue = "asc") String sortDirection) {
        return cartService.getCartItems(sortBy, sortDirection);
    }

    @GetMapping("/cart-total")
    public double cartTotal() {
        return cartService.cartTotal();
    }

    @GetMapping("/cart-tax")
    public double cartTax() {
        return cartService.cartTax();
    }

    @GetMapping("/cart-total-taxed")
    public double cartTotalTaxed() {
        return cartService.cartTotalTaxed();
    }

    @PutMapping("/assign-discount")
    public void assignDiscount(@RequestParam String productName, @RequestParam(required = false) Long discountId) {
        cartService.assignDiscountToProduct(productName, discountId);
    }

    @GetMapping("/discounts")
    public List<Discount> getAllDiscounts() {
        return cartService.getAllDiscounts();
    }

    @GetMapping("/discount-count")
    public long getDiscountCount() {
        return cartService.getDiscountedProductsCount();
    }

    @PostMapping("/cart-total-discounted-items")
    public double cartTotalDiscountedItems(@RequestBody List<DiscountSelection> discountSelections) {
        return cartService.cartTotalWithDiscountSelections(discountSelections);
    }

    @GetMapping("/tax-rate")
    public double getTaxRate() {
        return cartService.getTaxRate();
    }
}