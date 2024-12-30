package com.palaspapas.back.repository;

import com.palaspapas.back.model.entity.inventory.MeasurementUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Long> {
    // Buscar por código, útil para validar duplicados
    Optional<MeasurementUnit> findByCodeIgnoreCase(String code);

    // Buscar unidades activas
    List<MeasurementUnit> findByActiveTrue();

    // Verificar si existe un código
    boolean existsByCodeIgnoreCase(String code);
}
