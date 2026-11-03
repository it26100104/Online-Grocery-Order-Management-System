package com.grocery.model;

/**
 * Logistics Report measuring delivery fulfillment ratios and active dispatches.
 */
// INHERITANCE: Inherits from Report
public class DeliveryReport extends Report {

    private int totalDeliveries;
    private int deliveredCount;
    private int inTransitCount;
    private int cancelledCount;

    public DeliveryReport(int totalDeliveries, int deliveredCount, int inTransitCount, int cancelledCount) {
        super("Logistics & Dispatch Operations Report");
        this.totalDeliveries = totalDeliveries;
        this.deliveredCount = deliveredCount;
        this.inTransitCount = inTransitCount;
        this.cancelledCount = cancelledCount;
    }

    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public int getDeliveredCount() {
        return deliveredCount;
    }

    public int getInTransitCount() {
        return inTransitCount;
    }

    public int getCancelledCount() {
        return cancelledCount;
    }

    // POLYMORPHISM: Formats operations summary
    @Override
    public String generateReport() {
        double successRate = totalDeliveries > 0 ? ((double) deliveredCount / totalDeliveries) * 100.0 : 0.0;
        return String.format("=== %s ===\n" +
                        "Generated: %s\n" +
                        "Total Shipments Handled: %d\n" +
                        "Successfully Delivered: %d (%.1f%%)\n" +
                        "Currently Active / In Transit: %d\n" +
                        "Cancelled Shipments: %d\n",
                getReportTitle(), getGeneratedAt(), totalDeliveries, deliveredCount, successRate, inTransitCount, cancelledCount);
    }
}
