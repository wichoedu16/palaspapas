package com.palaspapas.back.persistence.repositories;

import com.palaspapas.back.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String name);

    List<ProductEntity> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE p.status = true AND p.category.id = :categoryId")
    List<ProductEntity> findActiveByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT p FROM ProductEntity p " +
            "LEFT JOIN FETCH p.recipeItems ri " +
            "LEFT JOIN FETCH ri.ingredient " +
            "WHERE p.id = :id")
    ProductEntity findByIdWithRecipe(@Param("id") Long id);

    @Query("SELECT p FROM ProductEntity p " +
            "WHERE p.status = true " +
            "AND EXISTS (SELECT 1 FROM p.recipeItems ri " +
            "WHERE ri.ingredient.stock >= ri.quantity)")
    List<ProductEntity> findAvailableProducts();
}