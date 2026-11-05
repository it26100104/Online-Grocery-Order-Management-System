package com.grocery.model;

/**
 * Abstract base class for list items (CartItem and WishlistItem).
 */
// ABSTRACTION: Common contract for shopping list entries
public abstract class ListItem {

    
    private String itemId;
    private String userId;
    private String productId;
    private String productName;
    private double unitPrice;

    public ListItem() {
    }

    public ListItem(String itemId, String userId, String productId, String productName, double unitPrice) {
        this.itemId = itemId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = Math.max(0.0, unitPrice);
    }

    // POLYMORPHISM: Abstract methods overridden by CartItem and WishlistItem
    public abstract double getLineTotal();

    public abstract String displayInfo();

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = Math.max(0.0, unitPrice);
    }
}
