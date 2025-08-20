package com.erp.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "return")
@Table(name = "returns")
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "return_code")
    private Long returnCode;

    @Column(name = "purchase_order_code")
    private Long purchaseOrderCode;

    @Column(name = "supplier_code")
    private Long supplierCode;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "product_code")
    private Long productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "purchase_quantity")
    private Integer purchaseQuantity;

    @Column(name = "return_quantity")
    private Integer returnQuantity;

    @Column(name = "return_date")
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @Column(name = "reason")
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Long returnCode) {
        this.returnCode = returnCode;
    }

    public Long getPurchaseOrderCode() {
        return purchaseOrderCode;
    }

    public void setPurchaseOrderCode(Long purchaseOrderCode) {
        this.purchaseOrderCode = purchaseOrderCode;
    }

    public Long getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(Long supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
