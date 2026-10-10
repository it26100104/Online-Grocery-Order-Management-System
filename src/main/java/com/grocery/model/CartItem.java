package com.grocery.model;

public class CartItem {

    private String userId;
    private String productId;
    private String productName;
    private double price;
    private int quantity;

    public CartItem(String userId, String productId, String productName,
                    double price, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    public double getLineTotal() {
        return price * quantity;
    }

    public String toFileString() {
        return userId + "|" + productId + "|" + productName + "|" + price + "|" + quantity;
    }
}