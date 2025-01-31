package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.Role;
import com.palaspapas.back.dto.request.RoleRequest;
import com.palaspapas.back.dto.response.RoleResponse;
import com.palaspapas.back.persistence.mappers.IPermissionMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {IPermissionMapper.class})
public interface IRoleDtoMapper {

    // De CreateRoleRequest a Domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "permissions", ignore = true) // Ignoramos porque los permisos se asignan después
    Role toDomain(RoleRequest request);

    // De Domain a RoleResponse
    @Mapping(target = "permissions", source = "permissions")
    RoleResponse toResponse(Role domain);
}