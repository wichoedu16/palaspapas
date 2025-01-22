package com.palaspapas.back.persistence.repositories;

import com.palaspapas.back.persistence.entities.RecipeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRecipeItemRepository extends JpaRepository<RecipeItemEntity, Long> {
    List<RecipeItemEntity> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}