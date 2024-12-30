package com.palaspapas.back.model.entity.provider;

public enum PaymentStatus {
    PENDING("Pendiente"),
    PARTIAL("Parcial"),
    COMPLETED("Completado");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}