package com.woundex.ecom.Controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.woundex.ecom.Entity.Product;
import com.woundex.ecom.Service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import com.woundex.ecom.dto.ProductDTO;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "0") double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
        ) {
        return ResponseEntity.ok(productService.filterProducts(name, category, minPrice, maxPrice, page, size, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(UUID.fromString(id)));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<java.util.List<Product>> getRecommendations(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(productService.getTopRecommendedProducts(limit));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody Product product) {
        productService.updateProduct(UUID.fromString(id), product);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }
}
