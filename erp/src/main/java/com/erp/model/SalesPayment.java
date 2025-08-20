package com.erp.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "sales_payment")
@Table(name = "sales_payment")
public class SalesPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sales_payment_code")
    private Long salesPaymentCode;

    @Column(name = "customer_code")
    private Long customerCode;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "sales_order_code")
    private Long salesOrderCode;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "pay_date")
    @Temporal(TemporalType.DATE)
    private Date payDate;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "payment_ref")
    private String paymentRef;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalesPaymentCode() {
        return salesPaymentCode;
    }

    public void setSalesPaymentCode(Long salesPaymentCode) {
        this.salesPaymentCode = salesPaymentCode;
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

    public Long getSalesOrderCode() {
        return salesOrderCode;
    }

    public void setSalesOrderCode(Long salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }
    
}
