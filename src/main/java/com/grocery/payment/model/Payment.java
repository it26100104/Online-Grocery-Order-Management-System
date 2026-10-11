package com.grocery.payment.model;

/**
 * ABSTRACTION + ENCAPSULATION:
 * Base class for all payment methods. Child classes decide their own fee.
 */
public abstract class Payment {

    private String method;   // private = encapsulation

    protected Payment(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    /** Each payment type calculates its own fee (POLYMORPHISM). */
    public abstract double calculateFee(double amount);

    public abstract String getDescription();
}
