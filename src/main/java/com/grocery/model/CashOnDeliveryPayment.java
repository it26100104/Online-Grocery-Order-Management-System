package com.grocery.model;

/**
 * Cash On Delivery payment option.
 */
// INHERITANCE: Inherits from Payment
public class CashOnDeliveryPayment extends Payment {

    public CashOnDeliveryPayment() {
        super();
        setMethod("COD");
    }

    public CashOnDeliveryPayment(String paymentId, String orderId, String userId,
                                double amount, String status, String date) {
        super(paymentId, orderId, userId, "COD", amount, status, date);
    }

    // POLYMORPHISM: COD is permitted up to standard limits (e.g. Rs. 50,000)
    @Override
    public boolean validate() {
        return getAmount() > 0 && getAmount() <= 50000.0;
    }

    // POLYMORPHISM: Records transaction in UNPAID status pending physical courier collection
    @Override
    public boolean processPayment() {
        if (!validate()) return false;
        setStatus("UNPAID");
        return true;
    }
}
