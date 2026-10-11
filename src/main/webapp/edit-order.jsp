<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.grocery.payment.model.Order, com.grocery.payment.util.HtmlUtil" %>
<%
    Order o = (Order) request.getAttribute("order");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Delivery Details</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <strong>Online Grocery - Edit Delivery</strong>
    <span><a href="checkout.jsp">Checkout</a><a href="orders">My Orders</a></span>
</header>

<div class="box">
    <h2>Edit delivery details - <%= o.getOrderId() %></h2>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= HtmlUtil.esc((String) request.getAttribute("error")) %></div>
    <% } %>
    <form action="updateOrder" method="post">
        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
        <label>Full name</label>
        <input type="text" name="fullName" value="<%= HtmlUtil.esc(o.getCustomerName()) %>">
        <label>Phone</label>
        <input type="text" name="phone" value="<%= HtmlUtil.esc(o.getPhone()) %>">
        <label>Address</label>
        <textarea name="address" rows="2"><%= HtmlUtil.esc(o.getAddress()) %></textarea>
        <label>City</label>
        <input type="text" name="city" value="<%= HtmlUtil.esc(o.getCity()) %>">
        <p style="font-size:13px;color:#555">The delivery charge is recalculated if you change the city.</p>
        <button type="submit">Save changes</button>
        <a class="btn btn-grey" href="order?id=<%= o.getOrderId() %>">Back</a>
    </form>
</div>
</body>
</html>
