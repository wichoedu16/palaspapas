package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient extends BaseDomain {
    private String name;
    private String description;
    private Category category;
    private String unitMeasure;
    private BigDecimal stock;
    private BigDecimal minimumStock;
    private BigDecimal cost;
    private Boolean isForKitchen;
    private Boolean isAddition;
}