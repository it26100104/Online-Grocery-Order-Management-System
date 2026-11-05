package com.grocery.model;

/**
 * Credit/Debit Card online payment processing.
 * Sensitive numbers are strictly transient and never written to storage.
 */
// INHERITANCE: Inherits from Payment
public class CardPayment extends Payment {

    private transient String cardNumber;
    private transient String expiry;
    private transient String cvv;

    public CardPayment() {
        super();
        setMethod("CARD");
    }

    public CardPayment(String paymentId, String orderId, String userId,
                       double amount, String status, String date) {
        super(paymentId, orderId, userId, "CARD", amount, status, date);
    }

    public void setCardDetails(String cardNumber, String expiry, String cvv) {
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    // POLYMORPHISM: Validates card length and cvv presence
    @Override
    public boolean validate() {
        if (getAmount() <= 0) return false;
        if (cardNumber == null) return true; // Existing saved card method
        String cleanNumber = cardNumber.replaceAll("\\s+", "");
        return cleanNumber.length() >= 12 && cvv != null && cvv.length() >= 3;
    }

    // POLYMORPHISM: Approves payment and sets status to PAID
    @Override
    public boolean processPayment() {
        if (!validate()) return false;
        setStatus("PAID");
        return true;
    }
}
