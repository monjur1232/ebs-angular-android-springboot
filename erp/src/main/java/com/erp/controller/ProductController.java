package com.erp.controller;

import com.erp.common.ICommonController;
import com.erp.model.Product;
import com.erp.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController implements ICommonController<Product> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable(value = "id") Long productId) {
        Product product = productRepository.getById(productId);
        return ResponseEntity.ok().body(product);
    }

    @Override
    @PostMapping
    public Product save(@Validated @RequestBody Product product) {
        return productRepository.save(product);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable(value = "id") Long productId,
                                          @Validated @RequestBody Product productDetails) {
        Product product = productRepository.getById(productId);

        product.setProductCode(productDetails.getProductCode());
        product.setProductName(productDetails.getProductName());
        product.setProductCategoryCode(productDetails.getProductCategoryCode());
        product.setProductCategoryName(productDetails.getProductCategoryName());        
        product.setDescription(productDetails.getDescription());
        product.setReorderLevel(productDetails.getReorderLevel());
        product.setStatus(productDetails.getStatus());

        final Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @Override
    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long productId) {
        Product product = productRepository.getById(productId);
        productRepository.delete(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
    
}
