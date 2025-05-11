package com.ecommerce.model.enums;

public enum UserRole {
    ADMIN("Administrador"),
    CUSTOMER("Cliente");

    private final String description;

    UserRole(String description) { this.description = description; }

    public String getDescription() { return description; }

}