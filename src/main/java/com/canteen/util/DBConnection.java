package com.canteen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place for the MySQL JDBC connection.
 * CHANGE DB_URL / DB_USER / DB_PASSWORD below to match your local MySQL setup.
 */

public class DBConnection {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/canteen_star";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Password"; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "MySQL JDBC Driver not found. Add mysql-connector-j jar to WEB-INF/lib.", e);
        }
    }

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
