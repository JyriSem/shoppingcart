package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add-product")
    public void addProduct(@RequestBody Product product) {
        cartService.addProduct(product);
    }

    @DeleteMapping("remove-product/{name}")
    public void removeProductByName(@PathVariable String name) {
        cartService.removeProductByName(name);
    }

    @GetMapping("/get-cart-items")
    public List<Product> getCartItems() {
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
}