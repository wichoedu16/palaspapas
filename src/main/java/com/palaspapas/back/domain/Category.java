package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseDomain {
    private Long id;
    private String name;
    private String description;
    private Boolean isForKitchen; // Para identificar si es para cocina o adicionales
    private Boolean isAddition;   // Para identificar si puede ser un adicional
}