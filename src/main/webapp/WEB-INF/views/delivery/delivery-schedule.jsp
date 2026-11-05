<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Logistics Dispatch Schedule" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem;">
    <h1>Logistics &amp; Courier Dispatch Schedule</h1>
    <p>Assign courier agents to customer orders, manage delivery routes, and confirm order handoffs.</p>
</div>

<!-- Schedule Manual Dispatch Form -->
<div class="card">
    <div class="card-header">
        <h2 class="card-title">Schedule Courier Dispatch</h2>
    </div>
    <form action="${pageContext.request.contextPath}/admin/deliveries" method="post" class="form-row">
        <input type="hidden" name="action" value="schedule">

        <div class="form-group" style="min-width: 140px;">
            <label class="form-label">Order ID *</label>
            <input type="text" name="orderId" class="form-control" placeholder="e.g. O001" required>
        </div>

        <div class="form-group" style="min-width: 140px;">
            <label class="form-label">Customer ID *</label>
            <input type="text" name="userId" class="form-control" placeholder="e.g. U002" required>
        </div>

        <div class="form-group" style="min-width: 160px;">
            <label class="form-label">Shipping Type *</label>
            <select name="type" class="form-control">
                <option value="STANDARD">STANDARD (Rs. 250)</option>
                <option value="EXPRESS">EXPRESS (Rs. 500)</option>
            </select>
        </div>

        <div class="form-group" style="flex: 2; min-width: 220px;">
            <label class="form-label">Destination Address *</label>
            <input type="text" name="address" class="form-control" placeholder="Street, City" required>
        </div>

        <div class="form-group" style="align-self: flex-end; min-width: 130px;">
            <button type="submit" class="btn btn-primary" style="width: 100%;">Schedule</button>
        </div>
    </form>
</div>

<!-- Deliveries Table -->
<div class="card">
    <div class="card-header">
        <h2 class="card-title">All Active &amp; Past Dispatches (${deliveries.size()})</h2>
    </div>

    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>Delivery ID</th>
                    <th>Order</th>
                    <th>Type</th>
                    <th>Destination</th>
                    <th>Agent</th>
                    <th>Status</th>
                    <th style="text-align: right;">Manage Courier</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="d" items="${deliveries}">
                    <tr>
                        <td><strong><c:out value="${d.deliveryId}" /></strong></td>
                        <td>#<c:out value="${d.orderId}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${d.type eq 'EXPRESS'}">
                                    <span class="badge badge-warning">EXPRESS</span>
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
                        <td style="text-align: right; white-space: nowrap;">
                            <c:if test="${d.status ne 'DELIVERED' and d.status ne 'CANCELLED'}">
                                <!-- Assign Agent Form -->
                                <form action="${pageContext.request.contextPath}/admin/deliveries" method="post" style="display: inline-flex; gap: 0.3rem;">
                                    <input type="hidden" name="action" value="assignAgent">
                                    <input type="hidden" name="deliveryId" value="<c:out value='${d.deliveryId}' />">
                                    <input type="text" name="agent" placeholder="Agent Name" class="form-control" style="width: 110px; padding: 0.25rem 0.5rem; font-size: 0.8rem;" required>
                                    <button type="submit" class="btn btn-sm btn-ghost">Assign</button>
                                </form>

                                <!-- Status Advance Form -->
                                <form action="${pageContext.request.contextPath}/admin/deliveries" method="post" style="display: inline-flex; gap: 0.3rem; margin-left: 0.4rem;">
                                    <input type="hidden" name="action" value="updateStatus">
                                    <input type="hidden" name="deliveryId" value="<c:out value='${d.deliveryId}' />">
                                    <select name="status" class="form-control" style="width: 140px; padding: 0.25rem 0.5rem; font-size: 0.8rem;">
                                        <option value="DISPATCHED">DISPATCHED</option>
                                        <option value="OUT_FOR_DELIVERY">OUT FOR DELIVERY</option>
                                        <option value="DELIVERED">DELIVERED</option>
                                        <option value="CANCELLED">CANCELLED</option>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-primary">Update</button>
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
