<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.canteen.model.Order" %>
<%@ page import="com.canteen.model.OrderItem" %>
<%@ page import="com.canteen.model.User" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect("index.jsp"); return; }
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Orders - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <span class="brand">🍽️ CanteenStar</span>
    <span>
        Hi, <%= user.getName() %>
        <a href="MenuServlet">Order More</a>
        <a href="AuthServlet?action=logout">Logout</a>
    </span>
</div>
<div class="page">
    <h2>My Order History</h2>
    <% if (orders == null || orders.isEmpty()) { %>
        <div class="empty-state">You haven't placed any orders yet.<br><br>
            <a class="btn" href="MenuServlet">Browse Menu</a>
        </div>
    <% } else { %>
        <% for (Order o : orders) { %>
        <div class="card" style="margin-bottom:16px;">
            <div style="display:flex; justify-content:space-between; align-items:center;">
                <h3>Order #<%= o.getId() %> &middot; <%= o.getOrderType() %></h3>
                <span class="badge <%= o.getStatus() %>"><%= o.getStatus() %></span>
            </div>
            <p style="color:#8a8f98; font-size:13px;">
                Placed: <%= o.getCreatedAt() %>
                <% if (o.getDeliveryTime() != null && !o.getDeliveryTime().isEmpty()) { %>
                    &middot; Time: <%= o.getDeliveryTime() %>
                <% } %>
            </p>
            <table class="styled">
                <tr><th>Item</th><th>Qty</th><th>Subtotal</th></tr>
                <% for (OrderItem oi : o.getItems()) { %>
                <tr>
                    <td><%= oi.getItemName() %></td>
                    <td><%= oi.getQuantity() %></td>
                    <td>₹<%= String.format("%.2f", oi.getSubtotal()) %></td>
                </tr>
                <% } %>
            </table>
            <div class="total-row">Total: ₹<%= String.format("%.2f", o.getTotalCost()) %></div>
        </div>
        <% } %>
    <% } %>
</div>
</body>
</html>
