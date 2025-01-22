package com.palaspapas.back.service.impl;

import com.palaspapas.back.domain.Category;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.exception.NotFoundException;
import com.palaspapas.back.persistence.entities.CategoryEntity;
import com.palaspapas.back.persistence.mappers.ICategoryMapper;
import com.palaspapas.back.persistence.repositories.ICategoryRepository;
import com.palaspapas.back.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    @Override
    public Category create(Category category) {
        log.debug("Creando nueva categoría: {}", category.getName());
        validateNewCategory(category);

        CategoryEntity entity = categoryMapper.toEntity(category);
        entity.setStatus(true);

        CategoryEntity savedEntity = categoryRepository.save(entity);
        return categoryMapper.toDomain(savedEntity);
    }

    @Override
    public Category update(Long id, Category category) {
        log.debug("Actualizando categoría con ID: {}", id);
        CategoryEntity existingEntity = findEntityById(id);

        if (!existingEntity.getName().equals(category.getName())) {
            validateNameUnique(category.getName());
        }

        validateCategory(category);
        categoryMapper.updateEntity(existingEntity, category);

        CategoryEntity updatedEntity = categoryRepository.save(existingEntity);
        log.info("Categoría actualizada exitosamente");

        return categoryMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        log.debug("Buscando categoría por ID: {}", id);
        return categoryMapper.toDomain(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.debug("Obteniendo todas las categorías");
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        log.debug("Eliminando categoría con ID: {}", id);
        CategoryEntity entity = findEntityById(id);

        // Aquí podrías agregar validaciones adicionales
        // Por ejemplo, verificar si la categoría está en uso

        categoryRepository.delete(entity);
        log.info("Categoría eliminada exitosamente");
    }

    @Override
    public Category changeStatus(Long id, Boolean status) {
        log.debug("Cambiando estado de categoría ID: {} a {}", id, status);
        CategoryEntity entity = findEntityById(id);
        entity.setStatus(status);

        CategoryEntity updatedEntity = categoryRepository.save(entity);
        log.info("Estado de categoría actualizado exitosamente");

        return categoryMapper.toDomain(updatedEntity);
    }

    private CategoryEntity findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));
    }

    private void validateNewCategory(Category category) {
        validateCategory(category);
        validateNameUnique(category.getName());
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new BusinessException("La categoría no puede ser nula");
        }

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new BusinessException("El nombre es requerido");
        }

        if (category.getName().length() > 50) {
            throw new BusinessException("El nombre no puede exceder los 50 caracteres");
        }

        if (category.getDescription() != null && category.getDescription().length() > 200) {
            throw new BusinessException("La descripción no puede exceder los 200 caracteres");
        }

        if (category.getIsForKitchen() == null) {
            throw new BusinessException("Debe especificar si es para cocina");
        }

        if (category.getIsAddition() == null) {
            throw new BusinessException("Debe especificar si puede ser adicional");
        }
    }

    private void validateNameUnique(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new BusinessException("Ya existe una categoría con el nombre: " + name);
        }
    }
}