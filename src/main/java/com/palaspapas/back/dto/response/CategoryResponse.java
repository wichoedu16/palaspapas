package com.palaspapas.back.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isForKitchen;
    private Boolean isAddition;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}