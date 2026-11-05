package com.grocery.model;

import java.util.List;

/**
 * BulkOrder represents large-quantity or commercial orders eligible for a 10% wholesale discount.
 */
// INHERITANCE: Inherits from abstract Order class
public class BulkOrder extends Order {

    public BulkOrder() {
        super();
        setOrderType("BULK");
    }

    public BulkOrder(String orderId, String userId, List<OrderItem> items,
                     double subtotal, double tax, double total, String address, String status, String date) {
        super(orderId, userId, "BULK", items, subtotal, tax, total, address, status, date);
    }

    // POLYMORPHISM: Applies 10% wholesale volume discount to subtotal prior to tax
    @Override
    public double calculateTotal() {
        double discountedSubtotal = getSubtotal() * 0.90;
        double calculatedTax = discountedSubtotal * 0.08;
        return Math.round((discountedSubtotal + calculatedTax) * 100.0) / 100.0;
    }
}
