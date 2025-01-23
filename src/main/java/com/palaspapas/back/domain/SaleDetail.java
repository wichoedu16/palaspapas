package com.palaspapas.back.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetail {
    private Long id;
    private Product product;      // Puede ser null si es un adicional
    private Ingredient ingredient;  // Puede ser null si es un producto
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String notes;
    private Boolean isAdditional; // true si es un adicional/extra
    private Long parentDetailId;  // ID del detalle padre si es un adicional
}
