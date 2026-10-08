<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="page center-page">
    <div style="text-align:center;">
        <h1 style="font-size:32px;">🍽️ Welcome to CanteenStar</h1>
        <p style="color:#8a8f98; margin-bottom:32px;">Please choose how you'd like to continue</p>
        <div class="role-grid">
            <a class="role-card" href="customerLogin.jsp">
                <span class="emoji">🧑‍🎓</span>
                <h3>I'm a Customer</h3>
                <p style="color:#8a8f98; font-size:13px;">Order food, dine-in or preorder</p>
            </a>
            <a class="role-card" href="shopkeeperLogin.jsp">
                <span class="emoji">🧑‍🍳</span>
                <h3>I'm the Shopkeeper</h3>
                <p style="color:#8a8f98; font-size:13px;">Manage orders & menu</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
