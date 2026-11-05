<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Receipt #${order.orderId}" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
    <a href="${pageContext.request.contextPath}/orders" class="text-muted" style="font-size: 0.9rem;">
        &larr; Back to Order History
    </a>
    <button onclick="window.print();" class="btn btn-sm btn-ghost">Print Receipt</button>
</div>

<div class="card" style="padding: 2.5rem; max-width: 800px; margin: 0 auto;">
    <div style="display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 1px solid var(--border-color); padding-bottom: 1.5rem; margin-bottom: 2rem;">
        <div>
            <h1 style="font-size: 1.8rem; margin-bottom: 0.25rem;">Grocery<span style="color: var(--color-accent);">Hub</span> Receipt</h1>
            <p style="margin-bottom: 0;">Official Order Invoice</p>
        </div>
        <div style="text-align: right;">
            <p style="margin-bottom: 0.25rem;"><strong>Order ID:</strong> #<c:out value="${order.orderId}" /></p>
            <p style="margin-bottom: 0.25rem;"><strong>Date:</strong> <c:out value="${order.date}" /></p>
            <div>
                <c:choose>
                    <c:when test="${order.status eq 'DELIVERED'}">
                        <span class="badge badge-success">DELIVERED</span>
                    </c:when>
                    <c:when test="${order.status eq 'CANCELLED'}">
                        <span class="badge badge-danger">CANCELLED</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-warning"><c:out value="${order.status}" /></span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Customer & Destination Details -->
    <div class="grid-2" style="margin-bottom: 2rem;">
        <div>
            <h4 style="margin-bottom: 0.5rem; color: var(--text-muted);">Customer Information</h4>
            <p style="margin-bottom: 0.2rem;"><strong><c:out value="${customer.fullName}" /></strong></p>
            <p style="margin-bottom: 0.2rem;"><c:out value="${customer.email}" /></p>
            <p style="margin-bottom: 0;"><c:out value="${customer.phone}" /></p>
        </div>
        <div>
            <h4 style="margin-bottom: 0.5rem; color: var(--text-muted);">Delivery Destination</h4>
            <p style="margin-bottom: 0.2rem;"><c:out value="${order.address}" /></p>
            <p style="margin-bottom: 0; font-size: 0.85rem; color: var(--text-muted);">
                Order Plan: <strong><c:out value="${order.orderType}" /></strong>
            </p>
        </div>
    </div>

    <!-- Purchased Line Items -->
    <div class="table-responsive" style="margin-bottom: 2rem;">
        <table class="table">
            <thead>
                <tr>
                    <th>Item Description</th>
                    <th>Unit Price</th>
                    <th>Qty</th>
                    <th style="text-align: right;">Amount</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${order.items}">
                    <tr>
                        <td>
                            <strong><c:out value="${item.name}" /></strong>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">Product ID: <c:out value="${item.productId}" /></div>
                        </td>
                        <td>Rs. <fmt:formatNumber value="${item.price}" pattern="#,##0.00" /></td>
                        <td><c:out value="${item.quantity}" /></td>
                        <td style="text-align: right;">
                            <strong>Rs. <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.00" /></strong>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Financial Calculation Summary -->
    <div style="max-width: 320px; margin-left: auto;">
        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
            <span class="text-muted">Subtotal:</span>
            <span>Rs. <fmt:formatNumber value="${order.subtotal}" pattern="#,##0.00" /></span>
        </div>

        <c:if test="${order.orderType eq 'BULK'}">
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; color: var(--color-accent);">
                <span>Bulk Discount (10%):</span>
                <span>- Rs. <fmt:formatNumber value="${order.subtotal * 0.10}" pattern="#,##0.00" /></span>
            </div>
        </c:if>

        <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
            <span class="text-muted">Tax (8%):</span>
            <span>Rs. <fmt:formatNumber value="${order.tax}" pattern="#,##0.00" /></span>
        </div>

        <hr style="border: 0; border-top: 1px solid var(--border-color); margin: 0.75rem 0;">

        <div style="display: flex; justify-content: space-between; font-size: 1.3rem; font-weight: 700; margin-bottom: 1.5rem;">
            <span>Grand Total:</span>
            <span class="text-accent">Rs. <fmt:formatNumber value="${order.total}" pattern="#,##0.00" /></span>
        </div>

        <a href="${pageContext.request.contextPath}/payment?orderId=${order.orderId}" class="btn btn-primary" style="width: 100%;">
            Proceed to Payment &rarr;
        </a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
