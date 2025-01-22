package com.palaspapas.back.persistence.entities;


import com.palaspapas.back.persistence.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "unit_measure", nullable = false, length = 20)
    private String unitMeasure;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal stock;

    @Column(name = "minimum_stock", nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumStock;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "is_for_kitchen", nullable = false)
    private Boolean isForKitchen;

    @Column(name = "is_addition", nullable = false)
    private Boolean isAddition;
}
