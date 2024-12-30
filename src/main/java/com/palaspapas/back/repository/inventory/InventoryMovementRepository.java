package com.palaspapas.back.repository.inventory;

import com.palaspapas.back.model.entity.inventory.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findByIngredientIdOrderByCreatedAtDesc(Long ingredientId);

    @Query("SELECT m FROM InventoryMovement m WHERE m.ingredient.id = :ingredientId " +
            "AND m.createdAt BETWEEN :startDate AND :endDate")
    List<InventoryMovement> findByIngredientIdAndDateRange(
            @Param("ingredientId") Long ingredientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<InventoryMovement> findByTypeAndCreatedAtBetween(
            String type,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}