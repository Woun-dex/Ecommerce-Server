package com.woundex.ecom.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CartItemRequest {
    private UUID productId;
    private int quantity;
}
