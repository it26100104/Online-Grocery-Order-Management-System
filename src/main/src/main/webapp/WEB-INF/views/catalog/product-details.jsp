<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="${product.name} - Details" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 1.5rem;">
    <a href="${pageContext.request.contextPath}/catalog" class="text-muted" style="font-size: 0.9rem;">
        &larr; Back to Catalog
    </a>
</div>

<div class="card" style="padding: 2rem;">
    <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 1rem;">
        <div>
            <span class="badge badge-info"><c:out value="${product.category}" /></span>
            <span class="badge badge-success" style="margin-left: 0.5rem;"><c:out value="${product.type}" /></span>
            <h1 style="margin-top: 0.75rem; font-size: 2rem;"><c:out value="${product.name}" /></h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">Product ID: <code><c:out value="${product.productId}" /></code></p>
        </div>

        <div style="text-align: right;">
            <div style="font-size: 2rem; font-weight: 700; color: var(--color-accent);">
                Rs. <fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00" />
            </div>
            <c:if test="${product.discountedPrice < product.price}">
                <p style="text-decoration: line-through; margin-bottom: 0;">
                    Regular: Rs. <fmt:formatNumber value="${product.price}" pattern="#,##0.00" />
                </p>
            </c:if>
        </div>
    </div>

    <hr style="border: 0; border-top: 1px solid var(--border-color); margin: 1.5rem 0;">

    <div class="grid-2" style="align-items: start;">
        <div>
            <img src="<c:out value='${product.imageUrl}' />" 
                 alt="<c:out value='${product.name}' />" 
                 class="product-detail-img" 
                 onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=700&q=80';">

            <!-- Polymorphic Display Info -->
            <div class="card" style="background-color: var(--bg-surface-elevated); border-color: var(--border-subtle); padding: 1.25rem;">
                <h4 style="margin-bottom: 0.35rem; color: var(--accent-gold);">Storage &amp; Validity</h4>
                <p style="margin-bottom: 0; color: #ffffff; font-weight: 500;">
                    <c:out value="${product.displayInfo()}" />
                </p>
            </div>
        </div>

        <div>
            <h3>Product Overview</h3>
            <p style="font-size: 1.05rem; line-height: 1.7;"><c:out value="${product.description}" /></p>

            <div class="card" style="background-color: var(--bg-surface-elevated); padding: 1.75rem; border-color: var(--border-subtle); margin-top: 1.5rem;">
                <h3 style="margin-bottom: 1.25rem;">Order Options</h3>

                <div style="margin-bottom: 1.25rem;">
                    <p style="margin-bottom: 0.5rem;"><strong>Availability:</strong></p>
                    <c:choose>
                        <c:when test="${product.inStock}">
                            <span class="badge badge-success" style="font-size: 0.85rem;">
                                In Stock (<c:out value="${product.stock}" /> items left)
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-danger" style="font-size: 0.85rem;">Currently Out of Stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Add To Cart Form -->
                <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="<c:out value='${product.productId}' />">

                    <div class="form-group">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" id="quantity" name="quantity" class="form-control"
                               value="1" min="1" max="${product.stock}" ${product.inStock ? '' : 'disabled'}>
                    </div>

                    <div style="display: flex; gap: 0.75rem; flex-wrap: wrap;">
                        <button type="submit" class="btn btn-primary" style="flex: 2;" ${product.inStock ? '' : 'disabled'}>
                            Add to Cart
                        </button>
                    </div>
                </form>

                <!-- Add To Wishlist Form -->
                <form action="${pageContext.request.contextPath}/wishlist" method="post" style="margin-top: 0.75rem;">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="<c:out value='${product.productId}' />">
                    <button type="submit" class="btn btn-ghost" style="width: 100%;">
                        Save to Wishlist
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
