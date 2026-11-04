<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Shopping Cart" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>Shopping Cart</h1>
    <p>Review and modify selected grocery items before proceeding to checkout.</p>
</div>

<c:choose>
    <c:when test="${empty cartItems}">
        <div class="card text-center" style="padding: 3rem 1rem;">
            <h3>Your cart is empty</h3>
            <p>Looks like you haven't added any groceries to your cart yet.</p>
            <div style="margin-top: 1rem;">
                <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary">Browse Catalog</a>
                <a href="${pageContext.request.contextPath}/wishlist" class="btn btn-ghost" style="margin-left: 0.5rem;">View Wishlist</a>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="split-layout-grid">
            <!-- Cart Items Table Card -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Cart Items (${cartItems.size()})</h2>
                    <form action="${pageContext.request.contextPath}/cart" method="post" onsubmit="return confirm('Clear your entire cart?');">
                        <input type="hidden" name="action" value="clear">
                        <button type="submit" class="btn btn-sm btn-ghost text-danger">Clear All</button>
                    </form>
                </div>

                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th style="width: 140px;">Quantity</th>
                                <th>Total</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>
                                        <strong><c:out value="${item.productName}" /></strong>
                                        <div style="font-size: 0.8rem; color: var(--text-muted);">
                                            Item Code: <c:out value="${item.productId}" />
                                        </div>
                                    </td>
                                    <td>Rs. <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" /></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart" method="post" style="display: flex; gap: 0.4rem; align-items: center;">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="cartItemId" value="<c:out value='${item.itemId}' />">
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="99" class="form-control" style="width: 65px; padding: 0.35rem 0.5rem;">
                                            <button type="submit" class="btn btn-sm btn-ghost">Update</button>
                                        </form>
                                    </td>
                                    <td>
                                        <!-- POLYMORPHISM: item.lineTotal -->
                                        <strong>Rs. <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.00" /></strong>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart" method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="cartItemId" value="<c:out value='${item.itemId}' />">
                                            <button type="submit" class="btn btn-sm btn-ghost text-danger">&times;</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div style="margin-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/catalog" class="text-accent">&larr; Continue Shopping</a>
                </div>
            </div>

            <!-- Order Summary Card -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Summary</h2>
                </div>

                <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem;">
                    <span class="text-muted">Subtotal:</span>
                    <span>Rs. <fmt:formatNumber value="${subtotal}" pattern="#,##0.00" /></span>
                </div>

                <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem;">
                    <span class="text-muted">Estimated Tax (8%):</span>
                    <span>Rs. <fmt:formatNumber value="${tax}" pattern="#,##0.00" /></span>
                </div>

                <hr style="border: 0; border-top: 1px solid var(--border-color); margin: 1rem 0;">

                <div style="display: flex; justify-content: space-between; font-size: 1.25rem; font-weight: 700; margin-bottom: 1.5rem;">
                    <span>Total:</span>
                    <span class="text-accent">Rs. <fmt:formatNumber value="${total}" pattern="#,##0.00" /></span>
                </div>

                <a href="${pageContext.request.contextPath}/cart-summary" class="btn btn-primary" style="width: 100%;">
                    Proceed to Review &rarr;
                </a>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
