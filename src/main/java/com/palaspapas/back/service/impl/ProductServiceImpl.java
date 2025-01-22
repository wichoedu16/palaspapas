package com.palaspapas.back.service.impl;


import com.palaspapas.back.domain.Product;
import com.palaspapas.back.domain.RecipeItem;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.exception.NotFoundException;
import com.palaspapas.back.persistence.entities.ProductEntity;
import com.palaspapas.back.persistence.mappers.IProductMapper;
import com.palaspapas.back.persistence.repositories.ICategoryRepository;
import com.palaspapas.back.persistence.repositories.IIngredientRepository;
import com.palaspapas.back.persistence.repositories.IProductRepository;
import com.palaspapas.back.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IIngredientRepository ingredientRepository;
    private final IProductMapper productMapper;

    @Override
    public Product create(Product product) {
        log.debug("Creando nuevo producto: {}", product.getName());
        validateNewProduct(product);

        ProductEntity entity = productMapper.toEntity(product);
        entity.setStatus(true);

        // Establecer la relación bidireccional
        entity.getRecipeItems().forEach(item -> item.setProduct(entity));

        ProductEntity savedEntity = productRepository.save(entity);
        log.info("Producto creado con ID: {}", savedEntity.getId());

        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Product update(Long id, Product product) {
        log.debug("Actualizando producto con ID: {}", id);
        ProductEntity existingEntity = findEntityById(id);

        validateProduct(product);
        validateCategory(product.getCategory().getId());

        if (!existingEntity.getName().equals(product.getName())) {
            validateNameUnique(product.getName());
        }

        productMapper.updateEntity(existingEntity, product);

        // Actualizar la relación bidireccional
        existingEntity.getRecipeItems().clear();
        ProductEntity entityToUpdate = productMapper.toEntity(product);
        entityToUpdate.getRecipeItems().forEach(item -> item.setProduct(existingEntity));
        existingEntity.getRecipeItems().addAll(entityToUpdate.getRecipeItems());

        ProductEntity updatedEntity = productRepository.save(existingEntity);
        log.info("Producto actualizado exitosamente");

        return productMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productMapper.toDomain(findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(Long categoryId) {
        validateCategory(categoryId);
        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Long id) {
        log.debug("Eliminando producto con ID: {}", id);
        ProductEntity entity = findEntityById(id);
        productRepository.delete(entity);
        log.info("Producto eliminado exitosamente");
    }

    @Override
    public Product changeStatus(Long id, Boolean status) {
        log.debug("Cambiando estado de producto ID: {} a {}", id, status);
        ProductEntity entity = findEntityById(id);
        entity.setStatus(status);

        ProductEntity updatedEntity = productRepository.save(entity);
        log.info("Estado de producto actualizado exitosamente");

        return productMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAvailableStock(Long productId) {
        ProductEntity product = findEntityById(productId);
        return product.getRecipeItems().stream()
                .allMatch(item -> {
                    var ingredient = item.getIngredient();
                    return ingredient.getStock().compareTo(item.getQuantity()) >= 0;
                });
    }

    private ProductEntity findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    private void validateNewProduct(Product product) {
        validateProduct(product);
        validateNameUnique(product.getName());
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new BusinessException("El producto no puede ser nulo");
        }

        validateBasicFields(product);
        validateCategory(product.getCategory().getId());
        validateRecipeItems(product.getRecipeItems());
    }

    private void validateBasicFields(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new BusinessException("El nombre es requerido");
        }

        if (product.getName().length() > 100) {
            throw new BusinessException("El nombre no puede exceder los 100 caracteres");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio debe ser mayor a 0");
        }
    }

    private void validateRecipeItems(List<RecipeItem> recipeItems) {
        if (recipeItems == null || recipeItems.isEmpty()) {
            throw new BusinessException("El producto debe tener al menos un ingrediente");
        }

        recipeItems.forEach(item -> {
            if (!ingredientRepository.existsById(item.getIngredient().getId())) {
                throw new BusinessException("Ingrediente no encontrado con ID: " + item.getIngredient().getId());
            }

            if (item.getQuantity() == null || item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("La cantidad debe ser mayor a 0");
            }
        });
    }

    private void validateCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new BusinessException("La categoría especificada no existe");
        }
    }

    private void validateNameUnique(String name) {
        if (productRepository.existsByName(name)) {
            throw new BusinessException("Ya existe un producto con el nombre: " + name);
        }
    }
}