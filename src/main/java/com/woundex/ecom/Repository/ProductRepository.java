package com.woundex.ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import com.woundex.ecom.Entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> , JpaSpecificationExecutor<Product> {
    
}