package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.common.ResourceNotFoundException;
import com.palaspapas.back.exceptions.inventory.InsufficientStockException;
import com.palaspapas.back.exceptions.inventory.InvalidMovementException;
import com.palaspapas.back.mapper.OrderDetailMapper;
import com.palaspapas.back.mapper.OrderMapper;
import com.palaspapas.back.model.dto.request.sale.OrderDetailRequest;
import com.palaspapas.back.model.dto.request.sale.OrderRequest;
import com.palaspapas.back.model.dto.response.sale.OrderResponse;
import com.palaspapas.back.model.entity.dish.Dish;
import com.palaspapas.back.model.entity.dish.DishIngredient;
import com.palaspapas.back.model.entity.sale.Order;
import com.palaspapas.back.model.entity.sale.OrderDetail;
import com.palaspapas.back.model.entity.sale.OrderStatus;
import com.palaspapas.back.repository.OrderRepository;
import com.palaspapas.back.repository.dish.DishRepository;
import com.palaspapas.back.services.InventoryService;
import com.palaspapas.back.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final InventoryService inventoryService;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper detailMapper;

    @Override
    public OrderResponse create(OrderRequest request) {
        log.debug("Creando nueva orden para cliente: {}", request.getCustomerName());

        // Validar disponibilidad de ingredientes
        validateIngredientsAvailability(request.getDetails());

        Order order = orderMapper.toEntity(request);

        // Procesar detalles y calcular totales
        List<OrderDetail> details = createOrderDetails(order, request.getDetails());
        order.setDetails(details);

        // Calcular totales
        calculateTotals(order, request.getDiscount());

        order = orderRepository.save(order);

        // Actualizar inventario
        updateInventory(order.getDetails());

        log.info("Orden creada con número: {}", order.getOrderNumber());
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        log.debug("Actualizando estado de orden ID: {} a: {}", id, newStatus);

        Order order = findOrderById(id);
        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        order = orderRepository.save(order);

        log.info("Estado de orden actualizado. ID: {}, Nuevo estado: {}", id, newStatus);
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse findById(Long id) {
        return null;
    }

    @Override
    public OrderResponse findByOrderNumber(String orderNumber) {
        return null;
    }

    @Override
    public List<OrderResponse> findByStatus(OrderStatus status) {
        return null;
    }

    @Override
    public List<OrderResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        log.debug("Cancelando orden ID: {}", id);

        Order order = findOrderById(id);
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidMovementException("No se puede cancelar una orden ya entregada");
        }

        order.setStatus(OrderStatus.CANCELLED);
        // Revertir cambios en inventario si es necesario
        if (order.getStatus() != OrderStatus.PENDING) {
            restoreInventory(order.getDetails());
        }

        orderRepository.save(order);
        log.info("Orden cancelada. ID: {}", id);
    }

    private void validateIngredientsAvailability(List<OrderDetailRequest> details) {
        for (OrderDetailRequest detail : details) {
            Dish dish = findDishById(detail.getDishId());
            for (DishIngredient ingredient : dish.getIngredients()) {
                BigDecimal requiredQuantity = ingredient.getQuantity()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));
                if (!inventoryService.hasEnoughStock(ingredient.getId(), requiredQuantity)) {
                    throw new InsufficientStockException(
                            String.format("Stock insuficiente para el ingrediente '%s' del plato '%s'",
                                    ingredient.getIngredient().getName(),
                                    dish.getName()),
                            ingredient.getIngredient().getCurrentStock(),
                            requiredQuantity
                    );
                }
            }
        }
    }

    private List<OrderDetail> createOrderDetails(Order order, List<OrderDetailRequest> detailRequests) {
        return detailRequests.stream()
                .map(req -> {
                    Dish dish = findDishById(req.getDishId());
                    return OrderDetail.builder()
                            .order(order)
                            .dish(dish)
                            .quantity(req.getQuantity())
                            .unitPrice(dish.getPrice())
                            .subtotal(dish.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())))
                            .notes(req.getNotes())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private void calculateTotals(Order order, BigDecimal discount) {
        BigDecimal subtotal = order.getDetails().stream()
                .map(OrderDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSubtotal(subtotal);
        order.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        order.setTotal(subtotal.subtract(order.getDiscount()));
    }

    private void updateInventory(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            for (DishIngredient ingredient : detail.getDish().getIngredients()) {
                BigDecimal quantityToReduce = ingredient.getQuantity()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));
                inventoryService.reduceStock(
                        ingredient.getIngredient().getId(),
                        quantityToReduce,
                        "Venta - Orden #" + detail.getOrder().getOrderNumber()
                );
            }
        }
    }

    private void restoreInventory(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            for (DishIngredient ingredient : detail.getDish().getIngredients()) {
                BigDecimal quantityToRestore = ingredient.getQuantity()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));
                inventoryService.addStock(
                        ingredient.getIngredient().getId(),
                        quantityToRestore,
                        "Cancelación - Orden #" + detail.getOrder().getOrderNumber()
                );
            }
        }
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Orden no encontrada con ID: %d", id)));
    }

    private Dish findDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Plato no encontrado con ID: %d", id)));
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Implementar lógica de validación de transiciones de estado
        if (currentStatus == OrderStatus.CANCELLED) {
            throw new InvalidMovementException("No se puede actualizar el estado de una orden cancelada");
        }
        if (currentStatus == OrderStatus.DELIVERED) {
            throw new InvalidMovementException("No se puede actualizar el estado de una orden entregada");
        }
        // Agregar más validaciones según las reglas de negocio
    }
}