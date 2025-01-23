package com.palaspapas.back.service.impl;

import com.palaspapas.back.domain.Sale;
import com.palaspapas.back.domain.SaleDetail;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.exception.NotFoundException;
import com.palaspapas.back.persistence.entities.SaleDetailEntity;
import com.palaspapas.back.persistence.entities.SaleEntity;
import com.palaspapas.back.persistence.entities.UserEntity;
import com.palaspapas.back.persistence.mappers.ISaleMapper;
import com.palaspapas.back.persistence.repositories.IIngredientRepository;
import com.palaspapas.back.persistence.repositories.IProductRepository;
import com.palaspapas.back.persistence.repositories.ISaleRepository;
import com.palaspapas.back.service.interfaces.ISaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SaleServiceImpl implements ISaleService {

    private final ISaleRepository saleRepository;
    private final IProductRepository productRepository;
    private final IIngredientRepository ingredientRepository;
    private final ISaleMapper saleMapper;

    @Override
    public Sale create(Sale sale) {
        log.debug("Creando nueva venta");
        validateSale(sale);
        calculateTotals(sale);

        sale.setSaleStatus("PENDING");
        sale.setSaleDate(LocalDateTime.now());
        sale.setCreatedBy(getCurrentUserId());

        SaleEntity entity = saleMapper.toEntity(sale);
        setupSaleDetails(entity);

        SaleEntity savedEntity = saleRepository.save(entity);
        log.info("Venta creada con ID: {}", savedEntity.getId());

        return saleMapper.toDomain(savedEntity);
    }

    @Override
    public Sale findById(Long id) {
        return saleMapper.toDomain(findEntityById(id));
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll().stream()
                .map(saleMapper::toDomain)
                .toList();
    }

    @Override
    public Sale approve(Long id) {
        log.debug("Aprobando venta ID: {}", id);
        SaleEntity entity = findEntityById(id);

        validateSaleForApproval(entity);
        validateStock(entity);
        updateStock(entity);

        entity.setSaleStatus("COMPLETED");
        entity.setApprovedBy(getCurrentUserId());

        SaleEntity savedEntity = saleRepository.save(entity);
        log.info("Venta aprobada exitosamente");

        return saleMapper.toDomain(savedEntity);
    }

    @Override
    public Sale cancel(Long id) {
        log.debug("Cancelando venta ID: {}", id);
        SaleEntity entity = findEntityById(id);

        if (!"PENDING".equals(entity.getSaleStatus())) {
            throw new BusinessException("Solo se pueden cancelar ventas pendientes");
        }

        entity.setSaleStatus("CANCELLED");
        SaleEntity savedEntity = saleRepository.save(entity);
        log.info("Venta cancelada exitosamente");

        return saleMapper.toDomain(savedEntity);
    }

    @Override
    public Sale applyDiscount(Long id, BigDecimal discount) {
        log.debug("Aplicando descuento a venta ID: {}", id);
        SaleEntity entity = findEntityById(id);

        if (!"PENDING".equals(entity.getSaleStatus())) {
            throw new BusinessException("Solo se pueden aplicar descuentos a ventas pendientes");
        }

        if (discount.compareTo(entity.getTotal()) > 0) {
            throw new BusinessException("El descuento no puede ser mayor al total");
        }

        entity.setDiscount(discount);
        entity.setTotal(entity.getSubtotal().subtract(discount));

        SaleEntity savedEntity = saleRepository.save(entity);
        log.info("Descuento aplicado exitosamente");

        return saleMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Buscando ventas entre {} y {}", startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new BusinessException("La fecha inicial no puede ser posterior a la fecha final");
        }

        return saleRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(saleMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> findByStatus(String status) {
        log.debug("Buscando ventas con estado: {}", status);

        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException("El estado no puede estar vacío");
        }

        return saleRepository.findBySaleStatus(status)
                .stream()
                .map(saleMapper::toDomain)
                .toList();
    }

    private SaleEntity findEntityById(Long id) {
        return saleRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada con ID: " + id));
    }

    private void validateSale(Sale sale) {
        if (sale.getDetails() == null || sale.getDetails().isEmpty()) {
            throw new BusinessException("La venta debe tener al menos un detalle");
        }

        sale.getDetails().forEach(this::validateSaleDetail);
    }

    private void validateSaleDetail(SaleDetail detail) {
        if (detail.getIsAdditional()) {
            validateAdditionalDetail(detail);
        } else {
            validateMainDetail(detail);
        }
    }

    private void validateMainDetail(SaleDetail detail) {
        if (detail.getProduct() == null || detail.getProduct().getId() == null) {
            throw new BusinessException("El detalle debe tener un producto asociado");
        }
        validateProduct(detail.getProduct().getId());
    }

    private void validateAdditionalDetail(SaleDetail detail) {
        if (detail.getIngredient() == null || detail.getIngredient().getId() == null) {
            throw new BusinessException("El adicional debe tener un ingrediente asociado");
        }
        if (detail.getParentDetailId() == null) {
            throw new BusinessException("El adicional debe estar asociado a un producto");
        }
        validateIngredient(detail.getIngredient().getId());
    }

    private void validateSaleForApproval(SaleEntity entity) {
        if (!"PENDING".equals(entity.getSaleStatus())) {
            throw new BusinessException("Solo se pueden aprobar ventas pendientes");
        }
    }

    private void validateStock(SaleEntity entity) {
        entity.getDetails().forEach(detail -> {
            if (detail.getIsAdditional()) {
                validateIngredientStock(detail);
            } else {
                validateProductStock(detail);
            }
        });
    }

    private void validateIngredientStock(SaleDetailEntity detail) {
        var ingredient = detail.getIngredient();
        if (ingredient.getStock().compareTo(detail.getQuantity()) < 0) {
            throw new BusinessException(
                    String.format("Stock insuficiente para el ingrediente %s. Disponible: %s, Requerido: %s",
                            ingredient.getName(), ingredient.getStock(), detail.getQuantity())
            );
        }
    }

    private void validateProductStock(SaleDetailEntity detail) {
        for (var recipeItem : detail.getProduct().getRecipeItems()) {
            var requiredQuantity = recipeItem.getQuantity().multiply(detail.getQuantity());
            if (recipeItem.getIngredient().getStock().compareTo(requiredQuantity) < 0) {
                throw new BusinessException(
                        String.format("Stock insuficiente del ingrediente %s para el producto %s",
                                recipeItem.getIngredient().getName(),
                                detail.getProduct().getName())
                );
            }
        }
    }

    private void updateStock(SaleEntity entity) {
        entity.getDetails().forEach(detail -> {
            if (detail.getIsAdditional()) {
                updateIngredientStock(detail);
            } else {
                updateProductStock(detail);
            }
        });
    }

    private void updateIngredientStock(SaleDetailEntity detail) {
        var ingredient = detail.getIngredient();
        ingredient.setStock(ingredient.getStock().subtract(detail.getQuantity()));
        ingredientRepository.save(ingredient);
    }

    private void updateProductStock(SaleDetailEntity detail) {
        detail.getProduct().getRecipeItems().forEach(recipeItem -> {
            var ingredient = recipeItem.getIngredient();
            var quantityToReduce = recipeItem.getQuantity().multiply(detail.getQuantity());
            ingredient.setStock(ingredient.getStock().subtract(quantityToReduce));
            ingredientRepository.save(ingredient);
        });
    }

    private void validateProduct(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + productId));

        if (!product.getStatus()) {
            throw new BusinessException("El producto no está activo: " + product.getName());
        }
    }

    private void validateIngredient(Long ingredientId) {
        var ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingrediente no encontrado con ID: " + ingredientId));

        if (!ingredient.getStatus()) {
            throw new BusinessException("El ingrediente no está activo: " + ingredient.getName());
        }
        if (!ingredient.getIsAddition()) {
            throw new BusinessException("El ingrediente no está disponible como adicional: " + ingredient.getName());
        }
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserEntity) {
            return ((UserEntity) auth.getPrincipal()).getId();
        }
        throw new BusinessException("No se pudo obtener el usuario actual");
    }

    private void setupSaleDetails(SaleEntity entity) {
        if (entity.getDetails() != null) {
            entity.getDetails().forEach(detail -> detail.setSale(entity));
        }
    }

    private void calculateTotals(Sale sale) {
        BigDecimal subtotal = sale.getDetails().stream()
                .map(this::calculateDetailTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setSubtotal(subtotal);
        sale.setTotal(subtotal.subtract(sale.getDiscount() != null ?
                sale.getDiscount() : BigDecimal.ZERO));
    }

    private BigDecimal calculateDetailTotal(SaleDetail detail) {
        BigDecimal unitPrice = detail.getIsAdditional() ?
                detail.getIngredient().getCost() :
                detail.getProduct().getPrice();

        detail.setUnitPrice(unitPrice);
        return unitPrice.multiply(detail.getQuantity());
    }
}