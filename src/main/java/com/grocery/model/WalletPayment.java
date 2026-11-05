package com.grocery.model;

/**
 * Mobile / Digital Wallet payment option (e.g. FriMi, eZ Cash, Genie).
 */
// INHERITANCE: Inherits from Payment
public class WalletPayment extends Payment {

    private transient String walletMobile;

    public WalletPayment() {
        super();
        setMethod("WALLET");
    }

    public WalletPayment(String paymentId, String orderId, String userId,
                         double amount, String status, String date) {
        super(paymentId, orderId, userId, "WALLET", amount, status, date);
    }

    public void setWalletMobile(String walletMobile) {
        this.walletMobile = walletMobile;
    }

    // POLYMORPHISM: Validates mobile wallet number
    @Override
    public boolean validate() {
        if (getAmount() <= 0) return false;
        if (walletMobile == null) return true; // Saved wallet method
        String clean = walletMobile.replaceAll("[^0-9]", "");
        return clean.length() >= 9;
    }

    // POLYMORPHISM: Approves payment and sets status to PAID
    @Override
    public boolean processPayment() {
        if (!validate()) return false;
        setStatus("PAID");
        return true;
    }
}
