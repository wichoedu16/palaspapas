package com.palaspapas.back.model.entity.inventory;

import com.palaspapas.back.model.entity.provider.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un ingrediente en el sistema.
 * Los ingredientes son los elementos básicos que se utilizan para preparar los productos.
 * Cada ingrediente pertenece a una categoría de inventario y mantiene un control de stock.
 */
@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentStock;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumStock;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentUnitCost;

    @Column(precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<InventoryMovement> movements = new ArrayList<>();

    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}