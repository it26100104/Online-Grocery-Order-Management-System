<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="User Management" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: gap; gap: 1rem;">
    <div>
        <h1>User Account Administration</h1>
        <p>Review customer and staff accounts, assign administrator roles, and control access states.</p>
    </div>
</div>

<!-- Add Administrator Card -->
<div class="card">
    <div class="card-header">
        <h2 class="card-title">Provision New Administrator</h2>
    </div>
    <form action="${pageContext.request.contextPath}/admin/users" method="post" class="form-row">
        <input type="hidden" name="action" value="createAdmin">

        <div class="form-group" style="min-width: 180px;">
            <label class="form-label">Full Name *</label>
            <input type="text" name="fullName" class="form-control" placeholder="Admin Name" required>
        </div>

        <div class="form-group" style="min-width: 200px;">
            <label class="form-label">Email Address *</label>
            <input type="email" name="email" class="form-control" placeholder="staff@grocery.com" required>
        </div>

        <div class="form-group" style="min-width: 160px;">
            <label class="form-label">Phone *</label>
            <input type="tel" name="phone" class="form-control" placeholder="+94 77 000 0000" required>
        </div>

        <div class="form-group" style="min-width: 160px;">
            <label class="form-label">Password *</label>
            <input type="password" name="password" class="form-control" placeholder="Min 6 chars" required minlength="6">
        </div>

        <input type="hidden" name="address" value="Store HQ Operations">

        <div class="form-group" style="align-self: flex-end; min-width: 140px;">
            <button type="submit" class="btn btn-primary" style="width: 100%;">Create Admin</button>
        </div>
    </form>
</div>

<!-- Users Table Card -->
<div class="card">
    <div class="card-header">
        <h2 class="card-title">Registered Accounts (${users.size()})</h2>
    </div>

    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>User ID</th>
                    <th>Role</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th style="text-align: right;">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td><strong><c:out value="${u.userId}" /></strong></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.role eq 'ADMIN'}">
                                    <span class="badge badge-success">ADMIN</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-info">CUSTOMER</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${u.fullName}" /></td>
                        <td><c:out value="${u.email}" /></td>
                        <td><c:out value="${u.phone}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.active}">
                                    <span class="badge badge-success">ACTIVE</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger">INACTIVE</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="text-align: right;">
                            <c:choose>
                                <c:when test="${u.userId eq sessionScope.userId}">
                                    <span class="badge badge-info">You</span>
                                </c:when>
                                <c:when test="${u.active}">
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline;" onsubmit="return confirm('Deactivate account for <c:out value="${u.fullName}" />?');">
                                        <input type="hidden" name="action" value="deactivate">
                                        <input type="hidden" name="userId" value="<c:out value='${u.userId}' />">
                                        <button type="submit" class="btn btn-sm btn-ghost" style="color: #fca5a5;">Deactivate</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline; margin-left: 0.35rem;" onsubmit="return confirm('PERMANENTLY DELETE user <c:out value="${u.fullName}" />? This cannot be undone.');">
                                        <input type="hidden" name="action" value="deletePermanent">
                                        <input type="hidden" name="userId" value="<c:out value='${u.userId}' />">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="reactivate">
                                        <input type="hidden" name="userId" value="<c:out value='${u.userId}' />">
                                        <button type="submit" class="btn btn-sm btn-ghost">Reactivate</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display: inline; margin-left: 0.35rem;" onsubmit="return confirm('PERMANENTLY DELETE user <c:out value="${u.fullName}" />? This cannot be undone.');">
                                        <input type="hidden" name="action" value="deletePermanent">
                                        <input type="hidden" name="userId" value="<c:out value='${u.userId}' />">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
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
