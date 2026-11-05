package com.grocery.model;

import java.util.Map;

/**
 * Sales Analytics Report calculating Total Revenue and Top Selling items.
 */

public class SalesReport extends Report {

    private double totalRevenue;
    private int completedOrdersCount;
    private Map<String, Integer> topSellingItems;

    public SalesReport(double totalRevenue, int completedOrdersCount, Map<String, Integer> topSellingItems) {
        super("Sales & Revenue Executive Summary");
        this.totalRevenue = Math.round(totalRevenue * 100.0) / 100.0;
        this.completedOrdersCount = completedOrdersCount;
        this.topSellingItems = topSellingItems;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getCompletedOrdersCount() {
        return completedOrdersCount;
    }

    public Map<String, Integer> getTopSellingItems() {
        return topSellingItems;
    }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== %s ===\n", getReportTitle()));
        sb.append(String.format("Generated: %s\n", getGeneratedAt()));
        sb.append(String.format("Gross Settled Revenue: Rs. %.2f\n", totalRevenue));
        sb.append(String.format("Completed Paid Transactions: %d\n", completedOrdersCount));
        sb.append("Top Demand Items:\n");
        if (topSellingItems != null && !topSellingItems.isEmpty()) {
            topSellingItems.forEach((item, qty) ->
                    sb.append(String.format(" - %s: %d units sold\n", item, qty)));
        } else {
            sb.append(" (No sales recorded yet)\n");
        }
        return sb.toString();
    }
}
