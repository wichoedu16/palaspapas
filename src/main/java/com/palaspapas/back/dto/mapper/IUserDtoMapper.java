package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.User;
import com.palaspapas.back.dto.request.UserRequest;
import com.palaspapas.back.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserDtoMapper {
    User toDomain(UserRequest request);
    UserResponse toResponse(User user);
}
