package com.grocery.payment.model;

/** INHERITANCE: Cash on Delivery has a flat Rs. 50 handling fee. */
public class CashOnDeliveryPayment extends Payment {

    public CashOnDeliveryPayment() {
        super("COD");
    }

    @Override
    public double calculateFee(double amount) {
        return 50.0;
    }

    @Override
    public String getDescription() {
        return "Cash on Delivery";
    }
}
