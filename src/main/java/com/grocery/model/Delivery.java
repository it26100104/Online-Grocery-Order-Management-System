package com.grocery.model;

/**
 * Abstract Delivery logistics entity.
 * Format: deliveryId|orderId|userId|type|address|agent|status|date|fee
 * type = STANDARD or EXPRESS, status = SCHEDULED, DISPATCHED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
 */
public abstract class Delivery {

    private String deliveryId;
    private String orderId;
    private String userId;
    private String type; // STANDARD or EXPRESS
    private String address;
    private String agent;
    private String status;
    private String date;
    private double fee;

    public Delivery() {
    }

    public Delivery(String deliveryId, String orderId, String userId, String type,
                    String address, String agent, String status, String date, double fee) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.userId = userId;
        this.type = type;
        this.address = address;
        this.agent = (agent != null && !agent.trim().isEmpty()) ? agent.trim() : "Unassigned";
        this.status = (status != null) ? status : "SCHEDULED";
        this.date = date;
        this.fee = Math.max(0.0, fee);
    }

    public abstract double calculateFee();

    public abstract String getEstimatedDelivery();

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = Math.max(0.0, fee);
    }

    public boolean isDelivered() {
        return "DELIVERED".equalsIgnoreCase(this.status);
    }

    public String toDataLine() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%.2f",
                deliveryId,
                orderId,
                userId,
                type,
                address.replace("|", ", "),
                agent.replace("|", " "),
                status,
                date,
                fee);
    }
}
