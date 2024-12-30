package com.palaspapas.back.mapper.provider;

import com.palaspapas.back.model.dto.request.provider.ProviderRequest;
import com.palaspapas.back.model.dto.response.provider.ProviderResponse;
import com.palaspapas.back.model.entity.provider.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "active", constant = "true")
    Provider toEntity(ProviderRequest request);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ProviderResponse toResponse(Provider provider);
}