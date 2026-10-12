<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Grocery Catalog" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>Fresh Grocery Catalog</h1>
    <p>Browse fresh farm perishables, pantry essentials, and household goods.</p>
</div>

<!-- Search & Category Filter Bar -->
<div class="card" style="padding: 1.25rem;">
    <form action="${pageContext.request.contextPath}/catalog" method="get" class="form-row" style="align-items: flex-end;">
        <div class="form-group" style="flex: 2; min-width: 260px; margin-bottom: 0;">
            <label class="form-label">Search by Name or Keyword</label>
            <input type="text" name="search" class="form-control" placeholder="e.g. Milk, Rice, Tea..." value="<c:out value='${searchQuery}' />">
        </div>

        <div class="form-group" style="flex: 1; min-width: 180px; margin-bottom: 0;">
            <label class="form-label">Category</label>
            <select name="category" class="form-control">
                <option value="ALL">All Categories</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="<c:out value='${cat}' />" ${selectedCategory eq cat ? 'selected' : ''}>
                        <c:out value="${cat}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <div style="display: flex; gap: 0.5rem;">
            <button type="submit" class="btn btn-primary">Filter</button>
            <a href="${pageContext.request.contextPath}/catalog" class="btn btn-ghost">Reset</a>
        </div>
    </form>
</div>

<!-- Products Grid -->
<c:choose>
    <c:when test="${empty products}">
        <div class="card text-center" style="padding: 3rem 1rem;">
            <h3>No products found</h3>
            <p>Try searching for a different keyword or reset your category filter.</p>
            <a href="${pageContext.request.contextPath}/catalog" class="btn btn-ghost">View All Products</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="product-grid">
            <c:forEach var="p" items="${products}">
                <div class="lively-product-card">
                    <div>
                        <div class="card-img-container">
                            <img src="<c:out value='${p.imageUrl}' />" 
                                 alt="<c:out value='${p.name}' />" 
                                 class="product-thumb-img" 
                                 loading="lazy"
                                 onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=500&q=80';">
                        </div>

                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.35rem;">
                            <span class="product-tag"><c:out value="${p.category}" /></span>
                            <c:choose>
                                <c:when test="${p.inStock}">
                                    <span class="badge badge-success" style="font-size: 0.65rem;">Stock: <c:out value="${p.stock}" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger" style="font-size: 0.65rem;">Out of Stock</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <h3 class="lively-product-title">
                            <a href="${pageContext.request.contextPath}/product-details?id=${p.productId}"><c:out value="${p.name}" /></a>
                        </h3>

                        <div class="product-rating">
                            <span>★★★★★</span>
                            <span class="score">4.9</span>
                        </div>

                        <p style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.75rem; line-height: 1.4;">
                            <c:out value="${p.displayInfo()}" />
                        </p>
                    </div>

                    <div>
                        <div class="product-card-price">
                            Rs. <fmt:formatNumber value="${p.discountedPrice}" pattern="#,##0.00" />
                            <c:if test="${p.discountedPrice < p.price}">
                                <span style="text-decoration: line-through; color: var(--text-muted); font-size: 0.8rem; margin-left: 0.35rem; font-weight: normal;">
                                    Rs. <fmt:formatNumber value="${p.price}" pattern="#,##0.00" />
                                </span>
                            </c:if>
                        </div>

                        <div style="display: flex; gap: 0.5rem;">
                            <c:choose>
                                <c:when test="${p.inStock}">
                                    <form action="${pageContext.request.contextPath}/cart" method="post" style="flex: 1;">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="productId" value="<c:out value='${p.productId}' />">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn-add-cart">
                                            Add to Cart
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-sm btn-ghost" style="width: 100%; opacity: 0.5;" disabled>Out of Stock</button>
                                </c:otherwise>
                            </c:choose>
                            <a href="${pageContext.request.contextPath}/product-details?id=${p.productId}" class="btn btn-sm btn-ghost" title="Details">
                                Details
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
