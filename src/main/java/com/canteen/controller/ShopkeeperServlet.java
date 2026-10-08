package com.canteen.controller;

import com.canteen.dao.MenuDAO;
import com.canteen.dao.OrderDAO;
import com.canteen.model.MenuItem;
import com.canteen.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ShopkeeperServlet")
public class ShopkeeperServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (!"SHOPKEEPER".equals(session.getAttribute("role"))) {
            resp.sendRedirect("index.jsp");
            return;
        }

        try {
            OrderDAO orderDAO = new OrderDAO();
            MenuDAO menuDAO = new MenuDAO();
            List<Order> orders = orderDAO.getAllOrders();
            List<MenuItem> menuItems = menuDAO.getAllMenuItems();
            req.setAttribute("orders", orders);
            req.setAttribute("menuItems", menuItems);
            req.getRequestDispatcher("shopkeeperDashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (!"SHOPKEEPER".equals(session.getAttribute("role"))) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String action = req.getParameter("action");

        try {
            if ("accept".equals(action)) {
                updateStatus(req, "ACCEPTED");
            } else if ("decline".equals(action)) {
                updateStatus(req, "DECLINED");
            } else if ("prepared".equals(action)) {
                updateStatus(req, "PREPARED");
            } else if ("completed".equals(action)) {
                updateStatus(req, "COMPLETED");
            } else if ("toggleAvailability".equals(action)) {
                int itemId = Integer.parseInt(req.getParameter("itemId"));
                boolean currentlyAvailable = Boolean.parseBoolean(req.getParameter("currentlyAvailable"));
                new MenuDAO().updateAvailability(itemId, !currentlyAvailable);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect("ShopkeeperServlet");
    }

    private void updateStatus(HttpServletRequest req, String status) throws SQLException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        new OrderDAO().updateOrderStatus(orderId, status);
    }
}
