<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.canteen.model.Order" %>
<%@ page import="com.canteen.model.OrderItem" %>
<%
    Order order = (Order) request.getAttribute("order");
    if (order == null) { response.sendRedirect("MenuServlet"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmed - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <span class="brand">🍽️ CanteenStar</span>
    <span><a href="MenuServlet">Order More</a> <a href="OrdersServlet">My Orders</a></span>
</div>
<div class="page">
    <div class="card" style="text-align:center;">
        <h1 style="color:#2ecc71;">✅ Order Placed!</h1>
        <p style="color:#8a8f98;">Order #<%= order.getId() %> &middot; Status:
            <span class="badge <%= order.getStatus() %>"><%= order.getStatus() %></span>
        </p>
        <p>Type: <strong><%= order.getOrderType() %></strong>
            <% if (order.getDeliveryTime() != null && !order.getDeliveryTime().isEmpty()) { %>
                &middot; Time: <strong><%= order.getDeliveryTime() %></strong>
            <% } %>
        </p>

        <table class="styled" style="margin-top:20px; text-align:left;">
            <tr><th>Item</th><th>Qty</th><th>Subtotal</th></tr>
            <% for (OrderItem oi : order.getItems()) { %>
            <tr>
                <td><%= oi.getItemName() %></td>
                <td><%= oi.getQuantity() %></td>
                <td>₹<%= String.format("%.2f", oi.getSubtotal()) %></td>
            </tr>
            <% } %>
        </table>
        <div class="total-row">Total: ₹<%= String.format("%.2f", order.getTotalCost()) %></div>

        <p style="color:#8a8f98; margin-top:20px;">The shopkeeper will accept your order shortly.
        You'll see live status in "My Orders".</p>
        <a class="btn" href="OrdersServlet">View My Orders</a>
        <a class="btn btn-outline" href="MenuServlet">Order More</a>
    </div>
</div>
</body>
</html>
