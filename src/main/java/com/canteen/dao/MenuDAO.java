package com.canteen.dao;

import com.canteen.model.MenuItem;
import com.canteen.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public List<MenuItem> getAllMenuItems() throws SQLException {
        return queryList("SELECT * FROM menu_items ORDER BY id");
    }

    public List<MenuItem> getAvailableMenuItems() throws SQLException {
        return queryList("SELECT * FROM menu_items WHERE available = TRUE ORDER BY id");
    }

    private List<MenuItem> queryList(String sql) throws SQLException {
        List<MenuItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public MenuItem getMenuItemById(int id) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean updateAvailability(int id, boolean available) throws SQLException {
        String sql = "UPDATE menu_items SET available = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public MenuItem addMenuItem(MenuItem item) throws SQLException {
        String sql = "INSERT INTO menu_items (name, description, price, available, image_url) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getPrice());
            ps.setBoolean(4, item.isAvailable());
            ps.setString(5, item.getImageUrl());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) item.setId(keys.getInt(1));
            }
        }
        return item;
    }

    private MenuItem mapRow(ResultSet rs) throws SQLException {
        return new MenuItem(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getBoolean("available"),
                rs.getString("image_url")
        );
    }
}
