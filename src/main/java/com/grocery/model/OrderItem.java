package com.grocery.model;

/**
 * Snapshot of an ordered item capturing product name and price at the exact moment of checkout.
 * Format: productId:name:price:qty
 */
// ENCAPSULATION: Encapsulates individual order line items
public class OrderItem {

    private String productId;
    private String name;
    private double price;
    private int quantity;

    public OrderItem() {
    }

    public OrderItem(String productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = Math.max(0.0, price);
        this.quantity = Math.max(1, quantity);
    }

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
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = Math.max(0.0, price);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    public double getLineTotal() {
        return Math.round(price * quantity * 100.0) / 100.0;
    }

    /**
     * Serializes this item into: productId:name:price:qty
     */
    public String toToken() {
        return String.format("%s:%s:%.2f:%d",
                productId,
                name.replace(":", " ").replace(";", " "),
                price,
                quantity);
    }

    public static OrderItem parse(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        String[] parts = token.split(":");
        if (parts.length < 4) {
            return null;
        }
        try {
            String pId = parts[0].trim();
            String name = parts[1].trim();
            double price = Double.parseDouble(parts[2].trim());
            int qty = Integer.parseInt(parts[3].trim());
            return new OrderItem(pId, name, price, qty);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
