<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Sales Analytics & Business Intelligence" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
    <div>
        <h1>Store Sales Analytics &amp; Performance</h1>
        <p>Real-time revenue metrics, inventory demand ranking, and logistics success rates.</p>
    </div>
    <div>
        <a href="${pageContext.request.contextPath}/admin/activity-logs" class="btn btn-ghost">
            View System Audit Logs &rarr;
        </a>
    </div>
</div>

<!-- KPI Summary Cards -->
<div class="grid-4" style="margin-bottom: 2rem;">
    <div class="card">
        <p style="margin-bottom: 0.35rem; font-size: 0.85rem; text-transform: uppercase;">Gross Settled Revenue</p>
        <h2 style="font-size: 1.85rem; color: var(--color-accent); margin-bottom: 0;">
            Rs. <fmt:formatNumber value="${salesReport.totalRevenue}" pattern="#,##0.00" />
        </h2>
    </div>

    <div class="card">
        <p style="margin-bottom: 0.35rem; font-size: 0.85rem; text-transform: uppercase;">Paid Transactions</p>
        <h2 style="font-size: 1.85rem; margin-bottom: 0;">
            <c:out value="${salesReport.completedOrdersCount}" />
        </h2>
    </div>

    <div class="card">
        <p style="margin-bottom: 0.35rem; font-size: 0.85rem; text-transform: uppercase;">Total Shipments</p>
        <h2 style="font-size: 1.85rem; margin-bottom: 0;">
            <c:out value="${deliveryReport.totalDeliveries}" />
        </h2>
    </div>

    <div class="card">
        <p style="margin-bottom: 0.35rem; font-size: 0.85rem; text-transform: uppercase;">Completed Deliveries</p>
        <h2 style="font-size: 1.85rem; color: var(--color-accent); margin-bottom: 0;">
            <c:out value="${deliveryReport.deliveredCount}" />
        </h2>
    </div>
</div>

<div class="grid-2">
    <!-- Top Selling Items Card -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Top Selling Grocery Items</h2>
        </div>

        <c:choose>
            <c:when test="${empty salesReport.topSellingItems}">
                <p style="text-align: center; padding: 2rem;">No item sales recorded yet.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Product Name</th>
                                <th style="text-align: right;">Units Sold</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="entry" items="${salesReport.topSellingItems}">
                                <tr>
                                    <td><strong><c:out value="${entry.key}" /></strong></td>
                                    <td style="text-align: right;">
                                        <span class="badge badge-success"><c:out value="${entry.value}" /> Units</span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Generated Text Intelligence Summary (Polymorphism) -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Polymorphic Report Exports</h2>
        </div>

        <h4 style="margin-bottom: 0.4rem; color: var(--text-muted);">Executive Sales Report</h4>
        <pre style="background: var(--bg-page); border: 1px solid var(--border-color); padding: 1rem; border-radius: 8px; color: var(--text-main); font-family: monospace; font-size: 0.85rem; margin-bottom: 1.25rem; white-space: pre-wrap;"><c:out value="${salesReport.generateReport()}" /></pre>

        <h4 style="margin-bottom: 0.4rem; color: var(--text-muted);">Logistics Operations Report</h4>
        <pre style="background: var(--bg-page); border: 1px solid var(--border-color); padding: 1rem; border-radius: 8px; color: var(--text-main); font-family: monospace; font-size: 0.85rem; margin-bottom: 0; white-space: pre-wrap;"><c:out value="${deliveryReport.generateReport()}" /></pre>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
