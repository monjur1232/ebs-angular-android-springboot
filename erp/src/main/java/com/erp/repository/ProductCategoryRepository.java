package com.erp.repository;

import com.erp.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "productCategoryRepository")
@Transactional
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	
}
