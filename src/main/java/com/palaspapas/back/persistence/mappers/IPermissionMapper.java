package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.Permission;
import com.palaspapas.back.persistence.entities.PermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {
    Permission toDomain(PermissionEntity entity);
    PermissionEntity toEntity(Permission domain);
    List<Permission> toDomainList(List<PermissionEntity> entities);
    Set<Permission> toDomainSet(Set<PermissionEntity> entities);
    Set<PermissionEntity> toEntitySet(Set<Permission> domains);
    void updateEntityFromDomain(Permission domain, @MappingTarget PermissionEntity entity);
}