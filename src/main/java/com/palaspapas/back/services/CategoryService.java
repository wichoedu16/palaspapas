package com.palaspapas.back.services;


import com.palaspapas.back.model.dto.request.dish.CategoryRequest;
import com.palaspapas.back.model.dto.response.dish.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    CategoryResponse findById(Long id);
    List<CategoryResponse> findAll();
    List<CategoryResponse> findMainCategories();
    List<CategoryResponse> findSubcategories(Long parentId);
    void delete(Long id);
}
