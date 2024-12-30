package com.palaspapas.back.repository.dish;

import com.palaspapas.back.model.entity.dish.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByActiveTrue();

    List<Dish> findByCategoryIdAndActiveTrue(Long categoryId);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT d FROM Dish d WHERE " +
            "LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND " +
            "d.active = true")
    Page<Dish> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}
