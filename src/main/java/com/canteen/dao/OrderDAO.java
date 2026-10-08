package com.canteen.dao;

import com.canteen.model.CartItem;
import com.canteen.model.Order;
import com.canteen.model.OrderItem;
import com.canteen.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /** Creates the order + its order_items in one transaction. Returns the new order id. */
    public int createOrder(Order order, List<CartItem> cartItems) throws SQLException {
        String orderSql = "INSERT INTO orders (user_id, customer_name, order_type, delivery_time, status, total_cost) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, item_name, quantity, price) VALUES (?,?,?,?,?)";

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int orderId;
            try (PreparedStatement ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getUserId());
                ps.setString(2, order.getCustomerName());
                ps.setString(3, order.getOrderType());
                ps.setString(4, order.getDeliveryTime());
                ps.setString(5, order.getStatus());
                ps.setDouble(6, order.getTotalCost());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    orderId = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                for (CartItem ci : cartItems) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, ci.getMenuItemId());
                    ps.setString(3, ci.getName());
                    ps.setInt(4, ci.getQuantity());
                    ps.setDouble(5, ci.getPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
            return orderId;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Order order = null;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) order = mapOrder(rs);
            }
        }
        if (order != null) {
            order.setItems(getItemsForOrder(orderId));
        }
        return order;
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        List<Order> orders = queryOrders("SELECT * FROM orders WHERE user_id = ? ORDER BY id DESC", userId);
        for (Order o : orders) {
            o.setItems(getItemsForOrder(o.getId()));
        }
        return orders;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY " +
                     "CASE status WHEN 'PENDING' THEN 0 WHEN 'ACCEPTED' THEN 1 ELSE 2 END, id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) orders.add(mapOrder(rs));
        }
        for (Order o : orders) {
            o.setItems(getItemsForOrder(o.getId()));
        }
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private List<Order> queryOrders(String sql, int param) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) orders.add(mapOrder(rs));
            }
        }
        return orders;
    }

    private List<OrderItem> getItemsForOrder(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setId(rs.getInt("id"));
                    oi.setOrderId(rs.getInt("order_id"));
                    oi.setMenuItemId(rs.getInt("menu_item_id"));
                    oi.setItemName(rs.getString("item_name"));
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPrice(rs.getDouble("price"));
                    items.add(oi);
                }
            }
        }
        return items;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setOrderType(rs.getString("order_type"));
        o.setDeliveryTime(rs.getString("delivery_time"));
        o.setStatus(rs.getString("status"));
        o.setTotalCost(rs.getDouble("total_cost"));
        o.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : "");
        return o;
    }
}
