package com.palaspapas.back.model.dto.response.provider;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProviderResponse {
    private Long id;
    private String name;
    private String taxId;
    private String contactPhone;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
