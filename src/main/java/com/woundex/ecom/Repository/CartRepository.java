package com.woundex.ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.woundex.ecom.Entity.Cart;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> , JpaSpecificationExecutor<Cart> {

    Optional<Cart> findByUserId(UUID userId);
    
}