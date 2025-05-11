package com.ecommerce.repository;

import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;
import com.ecommerce.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    public void save(User user) {
        String sql = "INSERT INTO users " +
                "(id, email, password, name, role, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getRole().name());
            pstmt.setLong  (6, user.getCreatedAt().getTime());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao salvar usuário no banco de dados", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return Optional.of(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar usuário no banco de dados", e);
        }
        return Optional.empty();
    }

    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return Optional.of(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar usuário no banco de dados", e);
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) users.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao listar usuários do banco de dados", e);
        }
        return users;
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET email = ?, password = ?, name = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getRole().name());
            pstmt.setString(5, user.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao atualizar usuário no banco de dados", e);
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao excluir usuário do banco de dados", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao verificar existência de email no banco de dados", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId      (rs.getString("id"));
        user.setEmail   (rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setName    (rs.getString("name"));
        user.setRole    (UserRole.valueOf(rs.getString("role")));
        return user;
    }

}