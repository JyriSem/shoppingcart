package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Discount;
import com.example.shoppingcart.repository.DiscountRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {
    private final DiscountRepository discountRepository;

    public DiscountController(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @GetMapping
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }
}