package com.grocery.dao;

import com.grocery.model.CardPayment;
import com.grocery.model.CashOnDeliveryPayment;
import com.grocery.model.Payment;
import com.grocery.model.PaymentMethod;
import com.grocery.model.WalletPayment;
import com.grocery.util.ActivityLogger;
import com.grocery.util.FileHandler;
import com.grocery.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payments and Saved Methods.
 * Interacts strictly with payments.txt using FileHandler.
 * TXN lines: TXN|paymentId|orderId|userId|method|amount|status|date
 * METHOD lines: METHOD|methodId|userId|type|label|last4
 */
// ENCAPSULATION: Encapsulates transactions, refunds, and masked payment method persistence
public class PaymentDAO {

    private static final String FILE_NAME = "payments.txt";

    // ==========================================
    // PARSING LOGIC
    // ==========================================

    private Payment parseTransaction(String line) {
        if (line == null || !line.startsWith("TXN|")) {
            return null;
        }
        String[] tokens = line.split("\\|");
        if (tokens.length < 8) return null;

        try {
            String paymentId = tokens[1].trim();
            String orderId = tokens[2].trim();
            String userId = tokens[3].trim();
            String method = tokens[4].trim();
            double amount = Double.parseDouble(tokens[5].trim());
            String status = tokens[6].trim();
            String date = tokens[7].trim();

            // POLYMORPHISM: Factory creation of Payment subclasses
            return switch (method.toUpperCase()) {
                case "CARD" -> new CardPayment(paymentId, orderId, userId, amount, status, date);
                case "WALLET" -> new WalletPayment(paymentId, orderId, userId, amount, status, date);
                default -> new CashOnDeliveryPayment(paymentId, orderId, userId, amount, status, date);
            };
        } catch (Exception e) {
            return null;
        }
    }

    private PaymentMethod parseMethod(String line) {
        if (line == null || !line.startsWith("METHOD|")) {
            return null;
        }
        String[] tokens = line.split("\\|");
        if (tokens.length < 6) return null;

        String methodId = tokens[1].trim();
        String userId = tokens[2].trim();
        String type = tokens[3].trim();
        String label = tokens[4].trim();
        String last4 = tokens[5].trim();
        return new PaymentMethod(methodId, userId, type, label, last4);
    }

    // ==========================================
    // TRANSACTION OPERATIONS
    // ==========================================

    public synchronized List<Payment> getAllTransactions() {
        List<String> lines = FileHandler.readAll(FILE_NAME);
        List<Payment> list = new ArrayList<>();
        for (String line : lines) {
            Payment p = parseTransaction(line);
            if (p != null) {
                list.add(p);
            }
        }
        return list;
    }

    public synchronized List<Payment> getTransactionsByUser(String userId) {
        List<Payment> list = new ArrayList<>();
        if (userId == null) return list;
        for (Payment p : getAllTransactions()) {
            if (p.getUserId().equalsIgnoreCase(userId.trim())) {
                list.add(p);
            }
        }
        return list;
    }

    public synchronized Payment getTransactionById(String paymentId) {
        if (paymentId == null) return null;
        for (Payment p : getAllTransactions()) {
            if (p.getPaymentId().equalsIgnoreCase(paymentId.trim())) {
                return p;
            }
        }
        return null;
    }

    public synchronized Payment getTransactionByOrderId(String orderId) {
        if (orderId == null) return null;
        for (Payment p : getAllTransactions()) {
            if (p.getOrderId().equalsIgnoreCase(orderId.trim())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Retrieves all PAID transactions for sales analytics and revenue computation.
     */
    public synchronized List<Payment> getPaidRecords() {
        List<Payment> list = new ArrayList<>();
        for (Payment p : getAllTransactions()) {
            if (p.isPaid()) {
                list.add(p);
            }
        }
        return list;
    }

    /**
     * Records a new payment transaction.
     */
    public synchronized boolean recordPayment(Payment payment) {
        if (payment == null) return false;

        String nextId = IdGenerator.getNextId("PAY");
        payment.setPaymentId(nextId);

        FileHandler.append(FILE_NAME, payment.toDataLine());
        ActivityLogger.log(payment.getUserId(), "PAYMENT_RECORDED",
                "Payment " + nextId + " for Order #" + payment.getOrderId() + " (" + payment.getMethod() + ", Rs. " + payment.getAmount() + ")");
        return true;
    }

    /**
     * Updates status (UNPAID -> PAID, REFUND_REQUESTED -> REFUNDED, or VOID).
     */
    public synchronized boolean updateStatus(String paymentId, String newStatus) {
        List<String> allLines = FileHandler.readAll(FILE_NAME);
        boolean found = false;
        List<String> updated = new ArrayList<>();

        for (String line : allLines) {
            if (line.startsWith("TXN|")) {
                Payment p = parseTransaction(line);
                if (p != null && p.getPaymentId().equalsIgnoreCase(paymentId)) {
                    p.setStatus(newStatus.trim().toUpperCase());
                    updated.add(p.toDataLine());
                    found = true;
                    continue;
                }
            }
            updated.add(line);
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, updated);
            ActivityLogger.log("SYSTEM", "PAYMENT_STATUS_UPDATE", "Payment " + paymentId + " marked as " + newStatus);
        }
        return found;
    }

    /**
     * Voids or refunds a payment associated with an order.
     */
    public synchronized boolean voidOrRefundByOrder(String orderId) {
        Payment p = getTransactionByOrderId(orderId);
        if (p == null) return false;
        return updateStatus(p.getPaymentId(), "VOID");
    }

    // ==========================================
    // SAVED PAYMENT METHODS OPERATIONS
    // ==========================================

    public synchronized List<PaymentMethod> getMethodsByUser(String userId) {
        List<String> lines = FileHandler.readAll(FILE_NAME);
        List<PaymentMethod> list = new ArrayList<>();
        for (String line : lines) {
            PaymentMethod m = parseMethod(line);
            if (m != null && userId != null && m.getUserId().equalsIgnoreCase(userId.trim())) {
                list.add(m);
            }
        }
        return list;
    }

    public synchronized boolean saveMethod(String userId, String type, String label, String last4) {
        if (userId == null || type == null || last4 == null) return false;
        String methodId = IdGenerator.getNextId("M");
        PaymentMethod pm = new PaymentMethod(methodId, userId, type, label, last4);
        FileHandler.append(FILE_NAME, pm.toDataLine());
        ActivityLogger.log(userId, "SAVE_PAYMENT_METHOD", "Saved payment method: " + label);
        return true;
    }

    public synchronized boolean deleteMethod(String methodId) {
        if (methodId == null) return false;
        List<String> allLines = FileHandler.readAll(FILE_NAME);
        boolean found = false;
        List<String> remaining = new ArrayList<>();

        for (String line : allLines) {
            if (line.startsWith("METHOD|")) {
                PaymentMethod pm = parseMethod(line);
                if (pm != null && pm.getMethodId().equalsIgnoreCase(methodId.trim())) {
                    found = true;
                    continue; // Remove saved payment method
                }
            }
            remaining.add(line);
        }

        if (found) {
            FileHandler.writeAll(FILE_NAME, remaining);
        }
        return found;
    }
}
