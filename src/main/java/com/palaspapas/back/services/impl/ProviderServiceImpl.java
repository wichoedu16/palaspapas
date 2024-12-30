package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.common.DuplicateResourceException;
import com.palaspapas.back.exceptions.common.ResourceNotFoundException;
import com.palaspapas.back.exceptions.inventory.InvalidMovementException;
import com.palaspapas.back.mapper.provider.ProviderMapper;
import com.palaspapas.back.mapper.provider.ProviderPaymentMapper;
import com.palaspapas.back.model.dto.request.provider.ProviderPaymentRequest;
import com.palaspapas.back.model.dto.request.provider.ProviderRequest;
import com.palaspapas.back.model.dto.response.provider.ProviderPaymentResponse;
import com.palaspapas.back.model.dto.response.provider.ProviderResponse;
import com.palaspapas.back.model.entity.provider.Provider;
import com.palaspapas.back.model.entity.provider.ProviderPayment;
import com.palaspapas.back.repository.provider.ProviderPaymentRepository;
import com.palaspapas.back.repository.provider.ProviderRepository;
import com.palaspapas.back.services.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepository;
    private final ProviderPaymentRepository paymentRepository;
    private final ProviderMapper providerMapper;
    private final ProviderPaymentMapper paymentMapper;

    @Override
    public ProviderResponse create(ProviderRequest request) {
        log.debug("Creando nuevo proveedor: {}", request.getName());

        if (request.getTaxId() != null && providerRepository.existsByTaxId(request.getTaxId())) {
            throw new DuplicateResourceException("Ya existe un proveedor con el ID fiscal: " + request.getTaxId());
        }

        Provider provider = providerMapper.toEntity(request);
        provider = providerRepository.save(provider);

        log.info("Proveedor creado con ID: {}", provider.getId());
        return providerMapper.toResponse(provider);
    }

    @Override
    public ProviderResponse update(Long id, ProviderRequest request) {
        log.debug("Actualizando proveedor ID: {}", id);

        Provider provider = findProviderById(id);

        if (request.getTaxId() != null && !request.getTaxId().equals(provider.getTaxId())
                && providerRepository.existsByTaxId(request.getTaxId())) {
            throw new DuplicateResourceException("Ya existe un proveedor con el ID fiscal: " + request.getTaxId());
        }

        updateProviderFields(provider, request);
        provider = providerRepository.save(provider);

        log.info("Proveedor actualizado con ID: {}", id);
        return providerMapper.toResponse(provider);
    }

    @Override
    public ProviderResponse findById(Long id) {
        return null;
    }

    @Override
    public List<ProviderResponse> findAll() {
        return null;
    }

    @Override
    public ProviderPaymentResponse registerPayment(Long providerId, ProviderPaymentRequest request) {
        log.debug("Registrando pago para proveedor ID: {}", providerId);

        Provider provider = findProviderById(providerId);

        validatePaymentAmount(request.getTotalAmount(), request.getPaidAmount());

        ProviderPayment payment = paymentMapper.toEntity(request);
        payment.setProvider(provider);
        payment = paymentRepository.save(payment);

        log.info("Pago registrado con ID: {} para proveedor ID: {}", payment.getId(), providerId);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<ProviderPaymentResponse> getPaymentHistory(Long providerId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPendingAmount(Long providerId) {
        findProviderById(providerId); // Validar que existe el proveedor
        return paymentRepository.findTotalPendingAmount(providerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderResponse> search(String searchTerm, Pageable pageable) {
        return providerRepository.search(searchTerm, pageable)
                .map(providerMapper::toResponse);
    }

    @Override
    public void delete(Long id) {

    }

    private void validatePaymentAmount(BigDecimal totalAmount, BigDecimal paidAmount) {
        if (paidAmount.compareTo(totalAmount) > 0) {
            throw new InvalidMovementException("El monto pagado no puede ser mayor al monto total");
        }
    }

    private void updateProviderFields(Provider provider, ProviderRequest request) {
        provider.setName(request.getName());
        provider.setTaxId(request.getTaxId());
        provider.setContactPhone(request.getContactPhone());
        provider.setEmail(request.getEmail());
    }

    private Provider findProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Proveedor no encontrado con ID: %d", id)));
    }
}