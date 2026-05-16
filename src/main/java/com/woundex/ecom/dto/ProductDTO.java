package com.woundex.ecom.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String category;
    private double price;
    private MultipartFile image;
    private int stockQuantity;
    private String description;

    

}
