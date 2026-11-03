package com.grocery.model;

/**
 * Standard ground shipping for grocery orders.
 */
// INHERITANCE: Inherits from Delivery
public class StandardDelivery extends Delivery {

    public StandardDelivery() {
        super();
        setType("STANDARD");
    }

    public StandardDelivery(String deliveryId, String orderId, String userId,
                            String address, String agent, String status, String date, double fee) {
        super(deliveryId, orderId, userId, "STANDARD", address, agent, status, date, fee);
    }

    // POLYMORPHISM: Fixed standard courier rate (Rs. 250.00)
    @Override
    public double calculateFee() {
        return 250.00;
    }

    // POLYMORPHISM: Standard delivery SLA
    @Override
    public String getEstimatedDelivery() {
        return "2 - 3 Business Days (Standard Ground)";
    }
}
