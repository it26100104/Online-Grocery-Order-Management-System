package com.grocery.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Order entity representing a placed grocery order.
 * Format: orderId|userId|orderType|items|subtotal|tax|total|address|status|date
 */
// ABSTRACTION: Base contract for orders with polymorphic price calculation
public abstract class Order {

    // ENCAPSULATION: Private attributes
    private String orderId;
    private String userId;
    private String orderType; // REGULAR or BULK
    private List<OrderItem> items = new ArrayList<>();
    private double subtotal;
    private double tax;
    private double total;
    private String address;
    private String status; // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    private String date;

    public Order() {
    }

    public Order(String orderId, String userId, String orderType, List<OrderItem> items,
                 double subtotal, double tax, double total, String address, String status, String date) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderType = orderType;
        if (items != null) this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.address = address;
        this.status = (status != null) ? status : "PENDING";
        this.date = date;
    }

    // POLYMORPHISM: Abstract method implemented differently by RegularOrder and BulkOrder
    public abstract double calculateTotal();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = (items != null) ? items : new ArrayList<>();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(this.status);
    }

    public String getSerializedItems() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i).toToken());
            if (i < items.size() - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public String toDataLine() {
        return String.format("%s|%s|%s|%s|%.2f|%.2f|%.2f|%s|%s|%s",
                orderId,
                userId,
                getOrderType(),
                getSerializedItems(),
                subtotal,
                tax,
                total,
                address.replace("|", ", "),
                status,
                date);
    }
}
