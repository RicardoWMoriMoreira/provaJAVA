package com.ecommerce.repository;

import com.ecommerce.model.Product;
import com.ecommerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProductRepository {

    private static final String TABLE = "products";

    public void save(Product product) {
        final String sql = "INSERT INTO " + TABLE +
                " (id, name, price, quantity, description, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt   (4, product.getQuantity());
            ps.setString(5, product.getDescription());
            ps.setLong  (6, product.getCreatedAt().getTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao salvar produto", e);
        }
    }

    public Optional<Product> findById(String id) {
        final String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(map(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar produto", e);
        }
    }

    public List<Product> findAll() {
        final String sql = "SELECT * FROM " + TABLE;
        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao listar produtos", e);
        }
    }

    public List<Product> findByName(String name) {
        final String sql = "SELECT * FROM " + TABLE + " WHERE name LIKE ?";
        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar produtos", e);
        }
    }

    public boolean update(Product product) {
        final String sql = "UPDATE " + TABLE +
                " SET name = ?, price = ?, quantity = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt   (3, product.getQuantity());
            ps.setString(4, product.getDescription());
            ps.setString(5, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao atualizar produto", e);
        }
    }

    public boolean updateQuantity(String id, int newQuantity) {
        if (newQuantity < 0) throw new IllegalArgumentException("Quantidade negativa");

        final String sql = "UPDATE " + TABLE + " SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, newQuantity);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao atualizar quantidade", e);
        }
    }

    public boolean decrementStock(String id, int amount) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection()) {
            return decrementStockTx(conn, id, amount);
        }
    }

    public boolean decrementStockTx(Connection conn, String id, int amount) throws SQLException {
        if (amount <= 0) throw new IllegalArgumentException("amount deve ser > 0");

        final String sql = "UPDATE " + TABLE +
                " SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, amount);
            ps.setString(2, id);
            ps.setInt   (3, amount);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String id) {
        final String sql = "DELETE FROM " + TABLE + " WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao excluir produto", e);
        }
    }

    public List<Product> findTopSellingProducts() {
        final String sql = """
            SELECT p.*, IFNULL(SUM(si.quantity),0) AS sold
            FROM products p
            LEFT JOIN sale_items si ON si.product_id = p.id
            GROUP BY p.id
            ORDER BY sold DESC
            LIMIT 10
            """;

        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar top vendidos", e);
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt   ("quantity"),
                rs.getString("description"),
                new Date(rs.getLong("created_at"))
        );
    }

}