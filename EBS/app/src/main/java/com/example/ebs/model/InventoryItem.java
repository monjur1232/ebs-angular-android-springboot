package com.example.ebs.model;

public class InventoryItem {
    private Long productCode;
    private String productName;
    private Double unitPrice;
    private int totalPurchased = 0;
    private int totalSold = 0;
    private int currentStock = 0;

    // Getters and Setters
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getTotalPurchased() {
        return totalPurchased;
    }

    public void addPurchased(int quantity) {
        this.totalPurchased += quantity;
        calculateStock();
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void addSold(int quantity) {
        this.totalSold += quantity;
        calculateStock();
    }

    public int getCurrentStock() {
        return currentStock;
    }

    private void calculateStock() {
        this.currentStock = this.totalPurchased - this.totalSold;
    }
}