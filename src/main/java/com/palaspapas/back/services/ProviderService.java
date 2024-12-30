package com.palaspapas.back.services;

import com.palaspapas.back.model.dto.request.provider.ProviderPaymentRequest;
import com.palaspapas.back.model.dto.request.provider.ProviderRequest;
import com.palaspapas.back.model.dto.response.provider.ProviderPaymentResponse;
import com.palaspapas.back.model.dto.response.provider.ProviderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProviderService {
    ProviderResponse create(ProviderRequest request);
    ProviderResponse update(Long id, ProviderRequest request);
    ProviderResponse findById(Long id);
    List<ProviderResponse> findAll();
    Page<ProviderResponse> search(String searchTerm, Pageable pageable);
    void delete(Long id);
    ProviderPaymentResponse registerPayment(Long providerId, ProviderPaymentRequest request);
    List<ProviderPaymentResponse> getPaymentHistory(Long providerId);
    BigDecimal getPendingAmount(Long providerId);
}