package com.palaspapas.back.mapper.provider;

import com.palaspapas.back.model.dto.request.provider.ProviderPaymentRequest;
import com.palaspapas.back.model.dto.response.provider.ProviderPaymentResponse;
import com.palaspapas.back.model.entity.provider.PaymentStatus;
import com.palaspapas.back.model.entity.provider.ProviderPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ProviderPaymentMapper {
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "status", expression = "java(determinePaymentStatus(request.getTotalAmount(), request.getPaidAmount()))")
    ProviderPayment toEntity(ProviderPaymentRequest request);

    @Mapping(target = "providerId", source = "provider.id")
    @Mapping(target = "providerName", source = "provider.name")
    ProviderPaymentResponse toResponse(ProviderPayment payment);

    @Named("determinePaymentStatus")
    default PaymentStatus determinePaymentStatus(BigDecimal totalAmount, BigDecimal paidAmount) {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            return PaymentStatus.PENDING;
        }
        return paidAmount.compareTo(totalAmount) >= 0 ? PaymentStatus.COMPLETED : PaymentStatus.PARTIAL;
    }
}