package com.canteen.controller;

import com.canteen.dao.MenuDAO;
import com.canteen.model.MenuItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/MenuServlet")
public class MenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        // Coming from orderType.jsp with the chosen order type / delivery time
        String orderType = req.getParameter("orderType");
        String deliveryTime = req.getParameter("deliveryTime");
        if (orderType != null && !orderType.isEmpty()) {
            session.setAttribute("orderType", orderType);
            session.setAttribute("deliveryTime", deliveryTime);
        }

        if (session.getAttribute("orderType") == null) {
            resp.sendRedirect("orderType.jsp");
            return;
        }

        try {
            MenuDAO menuDAO = new MenuDAO();
            List<MenuItem> items = menuDAO.getAvailableMenuItems();
            req.setAttribute("menuItems", items);
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
