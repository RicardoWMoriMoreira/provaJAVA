package com.ecommerce.model;

import com.ecommerce.model.enums.PaymentMethod;

import java.text.SimpleDateFormat;
import java.util.*;

public class Sale {

    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private String id;
    private String userId;
    private String userName;
    private Date   saleDate;
    private double totalValue;
    private PaymentMethod paymentMethod;
    private final List<SaleItem> items = new ArrayList<>();

    public Sale() {
        this.id = UUID.randomUUID().toString();
        this.saleDate = new Date();
    }

    public Sale(String uid, String uname, PaymentMethod pm) {
        this(); this.userId = uid; this.userName = uname; this.paymentMethod = pm;
    }

    public void calculateTotal() { totalValue = items.stream().mapToDouble(SaleItem::getTotal).sum(); }

    public void addItem(SaleItem item) {
        Objects.requireNonNull(item);
        items.add(item);
        totalValue += item.getTotal();
    }

    public void removeItem(SaleItem item) { if (items.remove(item)) totalValue -= item.getTotal(); }

    public String getId()                   { return id; }
    public String getUserId()               { return userId; }
    public String getUserName()             { return userName; }
    public Date   getSaleDate()             { return saleDate; }
    public double getTotalValue()           { return totalValue; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public List<SaleItem> getItems()        { return items; }

    public void setId(String id)                   { this.id = id; }
    public void setUserId(String u)                { this.userId = u; }
    public void setUserName(String n)              { this.userName = n; }
    public void setSaleDate(Date d)                { this.saleDate = d; }
    public void setPaymentMethod(PaymentMethod pm) { this.paymentMethod = pm; }
    public void setItems(List<SaleItem> newItems) {
        items.clear(); if (newItems != null) items.addAll(newItems); calculateTotal();
    }

    @Override public String toString() {
        return "Sale{id='" + id + "', userId='" + userId + "', userName='" +
               userName + "', saleDate=" + DF.format(saleDate) + ", total=" +
               String.format("%.2f", totalValue) + ", payment=" + paymentMethod +
               ", items=" + items + '}';
    }

}