package com.woundex.ecom.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.woundex.ecom.Entity.Cart;
import com.woundex.ecom.Entity.Item;
import com.woundex.ecom.Entity.Product;
import com.woundex.ecom.Repository.CartRepository;
import com.woundex.ecom.Repository.ItemRepository;
import com.woundex.ecom.Repository.ProductRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedissonClient redissonClient;

    public Cart getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public void addItemToCart(UUID userId, UUID productId, int quantity) {
        String lockKey = "lock:cart:" + userId + ":" + productId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(10, 20, java.util.concurrent.TimeUnit.SECONDS)) {
                Cart cart = getCartByUserId(userId);
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                Optional<Item> existingItem = cart.getItems().stream()
                        .filter(item -> item.getProduct().getId().equals(productId))
                        .findFirst();

                if (existingItem.isPresent()) {
                    Item item = existingItem.get();
                    item.setQuantity(item.getQuantity() + quantity);
                    item.setTotalPrice(item.getQuantity() * product.getPrice());
                } else {
                    Item newItem = new Item();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(quantity);
                    newItem.setTotalPrice(quantity * product.getPrice());
                    cart.getItems().add(newItem);
                }

                cartRepository.save(cart);
            } else {
                throw new RuntimeException("Could not acquire lock for cart update: " + lockKey);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for lock", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Transactional
    public void removeItemFromCart(UUID userId, Long itemId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(UUID userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
