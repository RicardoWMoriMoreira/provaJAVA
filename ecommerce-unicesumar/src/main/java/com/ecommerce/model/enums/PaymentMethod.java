package com.ecommerce.model.enums;

public enum PaymentMethod {
    CREDIT_CARD("Cartão de Crédito"),
    BOLETO("Boleto Bancário"),
    PIX("PIX");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethod fromInt(int option) {
        return switch (option) {
            case 1 -> CREDIT_CARD;
            case 2 -> BOLETO;
            case 3 -> PIX;
            default -> throw new IllegalArgumentException("Opção inválida");
        };
    }
}