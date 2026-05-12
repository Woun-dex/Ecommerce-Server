package com.woundex.ecom.utils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.Predicate;
import com.woundex.ecom.Entity.Product;

@Component
public class ProductSpecification {

    public static Specification<Product> filter(
        String name,
        String category,
        double minPrice,
        double maxPrice
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("name"), "%" + name + "%"));
            }

            if (category != null && !category.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("category"), category));
            }

            if (minPrice > 0) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice > 0) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return predicate;
        };
    }
    
}
