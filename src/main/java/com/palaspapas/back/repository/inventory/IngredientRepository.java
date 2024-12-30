package com.palaspapas.back.repository.inventory;

import com.palaspapas.back.model.entity.inventory.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar la persistencia de ingredientes.
 */
// repository/IngredientRepository.java
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByActiveTrue();

    @Query("SELECT i FROM Ingredient i WHERE i.currentStock <= i.minimumStock AND i.active = true")
    List<Ingredient> findLowStockIngredients();

    @Query("SELECT i FROM Ingredient i WHERE i.provider.id = :providerId AND i.active = true")
    List<Ingredient> findByProviderId(@Param("providerId") Long providerId);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Ingredient i " +
            "WHERE i.id = :id AND i.currentStock >= :requiredQuantity")
    boolean hasEnoughStock(@Param("id") Long id, @Param("requiredQuantity") BigDecimal requiredQuantity);

    Arrays findByActiveTrueAndCurrentStockLessThanEqualAndMinimumStockGreaterThan(BigDecimal zero, BigDecimal zero1);
}