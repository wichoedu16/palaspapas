package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.Role;
import com.palaspapas.back.persistence.entities.RoleEntity;
import com.palaspapas.back.dto.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IPermissionMapper.class})
public interface IRoleMapper {
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);
    RoleResponse toResponse(Role domain);
}