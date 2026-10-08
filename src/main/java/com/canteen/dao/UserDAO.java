package com.canteen.dao;

import com.canteen.model.User;
import com.canteen.util.DBConnection;

import java.sql.*;

public class UserDAO {

    public User findByContact(String contactNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE contact_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contactNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public User createUser(String name, String email, String contactNumber, String role) throws SQLException {
        String sql = "INSERT INTO users (name, email, contact_number, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, contactNumber);
            ps.setString(4, role);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new User(keys.getInt(1), name, email, contactNumber, role);
                }
            }
        }
        return null;
    }

    /** Finds an existing customer by contact number, or creates a new one. */
    public User findOrCreateCustomer(String name, String email, String contactNumber) throws SQLException {
        User existing = findByContact(contactNumber);
        if (existing != null) {
            // keep the stored name/email fresh
            updateProfile(existing.getId(), name, email);
            existing.setName(name);
            existing.setEmail(email);
            return existing;
        }
        return createUser(name, email, contactNumber, "CUSTOMER");
    }

    private void updateProfile(int id, String name, String email) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("contact_number"),
                rs.getString("role")
        );
    }
}
