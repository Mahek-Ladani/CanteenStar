<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.canteen.model.MenuItem" %>
<%@ page import="com.canteen.model.User" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect("index.jsp"); return; }
    List<MenuItem> menuItems = (List<MenuItem>) request.getAttribute("menuItems");
    List cart = (List) session.getAttribute("cart");
    int cartCount = (cart == null) ? 0 : cart.size();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Menu - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <span class="brand">🍽️ CanteenStar</span>
    <span>
        Hi, <%= user.getName() %>
        <a href="CartServlet">🛒 Cart (<%= cartCount %>)</a>
        <a href="OrdersServlet">My Orders</a>
        <a href="AuthServlet?action=logout">Logout</a>
    </span>
</div>
<div class="page">
    <h2>Menu</h2>
    <% if (menuItems == null || menuItems.isEmpty()) { %>
        <div class="empty-state">No items available right now. Please check back later.</div>
    <% } else { %>
    <div class="menu-grid">
        <% for (MenuItem item : menuItems) { %>
        <div class="menu-item-card">
            <h3><%= item.getName() %></h3>
            <div class="desc"><%= item.getDescription() != null ? item.getDescription() : "" %></div>
            <div class="price">₹<%= String.format("%.2f", item.getPrice()) %></div>
            <form action="CartServlet" method="post">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="itemId" value="<%= item.getId() %>">
                <input type="hidden" name="itemName" value="<%= item.getName() %>">
                <input type="hidden" name="itemPrice" value="<%= item.getPrice() %>">
                <div class="qty-row">
                    <input type="number" name="quantity" value="1" min="1" max="20">
                    <button class="btn btn-small" type="submit" style="flex:1;">Add to Cart</button>
                </div>
            </form>
        </div>
        <% } %>
    </div>
    <% } %>
</div>
</body>
</html>
