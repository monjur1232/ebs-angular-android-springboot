package com.erp.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "payment")
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "payment_code")
    private Long paymentCode;

    @Column(name = "supplier_code")
    private Long supplierCode;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "purchase_order_code")
    private Long purchaseOrderCode;

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

	public Long getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(Long paymentCode) {
		this.paymentCode = paymentCode;
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

	public Long getPurchaseOrderCode() {
		return purchaseOrderCode;
	}

	public void setPurchaseOrderCode(Long purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
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
