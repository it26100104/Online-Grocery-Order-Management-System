<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Track Deliveries" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>Live Delivery Tracking</h1>
    <p>Track the real-time fulfillment and courier dispatch status of your grocery shipments.</p>
</div>

<c:choose>
    <c:when test="${empty deliveries}">
        <div class="card text-center" style="padding: 3rem 1rem;">
            <h3>No shipments currently dispatched</h3>
            <p>Once you place an order, our dispatch team assigns a courier and tracks your shipment here.</p>
            <div style="margin-top: 1rem;">
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">View My Orders</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Dispatched Orders (${deliveries.size()})</h2>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Delivery ID</th>
                            <th>Order</th>
                            <th>Dispatch Type</th>
                            <th>Address</th>
                            <th>Courier Agent</th>
                            <th>Status</th>
                            <th>Delivery Fee</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${deliveries}">
                            <tr>
                                <td><strong><c:out value="${d.deliveryId}" /></strong></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order-receipt?id=${d.orderId}">
                                        #<c:out value="${d.orderId}" />
                                    </a>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${d.type eq 'EXPRESS'}">
                                            <span class="badge badge-warning">EXPRESS (4h)</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-info">STANDARD</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><c:out value="${d.address}" /></td>
                                <td><strong><c:out value="${d.agent}" /></strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${d.status eq 'DELIVERED'}">
                                            <span class="badge badge-success">DELIVERED</span>
                                        </c:when>
                                        <c:when test="${d.status eq 'OUT_FOR_DELIVERY'}">
                                            <span class="badge badge-warning">OUT FOR DELIVERY</span>
                                        </c:when>
                                        <c:when test="${d.status eq 'DISPATCHED'}">
                                            <span class="badge badge-info">DISPATCHED</span>
                                        </c:when>
                                        <c:when test="${d.status eq 'CANCELLED'}">
                                            <span class="badge badge-danger">CANCELLED</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-warning">SCHEDULED</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>Rs. <fmt:formatNumber value="${d.fee}" pattern="#,##0.00" /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
