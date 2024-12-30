package com.palaspapas.back.model.dto.response.dish;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private CategorySummaryResponse parent;
    private List<CategorySummaryResponse> subcategories;
    private Integer displayOrder;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}