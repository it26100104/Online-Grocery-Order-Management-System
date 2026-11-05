<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Premium Farm Fresh Provisions" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<!-- 1. Hero Section -->
<section class="lively-hero">
    <div>
        <div class="hero-organic-badge">
            <span>🌿 100% Farm Fresh Quality</span>
            <span>&bull;</span>
            <span>Harvested Daily</span>
        </div>

        <h1 class="hero-main-title">
            Artisanal Groceries &amp;<br>
            <span>Pure Organic Food.</span>
        </h1>

        <p class="hero-main-subtitle">
            Premium organic produce harvested at dawn, grass-fed dairy, stoneground grains, and cold-pressed pantry essentials delivered direct to your door.
        </p>

        <!-- Live Coupon Promo Pills -->
        <div style="margin-bottom: 1.75rem; display: flex; flex-wrap: wrap; gap: 0.65rem; align-items: center;">
            <span style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.08em; color: var(--text-muted); font-weight: 700;">Promo Codes:</span>
            <span class="badge badge-warning" style="cursor: pointer; font-size: 0.78rem;" title="10% discount on any order">FRESH10 (-10%)</span>
            <span class="badge badge-warning" style="cursor: pointer; font-size: 0.78rem;" title="20% discount on any order">AURA20 (-20%)</span>
            <span class="badge badge-warning" style="cursor: pointer; font-size: 0.78rem;" title="15% discount on any order">SAVE15 (-15%)</span>
            <span class="badge badge-warning" style="cursor: pointer; font-size: 0.78rem;" title="Rs. 100 flat discount">WELCOME100 (-Rs.100)</span>
        </div>

        <div style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/catalog" class="btn btn-lg btn-primary">
                Shop Catalog &rarr;
            </a>
            <a href="${pageContext.request.contextPath}/catalog?category=Produce" class="btn btn-lg btn-ghost">
                Fresh Produce
            </a>
        </div>
    </div>

    <!-- Hero Right Basket Visual -->
    <div style="background: rgba(16, 19, 29, 0.95); border: 1px solid var(--border-subtle); border-radius: var(--radius-lg); padding: 2rem; backdrop-filter: blur(12px); box-shadow: var(--shadow-lg);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem; padding-bottom: 0.75rem; border-bottom: 1px solid var(--border-subtle);">
            <div>
                <span class="section-label" style="margin-bottom: 0;">Morning Harvest Basket</span>
                <p style="font-size: 0.85rem; color: #ffffff; font-weight: 700; margin: 0;">Curated Daily Essentials</p>
            </div>
            <span class="badge badge-success">● In Stock</span>
        </div>

        <div style="display: flex; flex-direction: column; gap: 1rem;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; align-items: center; gap: 0.85rem;">
                    <img src="https://images.unsplash.com/photo-1584270354949-c26b0d5b4a0c?auto=format&fit=crop&w=120&q=80" 
                         alt="Broccoli" style="width: 44px; height: 44px; border-radius: 8px; object-fit: cover; border: 1px solid var(--border-subtle);">
                    <div>
                        <strong style="color: #ffffff; font-size: 0.9rem;">Fresh Organic Broccoli</strong>
                        <p style="font-size: 0.75rem; color: var(--text-muted); margin: 0;">Farm-Fresh 500g</p>
                    </div>
                </div>
                <span style="color: var(--accent-primary); font-weight: 800;">Rs. 280.00</span>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; align-items: center; gap: 0.85rem;">
                    <img src="https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=120&q=80" 
                         alt="Milk" style="width: 44px; height: 44px; border-radius: 8px; object-fit: cover; border: 1px solid var(--border-subtle);">
                    <div>
                        <strong style="color: #ffffff; font-size: 0.9rem;">Grass-Fed Organic Milk</strong>
                        <p style="font-size: 0.75rem; color: var(--text-muted); margin: 0;">Whole Milk 1L</p>
                    </div>
                </div>
                <span style="color: var(--accent-primary); font-weight: 800;">Rs. 350.00</span>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; align-items: center; gap: 0.85rem;">
                    <img src="https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?auto=format&fit=crop&w=120&q=80" 
                         alt="Avocados" style="width: 44px; height: 44px; border-radius: 8px; object-fit: cover; border: 1px solid var(--border-subtle);">
                    <div>
                        <strong style="color: #ffffff; font-size: 0.9rem;">Ripe Hass Avocados</strong>
                        <p style="font-size: 0.75rem; color: var(--text-muted); margin: 0;">2-Pack Orchard</p>
                    </div>
                </div>
                <span style="color: var(--accent-primary); font-weight: 800;">Rs. 420.00</span>
            </div>
        </div>

        <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border-subtle); display: flex; justify-content: space-between; align-items: center;">
            <span style="font-size: 0.85rem; color: var(--text-secondary);">⚡ Express Courier Dispatch:</span>
            <span style="font-size: 0.9rem; font-weight: 700; color: #ffffff;">Under 2 Hours</span>
        </div>
    </div>
