package com.palaspapas.back.model.entity.sale;

public enum OrderStatus {
    PENDING("Pendiente"),
    IN_PROGRESS("En Preparación"),
    READY("Listo"),
    DELIVERED("Entregado"),
    CANCELLED("Cancelado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
