package com.palaspapas.back.repository.inventory;

import com.palaspapas.back.model.entity.inventory.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar las operaciones de persistencia de Inventory.
 * Extiende JpaRepository para heredar operaciones básicas de CRUD.
 */
@Repository
public interface InventoryRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Busca una categoría de inventario por su nombre ignorando mayúsculas/minúsculas.
     * @param name El nombre a buscar
     * @return La categoría si existe
     */
    Optional<Ingredient> findByNameIgnoreCase(String name);

    Optional<Ingredient> findByI(String name);

    /**
     * Verifica si existe una categoría con el nombre dado.
     * @param name El nombre a verificar
     * @return true si existe, false si no
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Encuentra todas las categorías de un tipo específico que estén activas.
     * @param type El tipo de inventario
     * @return Lista de categorías activas del tipo especificado
     */
    List<Ingredient> findByTypeAndIsActiveTrue(String type);

    /**
     * Encuentra todas las categorías activas.
     * @return Lista de categorías activas
     */
    List<Ingredient> findByIsActiveTrue();

    /**
     * Para buscar ingredientes por múltiples IDs
     * @param ids IDs de la categoría
     * @return Lista de ingredientes
     */
    //
    List<Ingredient> findByIdInAndActiveTrue(Collection<Long> ids);

    @Query("SELECT i FROM Ingredient i WHERE i.currentStock <= i.minimumStock AND i.active = true")
    List<Ingredient> findAllLowStock();

    @Query("SELECT i FROM Ingredient i WHERE i.currentStock = 0 AND i.active = true")
    List<Ingredient> findAllOutOfStock();

    @Query("SELECT i FROM Ingredient i " +
            "WHERE i.provider.id = :providerId AND i.active = true")
    List<Ingredient> findByProviderId(@Param("providerId") Long providerId);
}
