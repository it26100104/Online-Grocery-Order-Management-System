package com.grocery.payment.service;

import com.grocery.payment.model.CardPayment;
import com.grocery.payment.model.CashOnDeliveryPayment;
import com.grocery.payment.model.Payment;

/** Returns the correct Payment child object. The caller only sees "Payment" (POLYMORPHISM). */
public class PaymentFactory {

    public static Payment create(String method, String cardHolder, String cardNumber) {
        if ("CARD".equals(method)) {
            return new CardPayment(cardHolder, cardNumber);
        }
        return new CashOnDeliveryPayment();
    }
}
