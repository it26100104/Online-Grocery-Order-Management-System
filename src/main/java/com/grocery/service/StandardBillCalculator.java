package com.grocery.service;

public class StandardBillCalculator extends BillCalculator {

    private static final double TAX_RATE = 0.08; // 8%

    @Override
    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }
}
