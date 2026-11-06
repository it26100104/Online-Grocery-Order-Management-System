package com.grocery.model;

/**
 * PerishableProduct represents grocery goods with a strict expiry date
 * (e.g. milk, fresh fruits, vegetables, eggs, dairy).
 */
// INHERITANCE: Inherits state and baseline methods from Product
public class PerishableProduct extends Product {

    public PerishableProduct() {
        super();
        setType("PERISHABLE");
    }

    public PerishableProduct(String productId, String name, String category, double price,
                             int stock, String description, String expiryDate) {
        super(productId, name, category, price, stock, description, "PERISHABLE", expiryDate);
    }

    public PerishableProduct(String productId, String name, String category, double price,
                             int stock, String description, String expiryDate, String imageUrl) {
        super(productId, name, category, price, stock, description, "PERISHABLE", expiryDate, imageUrl);
    }

    // POLYMORPHISM: Custom display format highlighting expiration date
    @Override
    public String displayInfo() {
        return "Expiry Date: " + getExtra();
    }

    // POLYMORPHISM: 10% promotional clearance discount for fresh perishable goods
    @Override
    public double getDiscountedPrice() {
        return Math.round(getPrice() * 0.90 * 100.0) / 100.0;
    }
}
