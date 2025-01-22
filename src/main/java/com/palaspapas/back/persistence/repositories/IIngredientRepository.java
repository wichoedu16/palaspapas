package com.palaspapas.back.persistence.repositories;

import com.palaspapas.back.persistence.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IIngredientRepository extends JpaRepository<IngredientEntity, Long> {
    boolean existsByName(String name);

    List<IngredientEntity> findByCategoryId(Long categoryId);

    @Query("SELECT i FROM IngredientEntity i WHERE i.stock <= i.minimumStock AND i.status = true")
    List<IngredientEntity> findByStockLessThanOrEqualToMinimumStock();

    List<IngredientEntity> findByIsForKitchen(Boolean isForKitchen);

    List<IngredientEntity> findByIsAddition(Boolean isAddition);

    @Query("SELECT i FROM IngredientEntity i WHERE i.status = true AND i.stock > 0")
    List<IngredientEntity> findAvailableIngredients();
}