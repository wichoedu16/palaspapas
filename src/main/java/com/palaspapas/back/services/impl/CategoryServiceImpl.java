package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.category.InvalidCategoryOperationException;
import com.palaspapas.back.exceptions.common.DuplicateResourceException;
import com.palaspapas.back.exceptions.common.ResourceNotFoundException;
import com.palaspapas.back.mapper.dish.CategoryMapper;
import com.palaspapas.back.model.dto.request.dish.CategoryRequest;
import com.palaspapas.back.model.dto.response.dish.CategoryResponse;
import com.palaspapas.back.model.entity.dish.Category;
import com.palaspapas.back.repository.dish.CategoryRepository;
import com.palaspapas.back.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        log.debug("Creando nueva categoría: {}", request.getName());

        validateUniqueCategoryName(request.getName(), request.getParentId());

        Category category = categoryMapper.toEntity(request);

        if (request.getParentId() != null) {
            Category parent = findCategoryById(request.getParentId());
            category.setParent(parent);
        }

        if (category.getDisplayOrder() == null) {
            category.setDisplayOrder(getNextDisplayOrder(category.getParent()));
        }

        category = categoryRepository.save(category);
        log.info("Categoría creada con ID: {}", category.getId());

        return buildCategoryResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        log.debug("Actualizando categoría ID: {}", id);

        Category category = findCategoryById(id);

        if (!category.getName().equalsIgnoreCase(request.getName())) {
            validateUniqueCategoryName(request.getName(), request.getParentId());
        }

        if (request.getParentId() != null) {
            validateHierarchyCycle(id, request.getParentId());
            Category parent = findCategoryById(request.getParentId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        updateCategoryFields(category, request);
        category = categoryRepository.save(category);

        log.info("Categoría actualizada con ID: {}", category.getId());
        return buildCategoryResponse(category);
    }

    @Override
    public CategoryResponse findById(Long id) {
        return null;
    }

    @Override
    public List<CategoryResponse> findAll() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findMainCategories() {
        return categoryRepository.findByParentIsNullAndActiveTrue().stream()
                .map(this::buildCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> findSubcategories(Long parentId) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    // Métodos privados de ayuda
    private void validateUniqueCategoryName(String name, Long parentId) {
        if (categoryRepository.existsByNameIgnoreCaseAndParentId(name, parentId)) {
            throw new DuplicateResourceException(
                    String.format("Ya existe una categoría con el nombre '%s' en este nivel", name));
        }
    }

    private void validateHierarchyCycle(Long categoryId, Long newParentId) {
        if (categoryId.equals(newParentId)) {
            throw new InvalidCategoryOperationException("Una categoría no puede ser su propio padre");
        }

        Category parent = findCategoryById(newParentId);
        while (parent != null) {
            if (parent.getId().equals(categoryId)) {
                throw new InvalidCategoryOperationException("No se puede crear un ciclo en la jerarquía");
            }
            parent = parent.getParent();
        }
    }

    private void updateCategoryFields(Category category, CategoryRequest request) {
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
    }

    private CategoryResponse buildCategoryResponse(Category category) {
        CategoryResponse response = categoryMapper.toResponse(category);

        if (category.getParent() != null) {
            response.setParent(categoryMapper.toSummaryResponse(category.getParent()));
        }

        if (!category.getSubcategories().isEmpty()) {
            response.setSubcategories(
                    category.getSubcategories().stream()
                            .filter(Category::isActive)
                            .map(categoryMapper::toSummaryResponse)
                            .collect(Collectors.toList())
            );
        }

        return response;
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Categoría no encontrada con ID: %d", id)));
    }

    private Integer getNextDisplayOrder(Category parent) {
        List<Category> siblings;
        if (parent != null) {
            siblings = categoryRepository.findByParentIdAndActiveTrue(parent.getId());
        } else {
            siblings = categoryRepository.findByParentIsNullAndActiveTrue();
        }
        return siblings.stream()
                .map(Category::getDisplayOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}