<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="navbar-wrapper">
    <!-- 1. Top Utility Announcement Bar -->
    <div class="navbar-top-bar">
        <div class="navbar-top-inner">
            <div class="top-announcement">
                <span>🌿</span>
                <span>Welcome to <strong>Aura Grocery</strong> &bull; Farm Fresh 100% Organic Provisions</span>
            </div>
            <div class="top-nav-links">
                <a href="${pageContext.request.contextPath}/deliveries"><span>📦</span> Order Tracking</a>
                <span class="top-nav-separator">|</span>
                <a href="${pageContext.request.contextPath}/wishlist"><span>❤️</span> My Wishlist</a>
                <span class="top-nav-separator">|</span>
                <span class="top-hotline"><span>📞</span> Hotline: <strong>(555) 019-2834</strong></span>
            </div>
        </div>
    </div>

    <!-- 2. Main Navigation Bar with Search & Quick Actions -->
    <div class="navbar-main">
        <div class="navbar-main-inner">
            <!-- Brand Logo -->
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">
                <span class="brand-badge">🍃</span>
                <span class="brand-text">AURA <span class="brand-highlight">GROCERY</span></span>
            </a>

            <!-- Central Search Bar (Screenshot Inspired) -->
            <form action="${pageContext.request.contextPath}/catalog" method="get" class="nav-search-bar">
                <input type="text" name="search" class="nav-search-input" placeholder="Search fresh vegetables, fruits, dairy, bakery..." value="<c:out value='${param.search}' />">
                <button type="submit" class="nav-search-button" aria-label="Search">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="11" cy="11" r="8"></circle>
                        <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                    </svg>
                </button>
            </form>

            <!-- Right Action Widgets -->
            <div class="navbar-widgets">
                <!-- Customer Contact -->
                <div class="widget-contact">
                    <div class="widget-icon">📞</div>
                    <div class="widget-details">
                        <span class="widget-label">Support 24/7</span>
                        <strong class="widget-value">(555) 019-2834</strong>
                    </div>
                </div>

                <!-- User Account Info / Dropdown Trigger -->
                <c:choose>
                    <c:when test="${empty sessionScope.userId}">
                        <a href="${pageContext.request.contextPath}/login" class="widget-account">
                            <div class="widget-icon">👤</div>
                            <div class="widget-details">
                                <span class="widget-label">Account</span>
                                <strong class="widget-value">Sign In / Join</strong>
                            </div>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <div class="widget-account logged-in">
                            <div class="widget-icon">👤</div>
                            <div class="widget-details">
                                <span class="widget-label">
                                    <c:choose>
                                        <c:when test="${sessionScope.role eq 'ADMIN'}">Admin Staff</c:when>
                                        <c:otherwise>Customer</c:otherwise>
                                    </c:choose>
                                </span>
                                <strong class="widget-value"><c:out value="${sessionScope.fullName}" /></strong>
                            </div>
                            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-ghost" style="margin-left: 0.5rem; padding: 0.25rem 0.5rem; font-size: 0.75rem;">Logout</a>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Lively Green Cart Pill -->
                <a href="${pageContext.request.contextPath}/cart" class="nav-cart-pill">
                    <div class="cart-pill-icon">🛒</div>
                    <div class="cart-pill-text">
                        <span class="cart-pill-title">Cart</span>
                        <span class="cart-pill-status">View Items &rarr;</span>
                    </div>
                </a>
            </div>
        </div>
    </div>

    <!-- 3. Sub-Navbar Department Strip -->
    <div class="navbar-sub">
        <div class="navbar-sub-inner">
            <nav class="sub-nav-links">
                <a href="${pageContext.request.contextPath}/" class="sub-nav-link">Home</a>
                <a href="${pageContext.request.contextPath}/catalog" class="sub-nav-link">All Products</a>
                <a href="${pageContext.request.contextPath}/catalog?category=Produce" class="sub-nav-link">🥬 Fresh Produce</a>
                <a href="${pageContext.request.contextPath}/catalog?category=Dairy" class="sub-nav-link">🥛 Dairy &amp; Eggs</a>
                <a href="${pageContext.request.contextPath}/catalog?category=Pantry" class="sub-nav-link">🌾 Pantry Essentials</a>
                <a href="${pageContext.request.contextPath}/catalog?category=Bakery" class="sub-nav-link">🥖 Organic Bakery</a>
                <a href="${pageContext.request.contextPath}/catalog" class="sub-nav-link highlight-deal">🔥 Flash Deals</a>

                <c:if test="${not empty sessionScope.userId and sessionScope.role eq 'CUSTOMER'}">
                    <span class="sub-nav-separator">|</span>
                    <a href="${pageContext.request.contextPath}/orders" class="sub-nav-link">My Orders</a>
                    <a href="${pageContext.request.contextPath}/profile" class="sub-nav-link">My Profile</a>
                </c:if>

                <c:if test="${not empty sessionScope.userId and sessionScope.role eq 'ADMIN'}">
                    <span class="sub-nav-separator">|</span>
                    <span class="badge badge-success" style="font-size: 0.65rem;">ADMIN CONSOLE</span>
                    <a href="${pageContext.request.contextPath}/admin/products" class="sub-nav-link">Inventory</a>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="sub-nav-link">Orders</a>
                    <a href="${pageContext.request.contextPath}/admin/payments" class="sub-nav-link">Ledger</a>
                    <a href="${pageContext.request.contextPath}/admin/deliveries" class="sub-nav-link">Logistics</a>
                    <a href="${pageContext.request.contextPath}/admin/users" class="sub-nav-link">Users</a>
                    <a href="${pageContext.request.contextPath}/admin/reports" class="sub-nav-link">Analytics</a>
                </c:if>
            </nav>
        </div>
    </div>
</header>
