package com.palaspapas.back.dto.response;


import lombok.Data;
import java.util.Set;

@Data
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private Set<PermissionResponse> permissions;
}