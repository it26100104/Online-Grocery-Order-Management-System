package com.grocery.model;

/**
 * NonPerishableProduct represents shelf-stable grocery goods
 * (e.g. rice, tea, pasta, canned items, cooking oil).
 */
// INHERITANCE: Inherits state and baseline methods from Product
public class NonPerishableProduct extends Product {

    public NonPerishableProduct() {
        super();
        setType("NON_PERISHABLE");
    }

    public NonPerishableProduct(String productId, String name, String category, double price,
                                int stock, String description, String shelfLifeMonths) {
        super(productId, name, category, price, stock, description, "NON_PERISHABLE", shelfLifeMonths);
    }

    public NonPerishableProduct(String productId, String name, String category, double price,
                                int stock, String description, String shelfLifeMonths, String imageUrl) {
        super(productId, name, category, price, stock, description, "NON_PERISHABLE", shelfLifeMonths, imageUrl);
    }

    // POLYMORPHISM: Custom display format highlighting durable shelf life
    @Override
    public String displayInfo() {
        return "Shelf Life: " + getExtra() + " Months";
    }

    // POLYMORPHISM: 5% standard bundle discount for shelf-stable pantry items
    @Override
    public double getDiscountedPrice() {
        return Math.round(getPrice() * 0.95 * 100.0) / 100.0;
    }
}
