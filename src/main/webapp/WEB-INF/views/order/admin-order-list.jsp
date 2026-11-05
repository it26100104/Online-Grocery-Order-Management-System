<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order Fulfillment Management" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem;">
    <h1>Customer Order Fulfillment</h1>
    <p>Monitor warehouse order queues and advance order statuses through shipping and delivery.</p>
</div>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">All Customer Orders (${orders.size()})</h2>
    </div>

    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th style="text-align: right;">Update Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td>
                            <strong><c:out value="${o.orderId}" /></strong>
                            <div>
                                <a href="${pageContext.request.contextPath}/order-receipt?id=${o.orderId}" style="font-size: 0.8rem;">
                                    View Receipt
                                </a>
                            </div>
                        </td>
                        <td>
                            <code><c:out value="${o.userId}" /></code>
                            <div style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${o.address}" /></div>
                        </td>
                        <td><c:out value="${o.date}" /></td>
                        <td><span class="badge badge-info"><c:out value="${o.orderType}" /></span></td>
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
                            <c:choose>
                                <c:when test="${o.status eq 'CANCELLED' or o.status eq 'DELIVERED'}">
                                    <span class="badge badge-ghost" style="border: 1px solid var(--border-color);">Closed</span>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/orders" method="post" style="display: inline-flex; gap: 0.4rem; align-items: center;">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="orderId" value="<c:out value='${o.orderId}' />">

                                        <select name="status" class="form-control" style="width: 140px; padding: 0.35rem 0.5rem; font-size: 0.85rem;">
                                            <c:if test="${o.status eq 'PENDING'}">
                                                <option value="PROCESSING">PROCESSING</option>
                                                <option value="CANCELLED">CANCELLED</option>
                                            </c:if>
                                            <c:if test="${o.status eq 'PROCESSING'}">
                                                <option value="SHIPPED">SHIPPED</option>
                                                <option value="CANCELLED">CANCELLED</option>
                                            </c:if>
                                            <c:if test="${o.status eq 'SHIPPED'}">
                                                <option value="DELIVERED">DELIVERED</option>
                                            </c:if>
                                        </select>
                                        <button type="submit" class="btn btn-sm btn-primary">Update</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
