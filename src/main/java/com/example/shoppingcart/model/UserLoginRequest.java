package com.example.shoppingcart.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    //hc
    private String username = "user";
    private String password = "password";
}