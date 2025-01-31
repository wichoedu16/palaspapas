package com.palaspapas.back.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private RoleResponse role;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
