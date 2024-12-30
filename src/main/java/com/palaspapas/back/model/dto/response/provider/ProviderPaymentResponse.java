package com.palaspapas.back.model.dto.response.provider;

import com.palaspapas.back.model.entity.provider.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProviderPaymentResponse {
    private Long id;
    private Long providerId;
    private String providerName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private LocalDateTime paymentDate;
    private PaymentStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
