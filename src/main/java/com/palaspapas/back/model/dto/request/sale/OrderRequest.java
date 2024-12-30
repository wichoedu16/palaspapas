package com.palaspapas.back.model.dto.request.sale;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderRequest {
    private String customerName;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "Número de teléfono inválido")
    private String customerPhone;

    @NotEmpty(message = "Debe incluir al menos un detalle en la orden")
    private List<OrderDetailRequest> details;

    private BigDecimal discount;
}
