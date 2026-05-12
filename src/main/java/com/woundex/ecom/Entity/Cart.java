package com.woundex.ecom.Entity;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

}
