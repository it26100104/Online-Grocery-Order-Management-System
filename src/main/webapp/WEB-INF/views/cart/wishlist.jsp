<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Wishlist" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>My Wishlist</h1>
    <p>Saved groceries you want to buy later. Move items directly to your cart whenever you are ready.</p>
</div>

<c:choose>
    <c:when test="${empty wishlist}">
        <div class="card text-center" style="padding: 3rem 1rem;">
            <h3>Your wishlist is empty</h3>
            <p>Save items you like while browsing the catalog.</p>
            <div style="margin-top: 1rem;">
                <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary">Browse Catalog</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Saved Items (${wishlist.size()})</h2>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Unit Price</th>
                            <th>Item Details</th>
                            <th style="text-align: right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${wishlist}">
                            <tr>
                                <td>
                                    <strong><c:out value="${item.productName}" /></strong>
                                    <div style="font-size: 0.8rem; color: var(--text-muted);">
                                        Product ID: <c:out value="${item.productId}" />
                                    </div>
                                </td>
                                <td>Rs. <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" /></td>
                                <td style="color: var(--text-muted); font-size: 0.9rem;">
                                    <!-- POLYMORPHISM: item.displayInfo -->
                                    <c:out value="${item.displayInfo()}" />
                                </td>
                                <td style="text-align: right; white-space: nowrap;">
                                    <form action="${pageContext.request.contextPath}/wishlist" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="moveToCart">
                                        <input type="hidden" name="wishId" value="<c:out value='${item.itemId}' />">
                                        <button type="submit" class="btn btn-sm btn-primary">Move to Cart</button>
                                    </form>

                                    <form action="${pageContext.request.contextPath}/wishlist" method="post" style="display: inline; margin-left: 0.4rem;">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="wishId" value="<c:out value='${item.itemId}' />">
                                        <button type="submit" class="btn btn-sm btn-ghost text-danger">Remove</button>
                                    </form>
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
