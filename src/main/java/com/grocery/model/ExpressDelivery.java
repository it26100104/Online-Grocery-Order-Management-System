package com.grocery.model;

/**
 * Priority Express refrigerated courier for fast, perishables-first delivery.
 */

public class ExpressDelivery extends Delivery {

    public ExpressDelivery() {
        super();
        setType("EXPRESS");
    }

    public ExpressDelivery(String deliveryId, String orderId, String userId,
                           String address, String agent, String status, String date, double fee) {
        super(deliveryId, orderId, userId, "EXPRESS", address, agent, status, date, fee);
    }

    @Override
    public double calculateFee() {
        return 500.00;
    }

    
    @Override
    public String getEstimatedDelivery() {
        return "Same-Day Priority Express (Within 4 Hours)";
    }
}
