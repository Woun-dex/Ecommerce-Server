package com.woundex.ecom.Controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woundex.ecom.Entity.Cart;
import com.woundex.ecom.Entity.User;
import com.woundex.ecom.Repository.UserRepository;
import com.woundex.ecom.Service.CartService;
import com.woundex.ecom.dto.CartItemRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    private UUID getCurrentUserId(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @GetMapping
    public Cart getCart(Principal principal) {
        return cartService.getCartByUserId(getCurrentUserId(principal));
    }

    @PostMapping("/items")
    public void addItemToCart(@RequestBody CartItemRequest request, Principal principal) {
        cartService.addItemToCart(getCurrentUserId(principal), request.getProductId(), request.getQuantity());
    }

    @DeleteMapping("/items/{id}")
    public void removeItemFromCart(@PathVariable Long id, Principal principal) {
        cartService.removeItemFromCart(getCurrentUserId(principal), id);
    }
}
