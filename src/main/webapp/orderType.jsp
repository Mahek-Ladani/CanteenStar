<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.canteen.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect("index.jsp"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Choose Order Type - CanteenStar</title>
    <link rel="stylesheet" href="css/style.css">
    <script>
        function selectType(el, value) {
            document.querySelectorAll('.order-type-option').forEach(o => o.style.borderColor = '#e4e6eb');
            el.style.borderColor = '#ff7a30';
            document.getElementById('orderType').value = value;
            document.getElementById('timeField').style.display =
                (value === 'PICKUP' || value === 'PREORDER') ? 'block' : 'none';
        }
    </script>
</head>
<body>
<div class="navbar">
    <span class="brand">🍽️ CanteenStar</span>
    <span>Hi, <%= user.getName() %> &nbsp; <a href="AuthServlet?action=logout">Logout</a></span>
</div>
<div class="page">
    <div class="card">
        <h2>How would you like your order?</h2>
        <form action="MenuServlet" method="get">
            <input type="hidden" id="orderType" name="orderType" value="">
            <div class="order-type-grid">
                <div class="order-type-option" onclick="selectType(this,'DINE_IN')">
                    <span class="emoji">🍴</span>
                    <strong>Dine In</strong>
                </div>
                <div class="order-type-option" onclick="selectType(this,'PICKUP')">
                    <span class="emoji">🥡</span>
                    <strong>Pick Away</strong>
                </div>
                <div class="order-type-option" onclick="selectType(this,'PREORDER')">
                    <span class="emoji">⏰</span>
                    <strong>Preorder</strong>
                </div>
            </div>

            <div id="timeField" style="display:none; max-width:320px;">
                <label>Preferred Delivery / Pickup Time</label>
                <input type="datetime-local" name="deliveryTime">
            </div>

            <button class="btn" type="submit" onclick="return validateSelection();">Continue to Menu →</button>
        </form>
    </div>
</div>
<script>
    function validateSelection() {
        if (!document.getElementById('orderType').value) {
            alert('Please select an order type.');
            return false;
        }
        return true;
    }
</script>
</body>
</html>
