package com.example.shoppingcart.service;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println("Loading user: " + username);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        System.out.println("User not found: " + username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });
            System.out.println("User found: " + username + ", role: " + user.getRole());
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        } catch (Exception e) {
            System.err.println("Error loading user " + username + ": " + e.getClass().getName() + " - " + e.getMessage());
            throw new UsernameNotFoundException("Failed to load user: " + username, e);
        }
    }
}