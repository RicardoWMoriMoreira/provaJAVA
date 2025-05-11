package com.ecommerce.util;

import java.sql.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:sqlite:ecommerce.db";
    static { initializeDatabase(); }

    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL); }

    public static void initializeDatabase() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {

            st.executeUpdate("""
    CREATE TABLE IF NOT EXISTS users(
        id TEXT PRIMARY KEY,email TEXT UNIQUE NOT NULL,
        password TEXT NOT NULL,name TEXT NOT NULL,
        role TEXT NOT NULL,created_at INTEGER NOT NULL);
    """);

            st.executeUpdate("""
    CREATE TABLE IF NOT EXISTS products(
        id TEXT PRIMARY KEY,name TEXT NOT NULL,
        price REAL NOT NULL,quantity INTEGER NOT NULL CHECK(quantity>=0),
        description TEXT,created_at INTEGER NOT NULL);
    """);

            /* ---------- SALE / SALE_ITEMS COM DATA EM DD/MM/AAAA HH:MM ---------- */
            st.executeUpdate("""
    CREATE TABLE IF NOT EXISTS sales(
        id TEXT PRIMARY KEY,user_id TEXT NOT NULL,
        user_name TEXT NOT NULL,total_value REAL NOT NULL,
        payment_method TEXT NOT NULL,sale_date TEXT NOT NULL,
        FOREIGN KEY(user_id) REFERENCES users(id));
    """);

            st.executeUpdate("""
    CREATE TABLE IF NOT EXISTS sale_items(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        sale_id TEXT NOT NULL,product_id TEXT NOT NULL,
        product_name TEXT NOT NULL,product_price REAL NOT NULL,
        quantity INTEGER NOT NULL,total REAL NOT NULL,
        created_at TEXT NOT NULL,
        FOREIGN KEY(sale_id) REFERENCES sales(id),
        FOREIGN KEY(product_id) REFERENCES products(id));
    """);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

}