package com.grocery.payment.model;

/** INHERITANCE: CardPayment IS-A Payment. Card fee = 2% of the amount. */
public class CardPayment extends Payment {

    private String cardHolder;
    private String lastFourDigits;   // we never store the full card number

    public CardPayment(String cardHolder, String cardNumber) {
        super("CARD");
        this.cardHolder = cardHolder;
        this.lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public double calculateFee(double amount) {
        return amount * 0.02;
    }

    @Override
    public String getDescription() {
        return "Card payment (**** **** **** " + lastFourDigits + ") - " + cardHolder;
    }
}
