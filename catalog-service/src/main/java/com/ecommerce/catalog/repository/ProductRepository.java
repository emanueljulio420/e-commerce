package com.ecommerce.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.catalog.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}