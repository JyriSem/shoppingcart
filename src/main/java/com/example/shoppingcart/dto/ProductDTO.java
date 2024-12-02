package com.example.shoppingcart.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private double total;
}