package com.erp.repository;

import com.erp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "productRepository")
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

}
