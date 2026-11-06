<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Payment for Order #${order.orderId}" scope="request" />

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="max-width: 600px; margin: 1.5rem auto;">
    <div class="card">
        <div class="card-header">
            <div>
                <h1 class="card-title">Complete Payment</h1>
                <p style="margin-bottom: 0;">Order ID: #<c:out value="${order.orderId}" /></p>
            </div>
            <div style="text-align: right;">
                <span class="product-price">Rs. <fmt:formatNumber value="${order.total}" pattern="#,##0.00" /></span>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/payment" method="post" onsubmit="return validatePayment();">
            <input type="hidden" name="orderId" value="<c:out value='${order.orderId}' />">

            <div class="form-group">
                <label class="form-label">Select Payment Method *</label>
                <div style="display: flex; gap: 1rem; flex-wrap: wrap; margin-top: 0.5rem;">
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 0.4rem;">
                        <input type="radio" name="methodType" value="CARD" checked onchange="toggleMethodFields();"> Credit / Debit Card
                    </label>
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 0.4rem;">
                        <input type="radio" name="methodType" value="WALLET" onchange="toggleMethodFields();"> Digital Wallet
                    </label>
                    <label style="cursor: pointer; display: flex; align-items: center; gap: 0.4rem;">
                        <input type="radio" name="methodType" value="COD" onchange="toggleMethodFields();"> Cash On Delivery (COD)
                    </label>
                </div>
            </div>

            <!-- Card Payment Fields -->
            <div id="cardFields" style="margin-top: 1.25rem;">
                <div class="form-group">
                    <label class="form-label">Card Number *</label>
                    <input type="text" id="cardNumber" name="cardNumber" class="form-control" placeholder="4242 •••• •••• 4242" maxlength="19">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">Expiry Date *</label>
                        <input type="text" id="expiry" name="expiry" class="form-control" placeholder="MM/YY" maxlength="5">
                    </div>
                    <div class="form-group">
                        <label class="form-label">CVV Code *</label>
                        <input type="password" id="cvv" name="cvv" class="form-control" placeholder="•••" maxlength="4">
                    </div>
                </div>

                <div class="form-group">
                    <label style="cursor: pointer; font-size: 0.9rem;">
                        <input type="checkbox" name="saveMethod" value="true"> Save this card securely for future purchases
                    </label>
                </div>
            </div>

            <!-- Digital Wallet Fields -->
            <div id="walletFields" style="display: none; margin-top: 1.25rem;">
                <div class="form-group">
                    <label class="form-label">Mobile Wallet Number *</label>
                    <input type="tel" id="walletMobile" name="walletMobile" class="form-control" placeholder="+94 77 123 4567">
                </div>
                <div class="form-group">
                    <label style="cursor: pointer; font-size: 0.9rem;">
                        <input type="checkbox" name="saveMethod" value="true"> Remember this wallet for 1-click checkout
                    </label>
                </div>
            </div>

            <!-- COD Note -->
            <div id="codFields" style="display: none; margin-top: 1.25rem; background: var(--bg-page); padding: 1rem; border-radius: 8px;">
                <p style="margin-bottom: 0; font-size: 0.9rem; color: var(--color-warning);">
                    Pay in cash upon physical delivery. Please have exact change ready.
                </p>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1.5rem; padding: 0.8rem;">
                Authorize &amp; Pay Rs. <fmt:formatNumber value="${order.total}" pattern="#,##0.00" />
            </button>
        </form>
    </div>
</div>

<script>
    function toggleMethodFields() {
        var card = document.querySelector('input[name="methodType"][value="CARD"]').checked;
        var wallet = document.querySelector('input[name="methodType"][value="WALLET"]').checked;
        var cod = document.querySelector('input[name="methodType"][value="COD"]').checked;

        document.getElementById('cardFields').style.display = card ? 'block' : 'none';
        document.getElementById('walletFields').style.display = wallet ? 'block' : 'none';
        document.getElementById('codFields').style.display = cod ? 'block' : 'none';
    }

    function validatePayment() {
        var card = document.querySelector('input[name="methodType"][value="CARD"]').checked;
        if (card) {
            var num = document.getElementById('cardNumber').value.trim();
            var cvv = document.getElementById('cvv').value.trim();
            if (num.length < 12 || cvv.length < 3) {
                alert('Please enter valid credit/debit card information.');
                return false;
            }
        }
        return true;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
