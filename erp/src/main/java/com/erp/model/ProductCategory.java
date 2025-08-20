package com.erp.model;

import javax.persistence.*;

@Entity(name = "product_category")
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_category_code")
    private Long productCategoryCode;

    @Column(name = "product_category_name")
    private String productCategoryName;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductCategoryCode() {
        return productCategoryCode;
    }

    public void setProductCategoryCode(Long productCategoryCode) {
        this.productCategoryCode = productCategoryCode;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String name) {
        this.productCategoryName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
