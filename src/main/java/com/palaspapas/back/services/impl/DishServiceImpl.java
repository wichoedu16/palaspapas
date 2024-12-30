package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.NotFoundException;
import com.palaspapas.back.exceptions.common.DuplicateResourceException;
import com.palaspapas.back.exceptions.common.ResourceNotFoundException;
import com.palaspapas.back.exceptions.inventory.InsufficientStockException;
import com.palaspapas.back.mapper.dish.DishIngredientMapper;
import com.palaspapas.back.mapper.dish.DishMapper;
import com.palaspapas.back.model.dto.request.dish.DishIngredientRequest;
import com.palaspapas.back.model.dto.request.dish.DishRequest;
import com.palaspapas.back.model.dto.response.dish.DishResponse;
import com.palaspapas.back.model.entity.dish.Category;
import com.palaspapas.back.model.entity.dish.Dish;
import com.palaspapas.back.model.entity.dish.DishIngredient;
import com.palaspapas.back.model.entity.inventory.Ingredient;
import com.palaspapas.back.repository.dish.CategoryRepository;
import com.palaspapas.back.repository.dish.DishRepository;
import com.palaspapas.back.repository.inventory.IngredientRepository;
import com.palaspapas.back.services.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final DishMapper dishMapper;
    private final DishIngredientMapper dishIngredientMapper;

    @Override
    public DishResponse create(DishRequest request) {
        log.debug("Creando nuevo plato: {}", request.getName());

        validateUniqueDishName(request.getName());
        Category category = findCategoryById(request.getCategoryId());

        Dish dish = dishMapper.toEntity(request);
        dish.setCategory(category);

        // Procesar ingredientes si existen
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            List<DishIngredient> ingredients = createDishIngredients(dish, request.getIngredients());
            dish.setIngredients(ingredients);
            // Calcular costo total del plato
            dish.setCost(calculateTotalCost(ingredients));
        }

        dish = dishRepository.save(dish);
        log.info("Plato creado con ID: {}", dish.getId());

        return dishMapper.toResponse(dish);
    }

    @Override
    public DishResponse update(Long id, DishRequest request) {
        return null;
    }

    @Override
    public DishResponse findById(Long id) {
        return null;
    }

    @Override
    public List<DishResponse> findAll() {
        return null;
    }

    @Override
    public List<DishResponse> findByCategory(Long categoryId) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public BigDecimal calculateCost(Long id) {
        Dish dish = findDishById(id);
        return calculateTotalCost(dish.getIngredients());
    }

    private List<DishIngredient> createDishIngredients(Dish dish, List<DishIngredientRequest> requests) {
        return requests.stream()
                .map(req -> {
                    Ingredient ingredient = findIngredientById(req.getIngredientId());
                    return DishIngredient.builder()
                            .dish(dish)
                            .ingredient(ingredient)
                            .quantity(req.getQuantity())
                            .unitCost(ingredient.getCurrentUnitCost())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalCost(List<DishIngredient> ingredients) {
        return ingredients.stream()
                .map(di -> di.getQuantity().multiply(di.getUnitCost()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateUniqueDishName(String name) {
        if (dishRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Ya existe un plato con el nombre: " + name);
        }
    }

    private Dish findDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Plato no encontrado con ID: %d", id)));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Categoría no encontrada con ID: %d", id)));
    }

    private Ingredient findIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Ingrediente no encontrado con ID: %d", id)));
    }
}