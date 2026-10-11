<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.grocery.payment.util.HtmlUtil" %>
<%
    // The cart total will come from the Cart member's part. Until it is connected, we use 2500 as a demo value.
    String subtotal = request.getParameter("subtotal");
    if (subtotal == null) subtotal = "2500";
    String method = request.getParameter("paymentMethod");
    if (method == null) method = "COD";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Checkout - Online Grocery</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <strong>Online Grocery - Checkout</strong>
    <span><a href="checkout.jsp">Checkout</a><a href="orders">My Orders</a></span>
</header>

<div class="box">
    <h2>Checkout</h2>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= HtmlUtil.esc((String) request.getAttribute("error")) %></div>
    <% } %>

    <form action="placeOrder" method="post">
        <input type="hidden" name="subtotal" value="<%= HtmlUtil.esc(subtotal) %>">

        <h3>Delivery address</h3>
        <label>Full name</label>
        <input type="text" name="fullName" value="<%= HtmlUtil.esc(request.getParameter("fullName")) %>">
        <label>Phone (10 digits)</label>
        <input type="text" name="phone" value="<%= HtmlUtil.esc(request.getParameter("phone")) %>">
        <label>Address</label>
        <textarea name="address" rows="2"><%= HtmlUtil.esc(request.getParameter("address")) %></textarea>
        <label>City</label>
        <input type="text" name="city" placeholder="Colombo, Gampaha, Kalutara..." value="<%= HtmlUtil.esc(request.getParameter("city")) %>">

        <h3>Payment method</h3>
        <label><input type="radio" name="paymentMethod" value="COD" style="width:auto"
            <%= "COD".equals(method) ? "checked" : "" %> onclick="toggleCard()"> Cash on Delivery (Rs. 50 fee)</label>
        <label><input type="radio" name="paymentMethod" value="CARD" style="width:auto"
            <%= "CARD".equals(method) ? "checked" : "" %> onclick="toggleCard()"> Credit / Debit Card (2% fee)</label>

        <div id="cardBox" style="display:none">
            <label>Card holder name</label>
            <input type="text" name="cardHolder" value="<%= HtmlUtil.esc(request.getParameter("cardHolder")) %>">
            <label>Card number (16 digits)</label>
            <input type="text" name="cardNumber" maxlength="19" placeholder="1234 5678 9012 3456">
        </div>

        <h3>Summary</h3>
        <div class="row"><span>Cart total</span><span>Rs. <%= HtmlUtil.esc(subtotal) %></span></div>
        <p style="font-size:13px;color:#555">
            Delivery: Colombo Rs. 200, Gampaha/Kalutara Rs. 350, other cities Rs. 500.
            Free delivery for orders of Rs. 10,000 or more.
        </p>

        <button type="submit">Place Order</button>
    </form>
</div>

<script>
    function toggleCard() {
        var isCard = document.querySelector('input[name="paymentMethod"]:checked').value === 'CARD';
        document.getElementById('cardBox').style.display = isCard ? 'block' : 'none';
    }
    toggleCard();
</script>
</body>
</html>
