package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.Permission;
import com.palaspapas.back.dto.request.PermissionRequest;
import com.palaspapas.back.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IPermissionDtoMapper {
    Permission toDomain(PermissionRequest request);

    PermissionResponse toResponse(Permission permission);
    List<PermissionResponse> toResponseList(List<Permission> domain);
}
