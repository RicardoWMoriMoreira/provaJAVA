package com.ecommerce.repository;

import com.ecommerce.model.Sale;
import com.ecommerce.model.SaleItem;
import com.ecommerce.model.enums.PaymentMethod;
import com.ecommerce.util.DatabaseUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
;

public class SaleRepository {

    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final String INSERT_SALE = """
    INSERT INTO sales (id, user_id, user_name, total_value, payment_method, sale_date)
    VALUES (?, ?, ?, ?, ?, ?)
    """;

    private static final String INSERT_ITEM = """
    INSERT INTO sale_items (sale_id, product_id, product_name,
                            product_price, quantity, total, created_at)
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE_STOCK = """
    UPDATE products SET quantity = quantity - ?
    WHERE id = ? AND quantity >= ?
    """;

    public void saveWithStockUpdate(Sale sale) {

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement salePs  = conn.prepareStatement(INSERT_SALE);
                 PreparedStatement itemPs  = conn.prepareStatement(INSERT_ITEM);
                 PreparedStatement stockPs = conn.prepareStatement(UPDATE_STOCK)) {

                if (sale.getId() == null) sale.setId(UUID.randomUUID().toString());
                sale.calculateTotal();

                String saleDateStr = DF.format(sale.getSaleDate());

                salePs.setString(1, sale.getId());
                salePs.setString(2, sale.getUserId());
                salePs.setString(3, sale.getUserName());
                salePs.setDouble(4, sale.getTotalValue());
                salePs.setString(5, sale.getPaymentMethod().name());
                salePs.setString(6, saleDateStr);
                salePs.executeUpdate();

                for (SaleItem it : sale.getItems()) {

                    String createdAtStr = DF.format(it.getCreatedAt());

                    itemPs.setString (1, sale.getId());
                    itemPs.setString (2, it.getProductId());
                    itemPs.setString (3, it.getProductName());
                    itemPs.setDouble (4, it.getProductPrice());
                    itemPs.setInt    (5, it.getQuantity());
                    itemPs.setDouble (6, it.getTotal());
                    itemPs.setString (7, createdAtStr);
                    itemPs.addBatch();

                    stockPs.setInt   (1, it.getQuantity());
                    stockPs.setString(2, it.getProductId());
                    stockPs.setInt   (3, it.getQuantity());

                    if (stockPs.executeUpdate() == 0) {
                        throw new SQLException("Estoque insuficiente p/ " + it.getProductName());
                    }
                }
                itemPs.executeBatch();
                conn.commit();

            } catch (SQLException ex) {
                conn.rollback();
                throw new RuntimeException("Falha ao salvar venda: " + ex.getMessage(), ex);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao salvar venda", e);
        }
    }

    private static final String SEL_SALE    = "SELECT * FROM sales WHERE id = ?";
    private static final String SEL_ITEMS   = "SELECT * FROM sale_items WHERE sale_id = ?";
    private static final String SEL_ALL     = "SELECT * FROM sales";
    private static final String SEL_BY_USER = "SELECT * FROM sales WHERE user_id = ?";

    public Optional<Sale> findById(String id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEL_SALE)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();

            Sale sale = mapSale(rs);
            sale.setItems(Collections.singletonList((SaleItem) fetchItems(conn, sale.getId())));
            return Optional.of(sale);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar venda", e);
        }
    }

    public List<Sale> findAll()              { return findMany(SEL_ALL, null); }
    public List<Sale> findByUserId(String u) { return findMany(SEL_BY_USER, u); }

    private List<Sale> findMany(String sql, String param) {
        List<Sale> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (param != null) ps.setString(1, param);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sale s = mapSale(rs);
                s.setItems(fetchItems(conn, s.getId()));
                list.add(s);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao listar vendas", e);
        }
    }

    private List<SaleItem> fetchItems(Connection conn, String saleId) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(SEL_ITEMS)) {
            ps.setString(1, saleId);
            ResultSet rs = ps.executeQuery();
            List<SaleItem> items = new ArrayList<>();

            while (rs.next()) {
                try {
                    Date created = DF.parse(rs.getString("created_at"));
                    items.add(new SaleItem(
                            rs.getString("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("product_price"),
                            rs.getInt("quantity"),
                            created
                    ));
                } catch (Exception e) {
                    throw new SQLException("Erro ao parsear data do item", e);
                }
            }
            return items;
        }
    }

    private Sale mapSale(ResultSet rs) throws SQLException {
        try {
            Sale sale = new Sale();
            sale.setId         (rs.getString("id"));
            sale.setUserId     (rs.getString("user_id"));
            sale.setUserName   (rs.getString("user_name"));
            sale.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
            sale.setSaleDate   (DF.parse(rs.getString("sale_date")));
            return sale;
        } catch (Exception e) {
            throw new SQLException("Erro ao parsear data da venda", e);
        }
    }

}