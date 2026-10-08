<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.canteen.model.Order" %>
<%@ page import="com.canteen.model.OrderItem" %>
<%@ page import="com.canteen.model.MenuItem" %>
<%@ page import="java.util.List" %>
<%
    if (!"SHOPKEEPER".equals(session.getAttribute("role"))) { response.sendRedirect("index.jsp"); return; }
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    List<MenuItem> menuItems = (List<MenuItem>) request.getAttribute("menuItems");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Shopkeeper Dashboard - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
    <script>
        function showTab(tab) {
            document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tabs button').forEach(b => b.classList.remove('active'));
            document.getElementById(tab).classList.add('active');
            document.getElementById('btn-' + tab).classList.add('active');
        }
    </script>
</head>
<body>
<div class="navbar">
    <span class="brand">🍽️ CanteenStar &middot; Shopkeeper</span>
    <span><a href="AuthServlet?action=logout">Logout</a></span>
</div>
<div class="page">
    <div class="tabs">
        <button id="btn-orders" class="active" onclick="showTab('orders')">📋 Orders</button>
        <button id="btn-menu" onclick="showTab('menu')">🍔 Menu Management</button>
    </div>

    <div id="orders" class="tab-content active">
        <h2>Incoming Orders</h2>
        <% if (orders == null || orders.isEmpty()) { %>
            <div class="empty-state">No orders yet.</div>
        <% } else { %>
            <% for (Order o : orders) { %>
            <div class="card" style="margin-bottom:16px;">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <h3>Order #<%= o.getId() %> &middot; <%= o.getCustomerName() %> &middot; <%= o.getOrderType() %></h3>
                    <span class="badge <%= o.getStatus() %>"><%= o.getStatus() %></span>
                </div>
                <p style="color:#8a8f98; font-size:13px;">
                    Placed: <%= o.getCreatedAt() %>
                    <% if (o.getDeliveryTime() != null && !o.getDeliveryTime().isEmpty()) { %>
                        &middot; Requested time: <%= o.getDeliveryTime() %>
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

                <div style="display:flex; gap:10px;">
                    <% if ("PENDING".equals(o.getStatus())) { %>
                        <form action="ShopkeeperServlet" method="post">
                            <input type="hidden" name="action" value="accept">
                            <input type="hidden" name="orderId" value="<%= o.getId() %>">
                            <button class="btn btn-success btn-small" type="submit">Accept</button>
                        </form>
                        <form action="ShopkeeperServlet" method="post">
                            <input type="hidden" name="action" value="decline">
                            <input type="hidden" name="orderId" value="<%= o.getId() %>">
                            <button class="btn btn-danger btn-small" type="submit">Decline</button>
                        </form>
                    <% } else if ("ACCEPTED".equals(o.getStatus())) { %>
                        <form action="ShopkeeperServlet" method="post">
                            <input type="hidden" name="action" value="prepared">
                            <input type="hidden" name="orderId" value="<%= o.getId() %>">
                            <button class="btn btn-small" type="submit">Mark Prepared</button>
                        </form>
                    <% } else if ("PREPARED".equals(o.getStatus())) { %>
                        <form action="ShopkeeperServlet" method="post">
                            <input type="hidden" name="action" value="completed">
                            <input type="hidden" name="orderId" value="<%= o.getId() %>">
                            <button class="btn btn-outline btn-small" type="submit">Mark Completed</button>
                        </form>
                    <% } %>
                </div>
            </div>
            <% } %>
        <% } %>
    </div>

    <div id="menu" class="tab-content">
        <h2>Menu Management</h2>
        <table class="styled">
            <tr><th>Item</th><th>Price</th><th>Status</th><th>Action</th></tr>
            <% for (MenuItem item : menuItems) { %>
            <tr>
                <td><%= item.getName() %></td>
                <td>₹<%= String.format("%.2f", item.getPrice()) %></td>
                <td>
                    <span class="badge <%= item.isAvailable() ? "available" : "unavailable" %>">
                        <%= item.isAvailable() ? "Available" : "Not Available" %>
                    </span>
                </td>
                <td>
                    <form action="ShopkeeperServlet" method="post">
                        <input type="hidden" name="action" value="toggleAvailability">
                        <input type="hidden" name="itemId" value="<%= item.getId() %>">
                        <input type="hidden" name="currentlyAvailable" value="<%= item.isAvailable() %>">
                        <button class="btn btn-small btn-outline" type="submit">
                            Mark <%= item.isAvailable() ? "Not Available" : "Available" %>
                        </button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>