</section>

<!-- 2. Feature Trust Pillars -->
<div class="trust-pillars-grid">
    <div class="trust-pillar-card">
        <div class="trust-pillar-icon" style="color: var(--accent-gold);">⭐</div>
        <h3 class="trust-pillar-title">Top Rank Farms</h3>
        <p class="trust-pillar-desc">Direct farm-fresh supply ensuring pure nutritional density and pesticide-free purity.</p>
    </div>

    <div class="trust-pillar-card">
        <div class="trust-pillar-icon" style="color: #10b981;">✔️</div>
        <h3 class="trust-pillar-title">Organic Certified</h3>
        <p class="trust-pillar-desc">Guaranteed natural cultivation for clean living and wholesome family health.</p>
    </div>

    <div class="trust-pillar-card">
        <div class="trust-pillar-icon" style="color: #38bdf8;">🚚</div>
        <h3 class="trust-pillar-title">Fast Delivery</h3>
        <p class="trust-pillar-desc">Priority same-day couriers delivering fresh food to your door under 2 hours.</p>
    </div>

    <div class="trust-pillar-card">
        <div class="trust-pillar-icon" style="color: #a855f7;">🛡️</div>
        <h3 class="trust-pillar-title">Secure Checkout</h3>
        <p class="trust-pillar-desc">Encrypted transactions, instant digital invoicing, and bulk wholesale rates.</p>
    </div>
</div>

<!-- 3. Promotional Cards -->
<div class="promo-banner-grid">
    <div class="promo-card promo-card-1">
        <div>
            <span class="badge badge-danger" style="margin-bottom: 0.75rem;">Special Deal</span>
            <h3 class="promo-card-title">Fresh Fruit Harvest Up To 30% Off</h3>
            <p style="font-size: 0.85rem; margin-bottom: 1.25rem;">Hand-selected crisp apples, seasonal berries, and citrus.</p>
        </div>
        <a href="${pageContext.request.contextPath}/catalog?category=Produce" class="btn btn-sm btn-ghost" style="align-self: flex-start;">
            Shop Produce &rarr;
        </a>
    </div>

    <div class="promo-card promo-card-2">
        <div>
            <span class="badge badge-success" style="margin-bottom: 0.75rem;">100% Organic</span>
            <h3 class="promo-card-title">Daily Garden Greens &amp; Vegetables</h3>
            <p style="font-size: 0.85rem; margin-bottom: 1.25rem;">Garden-crisp greens and root vegetables harvested at dawn.</p>
        </div>
        <a href="${pageContext.request.contextPath}/catalog?category=Produce" class="btn btn-sm btn-primary" style="align-self: flex-start;">
            Explore Greens &rarr;
        </a>
    </div>

    <div class="promo-card promo-card-3">
        <div>
            <span class="badge badge-warning" style="margin-bottom: 0.75rem;">Artisanal Dairy</span>
            <h3 class="promo-card-title">Farm Fresh Milk, Eggs &amp; Butter</h3>
            <p style="font-size: 0.85rem; margin-bottom: 1.25rem;">Grass-fed whole milk, farm eggs, and wholesome essentials.</p>
        </div>
        <a href="${pageContext.request.contextPath}/catalog?category=Dairy" class="btn btn-sm btn-ghost" style="align-self: flex-start;">
            View Dairy &rarr;
        </a>
    </div>
</div>

<!-- 4. Free Delivery Banner -->
<div class="free-delivery-banner">
    <span>🚚</span>
    <span>Free Express Courier Delivery When You Spend Over Rs. 2,500</span>
</div>

<!-- 5. Featured Daily Grocery Showcase -->
<div class="dotted-divider">
    <span class="dotted-divider-title">Featured Provisions</span>
</div>

