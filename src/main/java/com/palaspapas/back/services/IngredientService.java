package com.palaspapas.back.services;

import com.palaspapas.back.model.dto.request.inventory.IngredientRequest;
import com.palaspapas.back.model.dto.response.inventory.IngredientResponse;

import java.util.List;

public interface IngredientService {
    IngredientResponse create(IngredientRequest request);
    IngredientResponse update(Long id, IngredientRequest request);
    IngredientResponse findById(Long id);
    List<IngredientResponse> findAll();
    void delete(Long id);

}
