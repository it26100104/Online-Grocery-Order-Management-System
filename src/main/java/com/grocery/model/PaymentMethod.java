package com.grocery.model;

/**
 * Saved payment method (card or digital wallet).
 * Never stores full credit card numbers or security CVV codes.
 * Format: METHOD|methodId|userId|type|label|last4
 */
// ENCAPSULATION: Safely encapsulates masked payment credentials
public class PaymentMethod {

    private String methodId;
    private String userId;
    private String type; // CARD or WALLET
    private String label;
    private String last4;

    public PaymentMethod() {
    }

    public PaymentMethod(String methodId, String userId, String type, String label, String last4) {
        this.methodId = methodId;
        this.userId = userId;
        this.type = type;
        this.label = label;
        this.last4 = last4;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String toDataLine() {
        return String.format("METHOD|%s|%s|%s|%s|%s",
                methodId,
                userId,
                type,
                label.replace("|", " "),
                last4);
    }
}
