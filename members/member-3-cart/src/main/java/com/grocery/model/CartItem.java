package com.grocery.model;

/**
 * Concrete CartItem representing an item in the user's shopping basket.
 * File format: cartItemId|userId|productId|productName|unitPrice|quantity
 */
// INHERITANCE: Inherits from ListItem
public class CartItem extends ListItem {

    private int quantity;

    public CartItem() {
        super();
    }

    public CartItem(String cartItemId, String userId, String productId,
                    String productName, double unitPrice, int quantity) {
        super(cartItemId, userId, productId, productName, unitPrice);
        this.quantity = Math.max(1, quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    // POLYMORPHISM: Calculates line total based on unit price * quantity
    @Override
    public double getLineTotal() {
        return Math.round(getUnitPrice() * quantity * 100.0) / 100.0;
    }

    // POLYMORPHISM: Formats display text for cart line item
    @Override
    public String displayInfo() {
        return quantity + " x Rs. " + String.format("%.2f", getUnitPrice());
    }

    public String toDataLine() {
        return String.format("%s|%s|%s|%s|%.2f|%d",
                getItemId(),
                getUserId(),
                getProductId(),
                getProductName(),
                getUnitPrice(),
                quantity);
    }
}
