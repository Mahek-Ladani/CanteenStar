<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.canteen.model.CartItem" %>
<%@ page import="com.canteen.model.User" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect("index.jsp"); return; }
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    double total = 0;
    if (cart != null) { for (CartItem ci : cart) total += ci.getSubtotal(); }
    String orderType = (String) session.getAttribute("orderType");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Your Cart - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="navbar">
    <span class="brand">🍽️ CanteenStar</span>
    <span>
        Hi, <%= user.getName() %>
        <a href="MenuServlet">Back to Menu</a>
        <a href="AuthServlet?action=logout">Logout</a>
    </span>
</div>
<div class="page">
    <h2>Your Cart</h2>

    <% if ("error".equals(request.getParameter("error")) || cart == null || cart.isEmpty()) { %>
        <div class="empty-state">
            Your cart is empty.<br><br>
            <a class="btn" href="MenuServlet">Browse Menu</a>
        </div>
    <% } else { %>
        <table class="styled">
            <tr><th>Item</th><th>Price</th><th>Qty</th><th>Subtotal</th><th></th></tr>
            <% for (CartItem ci : cart) { %>
            <tr>
                <td><%= ci.getName() %></td>
                <td>₹<%= String.format("%.2f", ci.getPrice()) %></td>
                <td>
                    <form action="CartServlet" method="post" style="display:flex; gap:6px;">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="itemId" value="<%= ci.getMenuItemId() %>">
                        <input type="number" name="quantity" value="<%= ci.getQuantity() %>" min="0" style="width:60px;">
                        <button class="btn btn-small btn-outline" type="submit">Update</button>
                    </form>
                </td>
                <td>₹<%= String.format("%.2f", ci.getSubtotal()) %></td>
                <td>
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="action" value="remove">
                        <input type="hidden" name="itemId" value="<%= ci.getMenuItemId() %>">
                        <button class="btn btn-small btn-danger" type="submit">Remove</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>

        <div class="total-row">Total: ₹<%= String.format("%.2f", total) %></div>
        <p style="color:#8a8f98;">Order type: <strong><%= orderType %></strong>
            &nbsp; <a href="orderType.jsp">(change)</a></p>

        <form action="OrderServlet" method="post">
            <button class="btn btn-success" type="submit">✅ Place Order</button>
        </form>
    <% } %>
</div>
</body>
</html>
