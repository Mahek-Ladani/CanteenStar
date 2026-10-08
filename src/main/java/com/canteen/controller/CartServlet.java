package com.canteen.controller;

import com.canteen.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("cart.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            String name = req.getParameter("itemName");
            double price = Double.parseDouble(req.getParameter("itemPrice"));
            int qty = parseQtyOrDefault(req.getParameter("quantity"), 1);

            CartItem existing = findItem(cart, itemId);
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + qty);
            } else {
                cart.add(new CartItem(itemId, name, price, qty));
            }
            resp.sendRedirect("MenuServlet");
            return;

        } else if ("update".equals(action)) {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            int qty = parseQtyOrDefault(req.getParameter("quantity"), 1);
            CartItem existing = findItem(cart, itemId);
            if (existing != null) {
                if (qty <= 0) cart.remove(existing);
                else existing.setQuantity(qty);
            }

        } else if ("remove".equals(action)) {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            CartItem existing = findItem(cart, itemId);
            if (existing != null) cart.remove(existing);

        } else if ("clear".equals(action)) {
            cart.clear();
        }

        resp.sendRedirect("CartServlet");
    }

    private CartItem findItem(List<CartItem> cart, int itemId) {
        for (CartItem ci : cart) {
            if (ci.getMenuItemId() == itemId) return ci;
        }
        return null;
    }

    private int parseQtyOrDefault(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}
