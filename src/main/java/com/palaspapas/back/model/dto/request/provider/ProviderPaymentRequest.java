package com.palaspapas.back.model.dto.request.provider;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProviderPaymentRequest {
    @NotNull(message = "El monto total es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto total debe ser mayor que cero")
    private BigDecimal totalAmount;

    @NotNull(message = "El monto pagado es requerido")
    @DecimalMin(value = "0.0", message = "El monto pagado no puede ser negativo")
    private BigDecimal paidAmount;

    @NotNull(message = "La fecha de pago es requerida")
    private LocalDateTime paymentDate;

    @Size(max = 255, message = "Las notas no pueden exceder 255 caracteres")
    private String notes;
}