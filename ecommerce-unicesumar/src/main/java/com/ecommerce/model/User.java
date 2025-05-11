package com.ecommerce.model;

import com.ecommerce.model.enums.UserRole;

import java.util.Date;
import java.util.UUID;

public class User {

    private String id;
    private String email;
    private String password;
    private String name;
    private UserRole role;
    private Date createdAt;
    private transient String cached;

    public User() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
    }

    public User(String email, String pwd, String name, UserRole role) {
        this(); this.email = email; this.password = pwd;
        this.name = name; this.role = role;
    }

    public String getId()    { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName()  { return name; }
    public UserRole getRole(){ return role; }
    public Date getCreatedAt(){ return createdAt; }

    public void setId(String id)          { this.id = id; cached = null; }
    public void setEmail(String e)        { this.email = e; cached = null; }
    public void setPassword(String p)     { this.password = p; cached = null; }
    public void setName(String n)         { this.name = n; cached = null; }
    public void setRole(UserRole r)       { this.role = r; cached = null; }
    public void setCreatedAt(Date d)      { this.createdAt = d; cached = null; }

    @Override public String toString() {
        if (cached != null) return cached;
        return cached = "User{id='" + id + "', email='" + email +
                "', name='" + name + "', role=" + role +
                ", criadoEm=" + createdAt + '}';
    }

}