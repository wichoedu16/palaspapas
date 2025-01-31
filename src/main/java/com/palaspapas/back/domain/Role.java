package com.palaspapas.back.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private Set<Permission> permissions = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}