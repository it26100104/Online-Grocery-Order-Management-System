<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Payment History & Methods" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>Billing &amp; Payment History</h1>
    <p>Review your transaction receipts, manage saved payment cards, and submit refund requests.</p>
</div>

<div class="split-layout-grid">
    <!-- Transactions List -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Past Transactions (${transactions.size()})</h2>
        </div>

        <c:choose>
            <c:when test="${empty transactions}">
                <p style="text-align: center; padding: 2rem;">No payment transactions recorded.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Transaction</th>
                                <th>Order</th>
                                <th>Method</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th style="text-align: right;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${transactions}">
                                <tr>
                                    <td>
                                        <strong><c:out value="${t.paymentId}" /></strong>
                                        <div style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${t.date}" /></div>
                                    </td>
                                    <td>#<c:out value="${t.orderId}" /></td>
                                    <td><span class="badge badge-info"><c:out value="${t.method}" /></span></td>
                                    <td><strong>Rs. <fmt:formatNumber value="${t.amount}" pattern="#,##0.00" /></strong></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.status eq 'PAID'}">
                                                <span class="badge badge-success">PAID</span>
                                            </c:when>
                                            <c:when test="${t.status eq 'REFUND_REQUESTED'}">
                                                <span class="badge badge-warning">REFUND REQ</span>
                                            </c:when>
                                            <c:when test="${t.status eq 'REFUNDED'}">
                                                <span class="badge badge-info">REFUNDED</span>
                                            </c:when>
                                            <c:when test="${t.status eq 'VOID'}">
                                                <span class="badge badge-danger">VOID</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-warning">UNPAID</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: right; white-space: nowrap;">
                                        <a href="${pageContext.request.contextPath}/invoice?paymentId=${t.paymentId}" class="btn btn-sm btn-ghost">
                                            Invoice
                                        </a>

                                        <c:if test="${t.status eq 'PAID'}">
                                            <form action="${pageContext.request.contextPath}/payment-history" method="post" style="display: inline; margin-left: 0.3rem;" onsubmit="return confirm('Request a refund for #${t.paymentId}?');">
                                                <input type="hidden" name="action" value="requestRefund">
                                                <input type="hidden" name="paymentId" value="<c:out value='${t.paymentId}' />">
                                                <button type="submit" class="btn btn-sm btn-ghost text-warning">Refund</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Saved Methods -->
    <div class="card">
        <div class="card-header">
            <h2 class="card-title">Saved Payment Options</h2>
        </div>

        <c:choose>
            <c:when test="${empty savedMethods}">
                <p style="font-size: 0.9rem; color: var(--text-muted);">No payment methods saved yet. You can save your card or wallet during checkout.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="m" items="${savedMethods}">
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 0.75rem 0; border-bottom: 1px solid var(--border-color);">
                        <div>
                            <strong><c:out value="${m.label}" /></strong>
                            <div style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${m.type}" /> Ending in <c:out value="${m.last4}" /></div>
                        </div>
                        <form action="${pageContext.request.contextPath}/payment-history" method="post">
                            <input type="hidden" name="action" value="deleteMethod">
                            <input type="hidden" name="methodId" value="<c:out value='${m.methodId}' />">
                            <button type="submit" class="btn btn-sm btn-ghost text-danger">Remove</button>
                        </form>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
