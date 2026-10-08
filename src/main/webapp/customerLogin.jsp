<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Login - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="page center-page">
    <div class="card" style="max-width:420px; width:100%;">
        <h2>🧑‍🎓 Customer Login</h2>
        <p style="color:#8a8f98; font-size:14px;">Enter your details to continue</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-box"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="AuthServlet" method="post">
            <input type="hidden" name="role" value="CUSTOMER">
            <label>Full Name</label>
            <input type="text" name="name" required>

            <label>Email</label>
            <input type="email" name="email" required>

            <label>Contact Number</label>
            <input type="tel" name="contact" pattern="[0-9]{10}" placeholder="10 digit number" required>

            <button class="btn" type="submit" style="width:100%;">Continue →</button>
        </form>
        <p style="margin-top:18px;"><a href="index.jsp">&larr; Back</a></p>
    </div>
</div>
</body>
</html>
