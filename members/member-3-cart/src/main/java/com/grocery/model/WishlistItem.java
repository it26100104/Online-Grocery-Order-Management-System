package com.grocery.model;

/**
 * Concrete WishlistItem representing a saved product for later purchase.
 * File format: wishId|userId|productId|productName|unitPrice
 */
// INHERITANCE: Inherits from ListItem
public class WishlistItem extends ListItem {

    public WishlistItem() {
        super();
    }

    public WishlistItem(String wishId, String userId, String productId,
                        String productName, double unitPrice) {
        super(wishId, userId, productId, productName, unitPrice);
    }

    // POLYMORPHISM: Line total for a wishlist item equals its unit price
    @Override
    public double getLineTotal() {
        return getUnitPrice();
    }

    // POLYMORPHISM: Formats display text for wishlist line item
    @Override
    public String displayInfo() {
        return "Saved: " + getProductName() + " (Rs. " + String.format("%.2f", getUnitPrice()) + ")";
    }

    public String toDataLine() {
        return String.format("%s|%s|%s|%s|%.2f",
                getItemId(),
                getUserId(),
                getProductId(),
                getProductName(),
                getUnitPrice());
    }
}
