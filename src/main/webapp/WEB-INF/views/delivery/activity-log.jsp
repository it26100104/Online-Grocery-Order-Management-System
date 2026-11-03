<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="System Activity & Audit Logs" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
    <div>
        <h1>System Audit &amp; Activity Log</h1>
        <p>Complete tamper-evident log of user registrations, logins, orders, and administrative actions.</p>
    </div>
    <form action="${pageContext.request.contextPath}/admin/activity-logs" method="post" onsubmit="return confirm('Purge all historical audit logs?');">
        <input type="hidden" name="action" value="clear">
        <button type="submit" class="btn btn-danger">Clear All Logs</button>
    </form>
</div>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">Audit Trail Records (${logs.size()})</h2>
    </div>

    <c:choose>
        <c:when test="${empty logs}">
            <p style="text-align: center; padding: 2rem;">No activity recorded yet.</p>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Log ID</th>
                            <th>Timestamp</th>
                            <th>User ID</th>
                            <th>Action</th>
                            <th>Details</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${logs}">
                            <tr>
                                <td><strong><c:out value="${entry.logId}" /></strong></td>
                                <td style="white-space: nowrap;"><c:out value="${entry.timestamp}" /></td>
                                <td><code><c:out value="${entry.userId}" /></code></td>
                                <td>
                                    <span class="badge badge-info"><c:out value="${entry.action}" /></span>
                                </td>
                                <td><c:out value="${entry.details}" /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
