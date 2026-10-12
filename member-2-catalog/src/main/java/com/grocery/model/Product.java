package com.grocery.model;

/**
 * Abstract base class representing a grocery product item.
 * Serves as the blueprint for PerishableProduct and NonPerishableProduct.
 */
// ABSTRACTION: Abstract template defining core properties and business logic contracts
public abstract class Product {

    // ENCAPSULATION: Private fields accessible only via validated getters and setters
    private String productId;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String description;
    private String type; // PERISHABLE or NON_PERISHABLE
    private String extra; // Expiry date (e.g. 2026-10-25) OR Shelf life in months (e.g. 24)
    private String imageUrl; // Web URL or preset photo representing the product

    public Product() {
    }

    public Product(String productId, String name, String category, double price,
                   int stock, String description, String type, String extra) {
        this(productId, name, category, price, stock, description, type, extra, null);
    }

    public Product(String productId, String name, String category, double price,
                   int stock, String description, String type, String extra, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        setPrice(price);
        setStock(stock);
        this.description = description;
        this.type = type;
        this.extra = extra;
        setImageUrl(imageUrl);
    }

    // POLYMORPHISM: Abstract methods overridden by subclasses for item-specific details and pricing
    public abstract String displayInfo();

    public abstract double getDiscountedPrice();

    // Getters and Setters with Encapsulation & Validation
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            this.category = category.trim();
        }
    }

    public double getPrice() {
        return price;
    }

    // Validation: Price cannot be negative
    public void setPrice(double price) {
        this.price = Math.max(0.0, price);
    }

    public int getStock() {
        return stock;
    }

    // Validation: Stock cannot be negative
    public void setStock(int stock) {
        this.stock = Math.max(0, stock);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description.trim() : "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = (extra != null) ? extra.trim() : "";
    }

    public String getImageUrl() {
        if (this.imageUrl != null && !this.imageUrl.trim().isEmpty()) {
            return this.imageUrl.trim();
        }
        return getDefaultImageFor(this.name, this.category);
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            this.imageUrl = imageUrl.trim();
        } else {
            this.imageUrl = getDefaultImageFor(this.name, this.category);
        }
    }

    public boolean isInStock() {
        return this.stock > 0;
    }

    /**
     * Resolves a curated, high-quality photograph based on product keywords and category.
     * Prevents any missing images or broken '??' characters from displaying.
     */
    public static String getDefaultImageFor(String name, String category) {
        String query = ((name != null ? name : "") + " " + (category != null ? category : "")).toLowerCase();
        
        if (query.contains("milk") || query.contains("dairy")) {
            return "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("apple") || query.contains("fruit")) {
            return "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("rice") || query.contains("grain") || query.contains("flour")) {
            return "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("tea") || query.contains("beverage") || query.contains("coffee")) {
            return "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("egg") || query.contains("poultry")) {
            return "https://images.unsplash.com/photo-1506976785307-8732e854ad03?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("oil") || query.contains("olive")) {
            return "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("bread") || query.contains("bakery") || query.contains("loaf")) {
            return "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("veg") || query.contains("broccoli") || query.contains("produce") || query.contains("tomato")) {
            return "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500&auto=format&fit=crop&q=60";
        } else if (query.contains("avocado")) {
            return "https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=500&auto=format&fit=crop&q=60";
        }
        
        // General clean organic grocery fallback
        return "https://images.unsplash.com/photo-1542838132-92c53300491e?w=500&auto=format&fit=crop&q=60";
    }

    /**
     * Serializes this product into the pipe-delimited storage format:
     * productId|name|category|price|stock|description|type|extra|imageUrl
     */
    public String toDataLine() {
        String img = (imageUrl != null && !imageUrl.trim().isEmpty()) ? imageUrl.replace("|", "") : getDefaultImageFor(name, category);
        return String.format("%s|%s|%s|%.2f|%d|%s|%s|%s|%s",
                productId,
                name,
                category,
                price,
                stock,
                description.replace("|", " "),
                type,
                extra.replace("|", " "),
                img);
    }
}
