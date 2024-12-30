package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.common.ValidationException;
import com.palaspapas.back.mapper.inventory.IngredientMapper;
import com.palaspapas.back.model.dto.request.inventory.IngredientRequest;
import com.palaspapas.back.model.dto.response.inventory.IngredientResponse;
import com.palaspapas.back.model.entity.inventory.Ingredient;
import com.palaspapas.back.exceptions.NotFoundException;
import com.palaspapas.back.model.entity.inventory.MeasurementUnit;
import com.palaspapas.back.model.entity.provider.Provider;
import com.palaspapas.back.repository.inventory.IngredientRepository;
import com.palaspapas.back.repository.MeasurementUnitRepository;
import com.palaspapas.back.repository.provider.ProviderRepository;
import com.palaspapas.back.services.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para gestionar ingredientes.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final MeasurementUnitRepository measurementUnitRepository;
    private final ProviderRepository providerRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    public IngredientResponse create(IngredientRequest request) {
        log.debug("Creando nuevo ingrediente: {}", request.getName());

        validateUniqueName(request.getName());

        //MeasurementUnit measurementUnit = findMeasurementUnit(request.getMeasurementUnitId());

        Provider provider = null;
        if (request.getProviderId() != null) {
            provider = findProvider(request.getProviderId());
        }

        Ingredient ingredient = ingredientMapper.toEntity(request);
       // ingredient.setMeasurementUnit(measurementUnit);
        ingredient.setProvider(provider);

        ingredient = ingredientRepository.save(ingredient);
        log.info("Ingrediente creado con ID: {}", ingredient.getId());

        return ingredientMapper.toResponse(ingredient);
    }

    @Override
    public IngredientResponse update(Long id, IngredientRequest request) {
        log.debug("Actualizando ingrediente ID: {}", id);

        Ingredient existingIngredient = findIngredientById(id);

        // Validar nombre único si cambió
        if (!existingIngredient.getName().equalsIgnoreCase(request.getName())) {
            validateUniqueName(request.getName());
        }

        // Validar y actualizar unidad de medida
        //MeasurementUnit measurementUnit = findMeasurementUnit(request.getMeasurementUnitId());

        // Validar y actualizar proveedor
        Provider provider = null;
        if (request.getProviderId() != null) {
            provider = findProvider(request.getProviderId());
        }

        // Actualizar campos
        //updateIngredientFields(existingIngredient, request, measurementUnit, provider);
        updateIngredientFields(existingIngredient, request, provider);

        existingIngredient = ingredientRepository.save(existingIngredient);
        log.info("Ingrediente actualizado con ID: {}", existingIngredient.getId());

        return ingredientMapper.toResponse(existingIngredient);
    }

    @Override
    public IngredientResponse findById(Long id) {
        Ingredient existingIngredient = findIngredientById(id);
        return ingredientMapper.toResponse(existingIngredient);
    }

    @Override
    public List<IngredientResponse> findAll() {
        return null;
    }


    @Override
    public void delete(Long id) {
        return;
    }

    // Métodos auxiliares privados para mantener el código DRY (Don't Repeat Yourself)
    private void validateUniqueName(String name) {
        if (ingredientRepository.existsByNameIgnoreCase(name)) {
            throw new ValidationException("Ya existe un ingrediente con el nombre: " + name);
        }
    }

    private MeasurementUnit findMeasurementUnit(Long id) {
        return measurementUnitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad de medida no encontrada con ID: " + id));
    }

    private Provider findProvider(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con ID: " + id));
    }

    private Ingredient findIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingrediente no encontrado con ID: " + id));
    }

    private void updateIngredientFields(Ingredient ingredient, IngredientRequest request, Provider provider) {
        ingredient.setName(request.getName());
        ingredient.setCurrentStock(request.getCurrentStock());
        ingredient.setMinimumStock(request.getMinimumStock());
        ingredient.setCurrentUnitCost(request.getCurrentUnitCost());
        ingredient.setSellingPrice(request.getSellingPrice());
        //ingredient.setMeasurementUnit(measurementUnit);
        ingredient.setProvider(provider);
    }
}