package com.grocery.model;

/**
 * Abstract Payment transaction base class.
 * Format: TXN|paymentId|orderId|userId|method|amount|status|date
 */
// ABSTRACTION: Common contract for payment processing
public abstract class Payment {

    // ENCAPSULATION: Private fields
    private String paymentId;
    private String orderId;
    private String userId;
    private String method; // COD, CARD, WALLET
    private double amount;
    private String status; // UNPAID, PAID, REFUND_REQUESTED, REFUNDED, VOID
    private String date;

    public Payment() {
    }

    public Payment(String paymentId, String orderId, String userId, String method,
                   double amount, String status, String date) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.userId = userId;
        this.method = method;
        this.amount = Math.max(0.0, amount);
        this.status = (status != null) ? status : "UNPAID";
        this.date = date;
    }

    // POLYMORPHISM: Abstract payment execution and validation methods
    public abstract boolean processPayment();

    public abstract boolean validate();

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = Math.max(0.0, amount);
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

    public boolean isPaid() {
        return "PAID".equalsIgnoreCase(this.status);
    }

    public String toDataLine() {
        return String.format("TXN|%s|%s|%s|%s|%.2f|%s|%s",
                paymentId,
                orderId,
                userId,
                method,
                amount,
                status,
                date);
    }
}
