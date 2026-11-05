<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Payment Ledger & Auditing" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem;">
    <h1>Payment &amp; Revenue Transactions</h1>
    <p>Audit store cashflows, review transaction authorizations, and approve customer refunds.</p>
</div>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">Transaction Ledger (${transactions.size()})</h2>
    </div>

    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>Payment ID</th>
                    <th>Order</th>
                    <th>Customer</th>
                    <th>Date</th>
                    <th>Method</th>
                    <th>Amount (LKR)</th>
                    <th>Status</th>
                    <th style="text-align: right;">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="t" items="${transactions}">
                    <tr>
                        <td><strong><c:out value="${t.paymentId}" /></strong></td>
                        <td>#<c:out value="${t.orderId}" /></td>
                        <td><code><c:out value="${t.userId}" /></code></td>
                        <td><c:out value="${t.date}" /></td>
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
                                View
                            </a>

                            <c:if test="${t.status eq 'REFUND_REQUESTED'}">
                                <form action="${pageContext.request.contextPath}/admin/payments" method="post" style="display: inline; margin-left: 0.3rem;" onsubmit="return confirm('Approve refund for #${t.paymentId}?');">
                                    <input type="hidden" name="action" value="approveRefund">
                                    <input type="hidden" name="paymentId" value="<c:out value='${t.paymentId}' />">
                                    <button type="submit" class="btn btn-sm btn-primary">Approve Refund</button>
                                </form>
                            </c:if>

                            <c:if test="${t.status eq 'UNPAID'}">
                                <form action="${pageContext.request.contextPath}/admin/payments" method="post" style="display: inline; margin-left: 0.3rem;" onsubmit="return confirm('Void transaction #${t.paymentId}?');">
                                    <input type="hidden" name="action" value="void">
                                    <input type="hidden" name="paymentId" value="<c:out value='${t.paymentId}' />">
                                    <button type="submit" class="btn btn-sm btn-danger">Void</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
