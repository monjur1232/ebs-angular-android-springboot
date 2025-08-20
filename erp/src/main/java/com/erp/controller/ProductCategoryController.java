package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.ProductCategory;
import com.erp.repository.ProductCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product-category")
public class ProductCategoryController implements ICommonController<ProductCategory> {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    @GetMapping
    public List<ProductCategory> getAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getById(@PathVariable(value = "id") Long id) {
        ProductCategory category = productCategoryRepository.getById(id);
        return ResponseEntity.ok().body(category);
    }

    @Override
    @PostMapping
    public ProductCategory save(@Validated @RequestBody ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> update(@PathVariable(value = "id") Long id,
                                                  @Validated @RequestBody ProductCategory categoryDetails) {
        ProductCategory category = productCategoryRepository.getById(id);

        category.setProductCategoryCode(categoryDetails.getProductCategoryCode());
        category.setProductCategoryName(categoryDetails.getProductCategoryName());
        category.setDescription(categoryDetails.getDescription());

        final ProductCategory updatedCategory = productCategoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long id) {
        ProductCategory category = productCategoryRepository.getById(id);
        productCategoryRepository.delete(category);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
