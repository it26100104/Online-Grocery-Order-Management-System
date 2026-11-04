<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order Review & Checkout" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="margin-bottom: 2rem;">
    <h1>Review &amp; Place Order</h1>
    <p>Confirm your items, delivery address, promo discount code, and order type before checkout.</p>
</div>

<div class="split-layout-grid">
    <!-- Order Review Details -->
    <div>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Order Items</h2>
                <a href="${pageContext.request.contextPath}/cart" class="text-accent" style="font-size: 0.9rem;">Edit Cart</a>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Unit Price</th>
                            <th>Qty</th>
                            <th style="text-align: right;">Line Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cartItems}">
                            <tr>
                                <td>
                                    <strong><c:out value="${item.productName}" /></strong>
                                </td>
                                <td>Rs. <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" /></td>
                                <td><c:out value="${item.quantity}" /></td>
                                <td style="text-align: right;">
                                    <strong>Rs. <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.00" /></strong>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Checkout Form Posting to Member 4's Order Processing -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Delivery &amp; Order Type</h2>
            </div>

            <form action="${pageContext.request.contextPath}/orders" method="post" id="placeOrderForm">
                <input type="hidden" name="action" value="placeOrder">
                <!-- Hidden Coupon Code for Final Submission -->
                <input type="hidden" id="appliedCouponCode" name="couponCode" value="">

                <div class="form-group">
                    <label for="address" class="form-label">Delivery Destination *</label>
                    <textarea id="address" name="address" class="form-control" rows="2" required><c:out value="${user.address}" /></textarea>
                </div>

                <div class="form-group">
                    <label for="orderType" class="form-label">Order Type *</label>
                    <select id="orderType" name="orderType" class="form-control" required>
                        <option value="REGULAR">Regular Order (Standard processing)</option>
                        <option value="BULK">Bulk Order (Eligible for bulk wholesale discount)</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.85rem; font-size: 1.05rem;">
                    Confirm &amp; Place Order &rarr;
                </button>
            </form>
        </div>
    </div>

    <!-- Pricing Summary Card with Coupon Code -->
    <div>
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Price Breakdown</h2>
            </div>

            <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem;">
                <span class="text-muted">Subtotal:</span>
                <span id="baseSubtotal">Rs. <fmt:formatNumber value="${subtotal}" pattern="#,##0.00" /></span>
            </div>

            <!-- Discount Line (Dynamically Shown on Coupon Application) -->
            <div id="discountRow" style="display: none; justify-content: space-between; margin-bottom: 0.75rem; color: var(--accent-gold);">
                <span id="discountLabel">Promo Discount:</span>
                <strong id="discountValue">-Rs. 0.00</strong>
            </div>

            <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem;">
                <span class="text-muted">Government Tax (8%):</span>
                <span>Rs. <fmt:formatNumber value="${tax}" pattern="#,##0.00" /></span>
            </div>

            <hr style="border: 0; border-top: 1px solid var(--border-color); margin: 1rem 0;">

            <div style="display: flex; justify-content: space-between; font-size: 1.3rem; font-weight: 700;">
                <span>Estimated Total:</span>
                <span class="text-accent" id="finalTotal">Rs. <fmt:formatNumber value="${total}" pattern="#,##0.00" /></span>
            </div>

            <p style="font-size: 0.8rem; color: var(--text-muted); margin-top: 1rem; line-height: 1.4;">
                * Bulk Orders automatically receive an additional 10% wholesale discount upon placement.
            </p>
        </div>

        <!-- Coupon / Promo Code Box (Screenshot Requested Feature) -->
        <div class="card" style="border-color: rgba(245, 158, 11, 0.35);">
            <div class="card-header" style="border-bottom: none; padding-bottom: 0.25rem; margin-bottom: 0.5rem;">
                <h3 style="font-size: 1.05rem; margin: 0; display: flex; align-items: center; gap: 0.4rem;">
                    <span>🎟️</span> Have a Discount Code?
                </h3>
            </div>
            <p style="font-size: 0.82rem; color: var(--text-muted); margin-bottom: 0.75rem;">
                Try code <code style="color: var(--accent-gold); font-weight: 700;">FRESH10</code> (10% off), <code style="color: var(--accent-gold); font-weight: 700;">AURA20</code> (20% off), or <code style="color: var(--accent-gold); font-weight: 700;">SAVE15</code>.
            </p>

            <div style="display: flex; gap: 0.5rem;">
                <input type="text" id="couponInput" class="form-control" placeholder="Enter coupon code" style="text-transform: uppercase; font-weight: 700; letter-spacing: 0.05em;">
                <button type="button" class="btn btn-primary" onclick="applyCoupon();" style="padding: 0.65rem 1.25rem;">
                    Apply
                </button>
            </div>
            <div id="couponStatusMessage" style="margin-top: 0.6rem; font-size: 0.85rem; font-weight: 600;"></div>
        </div>
    </div>
</div>

<script>
    var rawSubtotal = ${subtotal};
    var rawTax = ${tax};
    var rawTotal = ${total};
    var appliedDiscount = 0.0;

    function applyCoupon() {
        var input = document.getElementById('couponInput').value.trim().toUpperCase();
        var status = document.getElementById('couponStatusMessage');
        var discountRow = document.getElementById('discountRow');
        var discountLabel = document.getElementById('discountLabel');
        var discountValue = document.getElementById('discountValue');
        var finalTotal = document.getElementById('finalTotal');
        var hiddenCoupon = document.getElementById('appliedCouponCode');

        if (!input) {
            status.style.color = '#f87171';
            status.innerText = 'Please enter a coupon code.';
            return;
        }

        var discountRate = 0.0;
        var discountFlat = 0.0;

        if (input === 'FRESH10') {
            discountRate = 0.10;
        } else if (input === 'AURA20') {
            discountRate = 0.20;
        } else if (input === 'SAVE15') {
            discountRate = 0.15;
        } else if (input === 'FIRSTBUY' || input === 'WELCOME100') {
            discountFlat = Math.min(100.0, rawSubtotal);
        } else {
            status.style.color = '#f87171';
            status.innerText = 'Invalid coupon code. Try FRESH10, AURA20, or SAVE15.';
            return;
        }

        if (discountRate > 0) {
            appliedDiscount = Math.round(rawSubtotal * discountRate * 100.0) / 100.0;
            discountLabel.innerText = 'Coupon Discount (' + input + ' -' + (discountRate * 100) + '%):';
        } else {
            appliedDiscount = discountFlat;
            discountLabel.innerText = 'Coupon Discount (' + input + ' -Rs. 100):';
        }

        var newTotal = Math.max(0.0, Math.round((rawTotal - appliedDiscount) * 100.0) / 100.0);

        // Update UI
        discountRow.style.display = 'flex';
        discountValue.innerText = '-Rs. ' + appliedDiscount.toFixed(2);
        finalTotal.innerText = 'Rs. ' + newTotal.toFixed(2);
        hiddenCoupon.value = input;

        status.style.color = '#10b981';
        status.innerText = '✓ Coupon ' + input + ' applied successfully! You saved Rs. ' + appliedDiscount.toFixed(2);
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
