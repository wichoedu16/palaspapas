package com.palaspapas.back.repository.dish;

import com.palaspapas.back.model.entity.dish.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByActiveTrue();

    List<Category> findByParentIdAndActiveTrue(Long parentId);

    List<Category> findByParentIsNullAndActiveTrue();

    boolean existsByNameIgnoreCaseAndParentId(String name, Long parentId);

    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.displayOrder ASC")
    List<Category> findAllOrdered();
}


