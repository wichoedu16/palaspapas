package com.palaspapas.back.service.impl;


import com.palaspapas.back.domain.Ingredient;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.exception.NotFoundException;
import com.palaspapas.back.persistence.entities.IngredientEntity;
import com.palaspapas.back.persistence.mappers.IIngredientMapper;
import com.palaspapas.back.persistence.repositories.ICategoryRepository;
import com.palaspapas.back.persistence.repositories.IIngredientRepository;
import com.palaspapas.back.service.interfaces.IIngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IngredientServiceImpl implements IIngredientService {

    private final IIngredientRepository ingredientRepository;
    private final ICategoryRepository categoryRepository;
    private final IIngredientMapper ingredientMapper;

    @Override
    public Ingredient create(Ingredient ingredient) {
        log.debug("Creando nuevo ingrediente: {}", ingredient.getName());
        validateNewIngredient(ingredient);

        IngredientEntity entity = ingredientMapper.toEntity(ingredient);
        entity.setStatus(true);

        IngredientEntity savedEntity = ingredientRepository.save(entity);
        log.info("Ingrediente creado con ID: {}", savedEntity.getId());

        return ingredientMapper.toDomain(savedEntity);
    }

    @Override
    public Ingredient update(Long id, Ingredient ingredient) {
        log.debug("Actualizando ingrediente con ID: {}", id);
        IngredientEntity existingEntity = findEntityById(id);

        if (ingredient.getName() != null && !existingEntity.getName().equals(ingredient.getName())) {
            validateNameUnique(ingredient.getName());
            existingEntity.setName(ingredient.getName());
        }

        if (ingredient.getCategory() != null && ingredient.getCategory().getId() != null) {
            validateCategory(ingredient.getCategory().getId());
            existingEntity.setCategory(categoryRepository.findById(ingredient.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + ingredient.getCategory().getId())));
        }

        if (ingredient.getDescription() != null) {
            existingEntity.setDescription(ingredient.getDescription());
        }

        if (ingredient.getStock() != null) {
            existingEntity.setStock(ingredient.getStock());
        }

        if (ingredient.getMinimumStock() != null) {
            existingEntity.setMinimumStock(ingredient.getMinimumStock());
        }

        if (ingredient.getCost() != null) {
            existingEntity.setCost(ingredient.getCost());
        }

        IngredientEntity updatedEntity = ingredientRepository.save(existingEntity);
        log.info("Ingrediente actualizado exitosamente");

        return ingredientMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Ingredient findById(Long id) {
        return ingredientMapper.toDomain(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll().stream()
                .map(ingredientMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findByCategoryId(Long categoryId) {
        validateCategory(categoryId);
        return ingredientRepository.findByCategoryId(categoryId).stream()
                .map(ingredientMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findLowStock() {
        return ingredientRepository.findByStockLessThanOrEqualToMinimumStock().stream()
                .map(ingredientMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        log.debug("Eliminando ingrediente con ID: {}", id);
        IngredientEntity entity = findEntityById(id);

        // Aquí podrías agregar validaciones adicionales
        // Por ejemplo, verificar si el ingrediente está en uso en alguna receta

        ingredientRepository.delete(entity);
        log.info("Ingrediente eliminado exitosamente");
    }

    @Override
    public Ingredient changeStatus(Long id, Boolean status) {
        log.debug("Cambiando estado de ingrediente ID: {} a {}", id, status);
        IngredientEntity entity = findEntityById(id);
        entity.setStatus(status);

        IngredientEntity updatedEntity = ingredientRepository.save(entity);
        log.info("Estado de ingrediente actualizado exitosamente");

        return ingredientMapper.toDomain(updatedEntity);
    }

    private IngredientEntity findEntityById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingrediente no encontrado con ID: " + id));
    }

    private void validateNewIngredient(Ingredient ingredient) {
        validateIngredient(ingredient);
        validateNameUnique(ingredient.getName());
    }

    private void validateIngredient(Ingredient ingredient) {
        if (ingredient == null) {
            throw new BusinessException("El ingrediente no puede ser nulo");
        }

        validateCategory(ingredient.getCategory().getId());

        if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()) {
            throw new BusinessException("El nombre es requerido");
        }

        if (ingredient.getName().length() > 100) {
            throw new BusinessException("El nombre no puede exceder los 100 caracteres");
        }

    }

    private void validateCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new BusinessException("La categoría especificada no existe");
        }
    }

    private void validateNameUnique(String name) {
        if (ingredientRepository.existsByName(name)) {
            throw new BusinessException("Ya existe un ingrediente con el nombre: " + name);
        }
    }
}
