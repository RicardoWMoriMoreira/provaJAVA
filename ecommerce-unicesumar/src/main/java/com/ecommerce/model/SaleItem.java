package com.ecommerce.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SaleItem {

    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private String productId;
    private String productName;
    private double productPrice;
    private int    quantity;
    private Date   createdAt;

    public SaleItem() { this.createdAt = new Date(); }

    public SaleItem(String id, String name, double price, int qty) {
        this(); this.productId = id; this.productName = name;
        this.productPrice = price; this.quantity = qty;
    }

    public SaleItem(String id, String name, double price, int qty, Date created) {
        this.productId = id; this.productName = name;
        this.productPrice = price; this.quantity = qty; this.createdAt = created;
    }

    public double getTotal()       { return productPrice * quantity; }
    public String getProductId()   { return productId; }
    public String getProductName() { return productName; }
    public double getProductPrice(){ return productPrice; }
    public int    getQuantity()    { return quantity; }
    public Date   getCreatedAt()   { return createdAt; }

    public void setQuantity(int q) { this.quantity = q; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleItem s)) return false;
        return Objects.equals(productId, s.productId);
    }
    @Override public int hashCode() { return Objects.hash(productId); }

    @Override public String toString() {
        return "SaleItem{productId='" + productId + "', name='" + productName +
               "', price=" + productPrice + ", qty=" + quantity +
               ", total=" + String.format("%.2f", getTotal()) +
               ", criadoEm=" + DF.format(createdAt) + '}';
    }

}