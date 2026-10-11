<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.grocery.payment.model.Order, com.grocery.payment.util.HtmlUtil" %>
<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    String q = (String) request.getAttribute("q");
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Orders</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <strong>Online Grocery - Orders</strong>
    <span><a href="checkout.jsp">Checkout</a><a href="orders">My Orders</a></span>
</header>

<div class="box wide">
    <h2>Orders</h2>
    <form action="orders" method="get" style="display:flex;gap:8px">
        <input type="text" name="q" placeholder="Search by Order ID or customer name" value="<%= HtmlUtil.esc(q) %>">
        <button type="submit" style="margin-top:4px">Search</button>
    </form>

    <table style="margin-top:16px">
        <tr><th>Order ID</th><th>Customer</th><th>City</th><th>Payment</th><th>Total (Rs.)</th><th>Status</th><th>Actions</th></tr>
        <% if (orders == null || orders.isEmpty()) { %>
            <tr><td colspan="7">No orders found.</td></tr>
        <% } else { for (Order o : orders) { %>
            <tr>
                <td><%= o.getOrderId() %></td>
                <td><%= HtmlUtil.esc(o.getCustomerName()) %></td>
                <td><%= HtmlUtil.esc(o.getCity()) %></td>
                <td><%= o.getPaymentMethod() %></td>
                <td><%= String.format("%.2f", o.getTotal()) %></td>
                <td><%= o.getStatus() %></td>
                <td>
                    <a href="order?id=<%= o.getOrderId() %>">View</a> |
                    <a href="updateOrder?id=<%= o.getOrderId() %>">Edit</a> |
                    <form action="deleteOrder" method="post" style="display:inline"
                          onsubmit="return confirm('Cancel this order?');">
                        <input type="hidden" name="id" value="<%= o.getOrderId() %>">
                        <button type="submit" class="btn-red" style="padding:3px 8px;margin:0;width:auto">Cancel</button>
                    </form>
                </td>
            </tr>
        <% } } %>
    </table>
</div>
</body>
</html>
