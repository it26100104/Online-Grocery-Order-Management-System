<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Payment Invoice #${payment.paymentId}" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
    <a href="${pageContext.request.contextPath}/orders" class="text-muted" style="font-size: 0.9rem;">
        &larr; Back to Order History
    </a>
    <button onclick="window.print();" class="btn btn-sm btn-ghost">Print Invoice</button>
</div>

<div class="card" style="padding: 2.5rem; max-width: 800px; margin: 0 auto;">
    <div style="display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 1px solid var(--border-color); padding-bottom: 1.5rem; margin-bottom: 2rem;">
        <div>
            <h1 style="font-size: 1.8rem; margin-bottom: 0.25rem;">Payment Receipt</h1>
            <p style="margin-bottom: 0;">GroceryHub e-Commerce Billing</p>
        </div>
        <div style="text-align: right;">
            <p style="margin-bottom: 0.25rem;"><strong>Payment ID:</strong> <c:out value="${payment.paymentId}" /></p>
            <p style="margin-bottom: 0.25rem;"><strong>Order ID:</strong> #<c:out value="${payment.orderId}" /></p>
            <p style="margin-bottom: 0.25rem;"><strong>Timestamp:</strong> <c:out value="${payment.date}" /></p>
            <span class="badge badge-success"><c:out value="${payment.status}" /></span>
        </div>
    </div>

    <div class="grid-2" style="margin-bottom: 2rem;">
        <div>
            <h4 style="margin-bottom: 0.4rem; color: var(--text-muted);">Billed To:</h4>
            <p style="margin-bottom: 0.2rem;"><strong><c:out value="${customer.fullName}" /></strong></p>
            <p style="margin-bottom: 0.2rem;"><c:out value="${customer.email}" /></p>
            <p style="margin-bottom: 0;"><c:out value="${customer.phone}" /></p>
        </div>
        <div>
            <h4 style="margin-bottom: 0.4rem; color: var(--text-muted);">Payment Channel:</h4>
            <p style="margin-bottom: 0.2rem;">Method: <strong><c:out value="${payment.method}" /></strong></p>
            <p style="margin-bottom: 0.2rem;">Status: <strong><c:out value="${payment.status}" /></strong></p>
            <p style="margin-bottom: 0;">Delivery Destination: <c:out value="${order.address}" /></p>
        </div>
    </div>

    <div class="table-responsive" style="margin-bottom: 2rem;">
        <table class="table">
            <thead>
                <tr>
                    <th>Item Description</th>
                    <th>Price</th>
                    <th>Qty</th>
                    <th style="text-align: right;">Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${order.items}">
                    <tr>
                        <td><strong><c:out value="${item.name}" /></strong></td>
                        <td>Rs. <fmt:formatNumber value="${item.price}" pattern="#,##0.00" /></td>
                        <td><c:out value="${item.quantity}" /></td>
                        <td style="text-align: right;">Rs. <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.00" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div style="max-width: 320px; margin-left: auto;">
        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
            <span class="text-muted">Subtotal:</span>
            <span>Rs. <fmt:formatNumber value="${order.subtotal}" pattern="#,##0.00" /></span>
        </div>
        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
            <span class="text-muted">Tax (8%):</span>
            <span>Rs. <fmt:formatNumber value="${order.tax}" pattern="#,##0.00" /></span>
        </div>
        <hr style="border: 0; border-top: 1px solid var(--border-color); margin: 0.75rem 0;">
        <div style="display: flex; justify-content: space-between; font-size: 1.3rem; font-weight: 700;">
            <span>Amount Paid:</span>
            <span class="text-accent">Rs. <fmt:formatNumber value="${payment.amount}" pattern="#,##0.00" /></span>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
