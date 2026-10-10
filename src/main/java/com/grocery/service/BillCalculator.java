package com.grocery.service;

import com.grocery.model.CartItem;

import java.util.List;

public abstract class BillCalculator {

    // anith classes walata common: subtotal eka
    public double calculateSubtotal(List<CartItem> items) {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getLineTotal();
        }
        return subtotal;
    }

    // abstract: tax eka calculate karana widiya subclass ekata
    public abstract double calculateTax(double subtotal);

    // total = subtotal + tax
    public double calculateTotal(List<CartItem> items) {
        double subtotal = calculateSubtotal(items);
        return subtotal + calculateTax(subtotal);
    }
}
