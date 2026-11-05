<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Orders" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>My Order History</h1>
    <p>Track your current shipments, review past orders, and view official receipts.</p>
</div>

<c:choose>
    <c:when test="${empty orders}">
        <div class="card text-center" style="padding: 3rem 1rem;">
            <h3>No orders placed yet</h3>
            <p>Start shopping from our fresh catalog to place your first order.</p>
            <div style="margin-top: 1rem;">
                <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary">Browse Catalog</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Placed Orders (${orders.size()})</h2>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date</th>
                            <th>Type</th>
                            <th>Items</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td><strong><c:out value="${o.orderId}" /></strong></td>
                                <td><c:out value="${o.date}" /></td>
                                <td><span class="badge badge-info"><c:out value="${o.orderType}" /></span></td>
                                <td>
                                    <c:forEach var="item" items="${o.items}" varStatus="s">
                                        <c:out value="${item.quantity}" />x <c:out value="${item.name}" />${!s.last ? ', ' : ''}
                                    </c:forEach>
                                </td>
                                <td><strong>Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00" /></strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${o.status eq 'DELIVERED'}">
                                            <span class="badge badge-success">DELIVERED</span>
                                        </c:when>
                                        <c:when test="${o.status eq 'SHIPPED'}">
                                            <span class="badge badge-info">SHIPPED</span>
                                        </c:when>
                                        <c:when test="${o.status eq 'PROCESSING'}">
                                            <span class="badge badge-warning">PROCESSING</span>
                                        </c:when>
                                        <c:when test="${o.status eq 'CANCELLED'}">
                                            <span class="badge badge-danger">CANCELLED</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-warning">PENDING</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <a href="${pageContext.request.contextPath}/order-receipt?id=${o.orderId}" class="btn btn-sm btn-ghost">
                                        Receipt
                                    </a>

                                    <c:if test="${o.pending}">
                                        <form action="${pageContext.request.contextPath}/orders" method="post" style="display: inline; margin-left: 0.4rem;" onsubmit="return confirm('Cancel Order #${o.orderId}?');">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="orderId" value="<c:out value='${o.orderId}' />">
                                            <button type="submit" class="btn btn-sm btn-danger">Cancel</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
