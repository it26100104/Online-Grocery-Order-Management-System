<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.grocery.payment.model.Order, com.grocery.payment.util.HtmlUtil" %>
<%
    Order o = (Order) request.getAttribute("order");
    String payText = "CARD".equals(o.getPaymentMethod()) ? "Credit / Debit Card" : "Cash on Delivery";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <strong>Online Grocery - Order Confirmation</strong>
    <span><a href="checkout.jsp">Checkout</a><a href="orders">My Orders</a></span>
</header>

<div class="box">
    <h2 class="ok">Thank you! Your order is confirmed.</h2>
    <div class="row"><span>Order ID</span><strong><%= o.getOrderId() %></strong></div>
    <div class="row"><span>Status</span><span><%= o.getStatus() %></span></div>

    <h3>Delivery details</h3>
    <div class="row"><span>Name</span><span><%= HtmlUtil.esc(o.getCustomerName()) %></span></div>
    <div class="row"><span>Phone</span><span><%= HtmlUtil.esc(o.getPhone()) %></span></div>
    <div class="row"><span>Address</span><span><%= HtmlUtil.esc(o.getAddress()) %>, <%= HtmlUtil.esc(o.getCity()) %></span></div>

    <h3>Payment</h3>
    <div class="row"><span>Method</span><span><%= payText %></span></div>
    <div class="row"><span>Cart total</span><span>Rs. <%= String.format("%.2f", o.getSubtotal()) %></span></div>
    <div class="row"><span>Delivery charge</span><span>Rs. <%= String.format("%.2f", o.getDeliveryCharge()) %></span></div>
    <div class="row"><span>Payment fee</span><span>Rs. <%= String.format("%.2f", o.getPaymentFee()) %></span></div>
    <div class="row total"><span>Total</span><span>Rs. <%= String.format("%.2f", o.getTotal()) %></span></div>

    <a class="btn" href="updateOrder?id=<%= o.getOrderId() %>">Change delivery address</a>
    <a class="btn btn-grey" href="orders">All orders</a>
</div>
</body>
</html>
