<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    </main>

    <footer class="footer">
        <div class="footer-inner">
            <div class="footer-col">
                <div class="footer-brand">
                    <span class="brand-badge">A</span>
                    AURA <span>GROCERY</span>
                </div>
                <p class="footer-desc">
                    The modern provisions market. Fresh organic farm produce, artisanal dairy, heirloom pantry staples, and cold-pressed elixirs delivered straight to your door with zero friction.
                </p>
                <div style="display: flex; gap: 0.75rem; align-items: center; font-size: 0.85rem; color: var(--text-muted);">
                    <span>🌿 100% Certified Organic</span>
                    <span>&bull;</span>
                    <span>⚡ Under 2h Dispatch</span>
                </div>
            </div>

            <div class="footer-col">
                <h4>Marketplace</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/catalog">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalog?category=Produce">Fresh Produce</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalog?category=Dairy">Dairy &amp; Eggs</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalog?category=Pantry">Pantry Essentials</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalog?category=Bakery">Bakery</a></li>
                </ul>
            </div>

            <div class="footer-col">
                <h4>Customer Care</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/deliveries">Order Tracking</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Shopping Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist">Saved Wishlist</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders">Order Receipts</a></li>
                </ul>
            </div>

            <div class="footer-col">
                <h4>Trust &amp; Security</h4>
                <ul class="footer-links">
                    <li><span style="color: var(--text-secondary); font-size: 0.85rem;">🔒 256-Bit SSL Checkout</span></li>
                    <li><span style="color: var(--text-secondary); font-size: 0.85rem;">❄️ Cold-Chain Transit</span></li>
                    <li><span style="color: var(--text-secondary); font-size: 0.85rem;">♻️ 100% Recyclable Totes</span></li>
                    <li><span style="color: var(--text-secondary); font-size: 0.85rem;">💳 Instant Wallet Refund</span></li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <p>&copy; 2026 Aura Grocery Marketplace. All rights reserved.</p>
            <div style="display: flex; gap: 1.5rem; font-size: 0.8rem;">
                <span class="text-muted">Direct Farm Sourcing</span>
                <span class="text-muted">Zero Middlemen</span>
                <span class="text-muted">Quality Guaranteed</span>
            </div>
        </div>
    </footer>

</body>
</html>
