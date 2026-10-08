package com.canteen.controller;

import com.canteen.dao.UserDAO;
import com.canteen.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {

    // Simple hardcoded shopkeeper credentials. Change these, or move to DB later.
    private static final String SHOP_USERNAME = "admin";
    private static final String SHOP_PASSWORD = "admin123";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String role = req.getParameter("role");
        HttpSession session = req.getSession();

        if ("CUSTOMER".equals(role)) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String contact = req.getParameter("contact");

            if (isBlank(name) || isBlank(email) || isBlank(contact)) {
                req.setAttribute("error", "Please fill in all fields.");
                req.getRequestDispatcher("customerLogin.jsp").forward(req, resp);
                return;
            }

            try {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.findOrCreateCustomer(name.trim(), email.trim(), contact.trim());
                session.setAttribute("user", user);
                session.setAttribute("role", "CUSTOMER");
                resp.sendRedirect("orderType.jsp");
            } catch (SQLException e) {
                req.setAttribute("error", "Database error: " + e.getMessage());
                req.getRequestDispatcher("customerLogin.jsp").forward(req, resp);
            }

        } else if ("SHOPKEEPER".equals(role)) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            if (SHOP_USERNAME.equals(username) && SHOP_PASSWORD.equals(password)) {
                session.setAttribute("role", "SHOPKEEPER");
                session.setAttribute("shopUsername", username);
                resp.sendRedirect("ShopkeeperServlet");
            } else {
                req.setAttribute("error", "Invalid shopkeeper credentials.");
                req.getRequestDispatcher("shopkeeperLogin.jsp").forward(req, resp);
            }
        } else {
            resp.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // logout support: /AuthServlet?action=logout
        if ("logout".equals(req.getParameter("action"))) {
            req.getSession().invalidate();
        }
        resp.sendRedirect("index.jsp");
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
