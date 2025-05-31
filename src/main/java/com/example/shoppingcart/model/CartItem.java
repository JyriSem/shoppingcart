package com.example.shoppingcart.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    public CartItem() {
    }

    public CartItem(User user, String name, double price, int quantity, Discount discount) {
        this.user = user;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public double totalPrice() {
        return price * quantity;
    }

    public double totalPriceWithDiscount() {
        double discountRate = (discount != null) ? discount.getPercentage() / 100.0 : 0.0;
        return totalPrice() * (1 - discountRate);
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    @Override
    public String toString() {
        return "CartItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + (discount != null ? discount.getPercentage() + "%" : "none") +
                '}';
    }
}