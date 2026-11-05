package com.grocery.model;

import java.util.List;

/**
 * RegularOrder represents standard consumer grocery orders.
 */
// INHERITANCE: Inherits from abstract Order class
public class RegularOrder extends Order {

    public RegularOrder() {
        super();
        setOrderType("REGULAR");
    }

    public RegularOrder(String orderId, String userId, List<OrderItem> items,
                        double subtotal, double tax, double total, String address, String status, String date) {
        super(orderId, userId, "REGULAR", items, subtotal, tax, total, address, status, date);
    }

    // POLYMORPHISM: Standard total is subtotal + tax (no wholesale discount)
    @Override
    public double calculateTotal() {
        return Math.round((getSubtotal() + getTax()) * 100.0) / 100.0;
    }
}
