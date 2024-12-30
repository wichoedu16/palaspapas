package com.palaspapas.back.model.dto.response.dish;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategorySummaryResponse {
    private Long id;
    private String name;
    private Integer displayOrder;
}