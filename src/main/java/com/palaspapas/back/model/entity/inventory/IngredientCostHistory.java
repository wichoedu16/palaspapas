package com.palaspapas.back.model.entity.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingredient_cost_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientCostHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal previousCost;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal newCost;

    @Column(length = 255)
    private String reason;

    @Column(name = "change_date", nullable = false, updatable = false)
    private LocalDateTime changeDate;

    @PrePersist
    protected void onCreate() {
        changeDate = LocalDateTime.now();
    }
}