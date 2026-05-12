package com.woundex.ecom.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.woundex.ecom.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}
