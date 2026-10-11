package com.grocery.payment.service;

/** Delivery charge rules. Free delivery for orders of Rs. 10,000 or more. */
public class DeliveryChargeCalculator {

    public static final double FREE_DELIVERY_LIMIT = 10000.0;

    public static double calculate(String city, double subtotal) {
        if (subtotal >= FREE_DELIVERY_LIMIT) {
            return 0.0;
        }
        String c = city == null ? "" : city.trim().toLowerCase();
        switch (c) {
            case "colombo":
                return 200.0;
            case "gampaha":
            case "kalutara":
                return 350.0;
            default:
                return 500.0;
        }
    }
}
