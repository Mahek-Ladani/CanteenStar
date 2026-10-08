<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shopkeeper Login - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="page center-page">
    <div class="card" style="max-width:420px; width:100%;">
        <h2>🧑‍🍳 Shopkeeper Login</h2>
        <p style="color:#8a8f98; font-size:14px;">Default: admin / admin123</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-box"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="AuthServlet" method="post">
            <input type="hidden" name="role" value="SHOPKEEPER">
            <label>Username</label>
            <input type="text" name="username" required>

            <label>Password</label>
            <input type="password" name="password" required>

            <button class="btn" type="submit" style="width:100%;">Login →</button>
        </form>
        <p style="margin-top:18px;"><a href="index.jsp">&larr; Back</a></p>
    </div>
</div>
</body>
</html>
