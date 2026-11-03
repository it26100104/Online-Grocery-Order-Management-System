package com.grocery.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Report template for business intelligence and operations audit.
 */
// ABSTRACTION: Common contract for analytics reporting
public abstract class Report {

    private String reportTitle;
    private String generatedAt;

    public Report(String reportTitle) {
        this.reportTitle = reportTitle;
        this.generatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    // POLYMORPHISM: Abstract report generation contract
    public abstract String generateReport();
}
