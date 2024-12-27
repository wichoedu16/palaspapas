package com.palaspapas.back.infraestructure.services;

import com.palaspapas.back.api.models.request.CategoryRequest;
import com.palaspapas.back.api.models.response.CategoryResponse;
import com.palaspapas.back.domain.entities.Category;
import com.palaspapas.back.domain.repository.CategoryRepository;
import com.palaspapas.back.infraestructure.abstract_services.ICategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class CategoryService implements ICategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {
        return null;
    }

    @Override
    public CategoryResponse read(Long id){
        var categoryFromDB = this.categoryRepository.findById(id).orElseThrow();
        return this.entityToResponse(categoryFromDB);
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }

    private CategoryResponse entityToResponse(Category entity){
        var response = new CategoryResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
