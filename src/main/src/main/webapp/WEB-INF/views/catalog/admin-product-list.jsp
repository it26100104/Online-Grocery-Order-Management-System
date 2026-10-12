<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Product Inventory Management" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
    <div>
        <h1>Grocery Catalog Management</h1>
        <p>Monitor warehouse inventory, edit pricing, and update stock counts.</p>
    </div>
    <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary">
        + Add New Product
    </a>
</div>

<div class="card">
    <div class="card-header">
        <h2 class="card-title">Inventory Items (${products.size()})</h2>
    </div>

    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price (LKR)</th>
                    <th>Stock</th>
                    <th>Type</th>
                    <th>Details</th>
                    <th style="text-align: right;">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td><strong><c:out value="${p.productId}" /></strong></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/product-details?id=${p.productId}">
                                <c:out value="${p.name}" />
                            </a>
                        </td>
                        <td><span class="badge badge-info"><c:out value="${p.category}" /></span></td>
                        <td>Rs. <fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${p.stock > 10}">
                                    <span class="badge badge-success"><c:out value="${p.stock}" /> in stock</span>
                                </c:when>
                                <c:when test="${p.stock > 0}">
                                    <span class="badge badge-warning">Low: <c:out value="${p.stock}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger">Out of Stock</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${p.type}" /></td>
                        <td style="font-size: 0.85rem; color: var(--text-muted);">
                            <c:out value="${p.displayInfo()}" />
                        </td>
                        <td style="text-align: right; white-space: nowrap;">
                            <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.productId}" class="btn btn-sm btn-ghost">
                                Edit
                            </a>
                            <form action="${pageContext.request.contextPath}/admin/products" method="post" style="display: inline;" onsubmit="return confirm('Permanently remove <c:out value="${p.name}" />?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="productId" value="<c:out value='${p.productId}' />">
                                <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