<div class="grid-5">
    <!-- Item 1 -->
    <div class="lively-product-card">
        <div>
            <div class="card-img-container">
                <img src="https://images.unsplash.com/photo-1550583724-b2692b85b150?auto=format&fit=crop&w=500&q=80" 
                     alt="Milk" class="product-thumb-img" loading="lazy">
            </div>
            <span class="product-tag">Dairy</span>
            <h4 class="lively-product-title">Fresh Organic Milk</h4>
            <div class="product-weight">1L Glass Bottle</div>
            <div class="product-rating">
                <span>★★★★★</span>
                <span class="score">4.9</span>
            </div>
            <div class="product-card-price">Rs. 350.00</div>
        </div>
        <a href="${pageContext.request.contextPath}/catalog" class="btn-add-cart">
            View in Catalog
        </a>
    </div>

    <!-- Item 2 -->
    <div class="lively-product-card">
        <div>
            <div class="card-img-container">
                <img src="https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?auto=format&fit=crop&w=500&q=80" 
                     alt="Apples" class="product-thumb-img" loading="lazy">
            </div>
            <span class="product-tag">Produce</span>
            <h4 class="lively-product-title">Crisp Red Apples</h4>
            <div class="product-weight">1 Kg Handpicked</div>
            <div class="product-rating">
                <span>★★★★★</span>
                <span class="score">4.8</span>
            </div>
            <div class="product-card-price">Rs. 450.00</div>
        </div>
        <a href="${pageContext.request.contextPath}/catalog" class="btn-add-cart">
            View in Catalog
        </a>
    </div>

    <!-- Item 3 -->
    <div class="lively-product-card">
        <div>
            <div class="card-img-container">
                <img src="https://images.unsplash.com/photo-1584270354949-c26b0d5b4a0c?auto=format&fit=crop&w=500&q=80" 
                     alt="Broccoli" class="product-thumb-img" loading="lazy">
            </div>
            <span class="product-tag">Produce</span>
            <h4 class="lively-product-title">Fresh Broccoli</h4>
            <div class="product-weight">500 gm Fresh Cut</div>
            <div class="product-rating">
                <span>★★★★★</span>
                <span class="score">4.9</span>
            </div>
            <div class="product-card-price">Rs. 280.00</div>
        </div>
        <a href="${pageContext.request.contextPath}/catalog" class="btn-add-cart">
            View in Catalog
        </a>
    </div>

    <!-- Item 4 -->
    <div class="lively-product-card">
        <div>
            <div class="card-img-container">
                <img src="https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=500&q=80" 
                     alt="Bread" class="product-thumb-img" loading="lazy">
            </div>
            <span class="product-tag">Bakery</span>
            <h4 class="lively-product-title">Artisan Sourdough</h4>
            <div class="product-weight">Loaf Fresh Baked</div>
            <div class="product-rating">
                <span>★★★★★</span>
                <span class="score">4.8</span>
            </div>
            <div class="product-card-price">Rs. 320.00</div>
        </div>
        <a href="${pageContext.request.contextPath}/catalog" class="btn-add-cart">
            View in Catalog
        </a>
    </div>

    <!-- Item 5 -->
    <div class="lively-product-card">
        <div>
            <div class="card-img-container">
                <img src="https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?auto=format&fit=crop&w=500&q=80" 
                     alt="Olive Oil" class="product-thumb-img" loading="lazy">
            </div>
            <span class="product-tag">Pantry</span>
            <h4 class="lively-product-title">Extra Virgin Olive Oil</h4>
            <div class="product-weight">750 ml Cold-Pressed</div>
            <div class="product-rating">
                <span>★★★★★</span>
                <span class="score">5.0</span>
            </div>
            <div class="product-card-price">Rs. 1,850.00</div>
        </div>
        <a href="${pageContext.request.contextPath}/catalog" class="btn-add-cart">
            View in Catalog
        </a>
    </div>
</div>

<!-- 6. Deal Newsletter Subscription Bar -->
<section class="deal-newsletter-banner" style="margin-top: 3.5rem; background: linear-gradient(135deg, rgba(245, 158, 11, 0.15) 0%, rgba(16, 19, 29, 0.95) 100%); border: 1px solid rgba(245, 158, 11, 0.35);">
    <div>
        <h3 style="font-size: 1.6rem; margin-bottom: 0.35rem; color: #ffffff;">Unlock Exclusive Grocery Discounts</h3>
        <p style="margin-bottom: 0; color: var(--text-secondary); font-size: 0.95rem;">
            Use promo code <strong style="color: var(--accent-gold);">FRESH10</strong> for 10% off, or <strong style="color: var(--accent-gold);">AURA20</strong> for 20% off at final order review.
        </p>
    </div>

    <div>
        <a href="${pageContext.request.contextPath}/catalog" class="btn btn-primary" style="padding: 0.85rem 2rem;">
            Browse Full Catalog &rarr;
        </a>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

