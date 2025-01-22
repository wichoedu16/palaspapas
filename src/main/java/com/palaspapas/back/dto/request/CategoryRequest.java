package com.palaspapas.back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    private String description;

    @NotNull(message = "Debe especificar si la categoría es para cocina")
    private Boolean isForKitchen;

    @NotNull(message = "Debe especificar si la categoría puede ser un adicional")
    private Boolean isAddition;
}
