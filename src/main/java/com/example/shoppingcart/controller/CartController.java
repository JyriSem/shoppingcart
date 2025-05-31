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
        if (item.getName() == null || item.getName().isBlank() || item.getPrice() <= 0 || item.getQuantity() < 1) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid product: Name must be non-empty, price and quantity must be positive"));
        }
        cartService.addProduct(item);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove-product/{name}")
    public void removeProductByName(@PathVariable String name) {
        cartService.removeProductByName(name);
    }

    @GetMapping("/get-cart-items")
    public List<CartItem> getCartItems() {
        return cartService.getCartItems();
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
}