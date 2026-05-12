package com.woundex.ecom.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.woundex.ecom.Entity.Product;
import com.woundex.ecom.Repository.ProductRepository;
import com.woundex.ecom.utils.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedissonClient redissonClient;

    private static final String RECOMMENDATION_KEY = "product:recommendations";

    public Product getProductById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            RScoredSortedSet<UUID> recommendations = redissonClient.getScoredSortedSet(RECOMMENDATION_KEY);
            recommendations.addScore(id, 1);
        }
        return product;
    }

    public List<Product> getTopRecommendedProducts(int limit) {
        RScoredSortedSet<UUID> recommendations = redissonClient.getScoredSortedSet(RECOMMENDATION_KEY);
        java.util.Collection<UUID> topIds = recommendations.valueRangeReversed(0, limit - 1);
        return productRepository.findAllById(topIds);
    }
    
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public void updateProduct(UUID id, Product product) {
        product.setId(id);
        productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Page<Product> filterProducts(
        String name,
        String category,
        double minPrice,
        double maxPrice,
        int page,
        int size,
        String sortBy,
        String sortOrder
    ) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> specification = ProductSpecification.filter(
            name,
            category,
            minPrice,
            maxPrice
        );

        return productRepository.findAll(specification, pageable);
    }


}
