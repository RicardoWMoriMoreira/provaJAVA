package com.ecommerce.model;

import java.util.Date;
import java.util.UUID;

public class Product {
    private String id;
    private String name;
    private double price;
    private int    quantity;
    private String description;
    private Date   createdAt;

    public Product() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
    }

    public Product(String name, double price, int qty, String desc) {
        this();
        this.name = name; this.price = price;
        this.quantity = qty; this.description = desc;
    }

    public Product(String id, String name, double price, int qty,
                   String desc, Date createdAt) {
        this.id = id; this.name = name; this.price = price;
        this.quantity = qty; this.description = desc; this.createdAt = createdAt;
    }

    public String getId()          { return id; }
    public String getName()        { return name; }
    public double getPrice()       { return price; }
    public int    getQuantity()    { return quantity; }
    public String getDescription() { return description; }
    public Date   getCreatedAt()   { return createdAt; }

    public void setId(String id)             { this.id = id; }
    public void setName(String name)         { this.name = name; }
    public void setPrice(double price)       { this.price = price; }
    public void setQuantity(int qty)         { this.quantity = qty; }
    public void setDescription(String desc)  { this.description = desc; }

    @Override public String toString() {
        return "Product{id=" + id + ", nome='" + name + "', preço=" + price +
                ", estoque=" + quantity + ", criadoEm=" + createdAt +
                ", descrição=" + description + '}';
    }

}