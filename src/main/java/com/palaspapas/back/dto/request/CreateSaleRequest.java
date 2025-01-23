package com.palaspapas.back.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Solicitud para crear una nueva venta")
public class CreateSaleRequest {
    @NotEmpty(message = "Los detalles de la venta son requeridos")
    private List<SaleDetailRequest> details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotBlank(message = "El tipo de pago es requerido")
    private String paymentType;

    private String notes;
}