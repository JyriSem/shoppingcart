package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartService {
    private final ProductRepository productRepository;

    private final double TAX = 22.0;

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProductByName(String name) {
        productRepository.deleteByName(name);
    }

    public List<Product> getCartItems() {
        return productRepository.findAll();
    }

    public double cartTotal() {
        List<Product> cartItems = productRepository.findAll();
        double total = 0;

        for (Product product : cartItems) {
            total += product.totalPrice();
        }
        return total;
    }

    public double cartTax() {
        List<Product> cartItems = productRepository.findAll();
        double taxed = 0;
        double tax = 0.22;

        for (Product product : cartItems) {
            taxed += product.totalPrice() * tax;
        }
        return taxed;
    }

    public double calcTax() {
        return 1 + TAX / 100;
    }

    public double cartTotalTaxed() {
        List<Product> cartItems = productRepository.findAll();
        double total = 0;

        for (Product product : cartItems) {
            total += product.totalPrice();
        }
        return total * calcTax();
    }
}