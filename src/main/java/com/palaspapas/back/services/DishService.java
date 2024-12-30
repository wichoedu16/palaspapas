package com.palaspapas.back.services;

import com.palaspapas.back.model.dto.request.dish.DishRequest;
import com.palaspapas.back.model.dto.response.dish.DishResponse;

import java.math.BigDecimal;
import java.util.List;

public interface DishService {
    DishResponse create(DishRequest request);
    DishResponse update(Long id, DishRequest request);
    DishResponse findById(Long id);
    List<DishResponse> findAll();
    List<DishResponse> findByCategory(Long categoryId);
    void delete(Long id);
    BigDecimal calculateCost(Long id);
}
