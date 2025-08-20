package com.erp.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "sales_return")
@Table(name = "sales_return")
public class SalesReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sales_return_code")
    private Long salesReturnCode;

    @Column(name = "sales_order_code")
    private Long salesOrderCode;

    @Column(name = "customer_code")
    private Long customerCode;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "product_code")
    private Long productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "sales_quantity")
    private Integer salesQuantity;

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

    public Long getSalesReturnCode() {
        return salesReturnCode;
    }

    public void setSalesReturnCode(Long salesReturnCode) {
        this.salesReturnCode = salesReturnCode;
    }

    public Long getSalesOrderCode() {
        return salesOrderCode;
    }

    public void setSalesOrderCode(Long salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    public Long getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Long customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Integer getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(Integer salesQuantity) {
        this.salesQuantity = salesQuantity;
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
