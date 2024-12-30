package com.palaspapas.back.model.dto.request.dish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
public class CategoryRequest {
    @NotBlank(message = "El nombre de la categoría es requerido")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
    private String description;

    private Long parentId;

    private Integer displayOrder;
}