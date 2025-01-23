package com.palaspapas.back.service.interfaces;

import com.palaspapas.back.domain.Sale;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ISaleService {
    /**
     * Crea una nueva venta
     * @param sale datos de la venta a crear
     * @return la venta creada
     */
    Sale create(Sale sale);

    /**
     * Busca una venta por su ID
     * @param id el ID de la venta
     * @return la venta encontrada
     */
    Sale findById(Long id);

    /**
     * Obtiene todas las ventas
     * @return lista de ventas
     */
    List<Sale> findAll();

    /**
     * Aprueba una venta
     * @param id el ID de la venta a aprobar
     * @return la venta aprobada
     */
    Sale approve(Long id);

    /**
     * Cancela una venta
     * @param id el ID de la venta a cancelar
     * @return la venta cancelada
     */
    Sale cancel(Long id);

    /**
     * Aplica un descuento a una venta
     * @param id el ID de la venta
     * @param discount el monto del descuento
     * @return la venta con el descuento aplicado
     */
    Sale applyDiscount(Long id, BigDecimal discount);

    /**
     * Busca ventas por rango de fechas
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de ventas en el rango
     */
    List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca ventas por estado
     * @param status estado de la venta
     * @return lista de ventas con el estado especificado
     */
    List<Sale> findByStatus(String status);
}