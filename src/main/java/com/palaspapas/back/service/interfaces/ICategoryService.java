package com.palaspapas.back.service.interfaces;

import com.palaspapas.back.domain.Category;
import java.util.List;

public interface ICategoryService {
    Category create(Category category);
    Category update(Long id, Category category);
    Category findById(Long id);
    List<Category> findAll();
    void delete(Long id);
    Category changeStatus(Long id, Boolean status);
}