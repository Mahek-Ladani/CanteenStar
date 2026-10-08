package com.canteen.controller;

import com.canteen.dao.OrderDAO;
import com.canteen.model.CartItem;
import com.canteen.model.Order;
import com.canteen.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/OrdersServlet")
public class OrdersServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null) {
            resp.sendRedirect("index.jsp");
            return;
        }
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect("cart.jsp?error=empty");
            return;
        }

        String orderType = (String) session.getAttribute("orderType");
        String deliveryTime = (String) session.getAttribute("deliveryTime");

        double total = 0;
        for (CartItem ci : cart) total += ci.getSubtotal();

        Order order = new Order();
        order.setUserId(user.getId());
        order.setCustomerName(user.getName());
        order.setOrderType(orderType);
        order.setDeliveryTime(deliveryTime);
        order.setStatus("PENDING");
        order.setTotalCost(total);

        try {
            OrderDAO orderDAO = new OrderDAO();
            int orderId = orderDAO.createOrder(order, cart);

            // clear cart + order type selection after placing order
            session.removeAttribute("cart");
            session.removeAttribute("orderType");
            session.removeAttribute("deliveryTime");

            resp.sendRedirect("OrderServlet?action=confirm&orderId=" + orderId);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if ("confirm".equals(action)) {
            try {
                int orderId = Integer.parseInt(req.getParameter("orderId"));
                OrderDAO orderDAO = new OrderDAO();
                Order order = orderDAO.getOrderById(orderId);
                req.setAttribute("order", order);
                req.getRequestDispatcher("orderConfirmation.jsp").forward(req, resp);
            } catch (SQLException | NumberFormatException e) {
                throw new ServletException(e);
            }
        } else {
            resp.sendRedirect("MenuServlet");
        }
    }
}
