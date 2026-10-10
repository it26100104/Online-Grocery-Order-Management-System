package com.grocery.model;

public class WishlistItem {

    private String userId;
    private String productId;
    private String productName;
    private double price;

    public WishlistItem(String userId, String productId, String productName, double price) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String toFileString() {
        return userId + "|" + productId + "|" + productName + "|" + price;
    }
}
