package com.palaspapas.back.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class IngredientRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    private String description;

    @NotNull(message = "La categoría es requerida")
    private Long categoryId;

    @NotBlank(message = "La unidad de medida es requerida")
    @Size(max = 20, message = "La unidad de medida no puede exceder los 20 caracteres")
    private String unitMeasure;

    @NotNull(message = "El stock es requerido")
    @Positive(message = "El stock debe ser mayor a 0")
    private BigDecimal stock;

    @NotNull(message = "El stock mínimo es requerido")
    @Positive(message = "El stock mínimo debe ser mayor a 0")
    private BigDecimal minimumStock;

    @NotNull(message = "El costo es requerido")
    @Positive(message = "El costo debe ser mayor a 0")
    private BigDecimal cost;

    @NotNull(message = "Debe especificar si es para cocina")
    private Boolean isForKitchen;

    @NotNull(message = "Debe especificar si puede ser adicional")
    private Boolean isAddition;
}