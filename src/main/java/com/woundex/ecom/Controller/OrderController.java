package com.woundex.ecom.Controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woundex.ecom.Entity.Order;
import com.woundex.ecom.Entity.User;
import com.woundex.ecom.Repository.UserRepository;
import com.woundex.ecom.Service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    private UUID getCurrentUserId(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @PostMapping
    public Order createOrder(Principal principal) {
        return orderService.createOrderFromCart(getCurrentUserId(principal));
    }
}
