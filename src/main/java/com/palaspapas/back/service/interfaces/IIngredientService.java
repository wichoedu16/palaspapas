package com.palaspapas.back.service.interfaces;

import com.palaspapas.back.domain.Ingredient;
import java.util.List;

public interface IIngredientService {
    Ingredient create(Ingredient ingredient);
    Ingredient update(Long id, Ingredient ingredient);
    Ingredient findById(Long id);
    List<Ingredient> findAll();
    List<Ingredient> findByCategoryId(Long categoryId);
    List<Ingredient> findLowStock();
    void delete(Long id);
    Ingredient changeStatus(Long id, Boolean status);
}