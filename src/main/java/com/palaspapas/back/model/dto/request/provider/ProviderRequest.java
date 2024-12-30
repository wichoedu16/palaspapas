package com.palaspapas.back.model.dto.request.provider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @Size(max = 20, message = "El ID fiscal no puede exceder 20 caracteres")
    private String taxId;

    @Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "Número de teléfono inválido")
    private String contactPhone;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;
}