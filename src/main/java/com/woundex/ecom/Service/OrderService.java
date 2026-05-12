package com.woundex.ecom.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woundex.ecom.Entity.Cart;
import com.woundex.ecom.Entity.Order;
import com.woundex.ecom.Entity.OrderItem;
import com.woundex.ecom.Repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrderFromCart(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getTotalPrice())
                .sum();
        order.setTotalAmount(totalAmount);

        order.setItems(cart.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getProduct().getId());
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            return orderItem;
        }).collect(Collectors.toList()));

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        return savedOrder;
    }
}
