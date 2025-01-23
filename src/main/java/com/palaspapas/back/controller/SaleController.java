package com.palaspapas.back.controller;

import com.palaspapas.back.domain.Sale;
import com.palaspapas.back.dto.mapper.ISaleDtoMapper;
import com.palaspapas.back.dto.request.ApplyDiscountRequest;
import com.palaspapas.back.dto.request.CreateSaleRequest;
import com.palaspapas.back.dto.request.SaleRequest;
import com.palaspapas.back.dto.response.SaleResponse;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.persistence.mappers.ISaleMapper;
import com.palaspapas.back.service.interfaces.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/sales")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sales", description = "API para gestión de ventas")
@SecurityRequirement(name = "Bearer Authentication")
public class SaleController {

    private final ISaleService saleService;
    private final ISaleDtoMapper saleDtoMapper;

    @PostMapping
    @Operation(summary = "Crear nueva venta")
    public ResponseEntity<SaleResponse> create(@Valid @RequestBody CreateSaleRequest request) {
        try {
            log.debug("Iniciando creación de venta");
            Sale sale = saleDtoMapper.toDomain(request);
            Sale created = saleService.create(sale);
            SaleResponse response = saleDtoMapper.toResponse(created);
            log.info("Venta creada exitosamente con ID: {}", created.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear la venta: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas las ventas")
    public ResponseEntity<List<SaleResponse>> getAllSales(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status
    ) {
        try {
            log.debug("Obteniendo lista de ventas");
            List<Sale> sales;

            if (startDate != null && endDate != null) {
                sales = saleService.findByDateRange(startDate, endDate);
            } else if (status != null) {
                sales = saleService.findByStatus(status);
            } else {
                sales = saleService.findAll();
            }

            List<SaleResponse> response = sales.stream()
                    .map(saleDtoMapper::toResponse)
                    .collect(Collectors.toList());

            log.debug("Se encontraron {} ventas", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener las ventas: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID")
    public ResponseEntity<SaleResponse> getById(@PathVariable Long id) {
        try {
            log.debug("Buscando venta con ID: {}", id);
            Sale sale = saleService.findById(id);
            SaleResponse response = saleDtoMapper.toResponse(sale);
            log.debug("Venta encontrada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al buscar la venta: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Aprobar una venta")
    public ResponseEntity<SaleResponse> approve(@PathVariable Long id) {
        try {
            log.debug("Iniciando aprobación de venta ID: {}", id);
            Sale approvedSale = saleService.approve(id);
            SaleResponse response = saleDtoMapper.toResponse(approvedSale);
            log.info("Venta aprobada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al aprobar la venta: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancelar una venta")
    public ResponseEntity<SaleResponse> cancel(@PathVariable Long id) {
        try {
            log.debug("Iniciando cancelación de venta ID: {}", id);
            Sale cancelledSale = saleService.cancel(id);
            SaleResponse response = saleDtoMapper.toResponse(cancelledSale);
            log.info("Venta cancelada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al cancelar la venta: {}", e.getMessage());
            throw e;
        }
    }

    @PatchMapping("/{id}/discount")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Aplicar descuento a una venta")
    public ResponseEntity<SaleResponse> applyDiscount(
            @PathVariable Long id,
            @Valid @RequestBody ApplyDiscountRequest request) {
        try {
            log.debug("Aplicando descuento a venta ID: {}", id);
            Sale updatedSale = saleService.applyDiscount(id, request.getDiscount());
            SaleResponse response = saleDtoMapper.toResponse(updatedSale);
            log.info("Descuento aplicado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al aplicar descuento: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/daily-report")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener reporte diario de ventas")
    public ResponseEntity<List<SaleResponse>> getDailyReport(
            @Valid @RequestBody SaleRequest request) {
        try {
            LocalDate date = request.getDate();
            log.debug("Generando reporte diario para la fecha: {}", date);
            List<Sale> sales = saleService.findByDateRange(
                    date.atStartOfDay(),
                    date.atTime(LocalTime.MAX)
            );
            List<SaleResponse> response = sales.stream()
                    .map(saleDtoMapper::toResponse)
                    .collect(Collectors.toList());
            log.info("Reporte diario generado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al generar reporte diario: {}", e.getMessage());
            throw e;
        }
    }
}