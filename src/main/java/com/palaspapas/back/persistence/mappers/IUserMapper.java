package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.User;
import com.palaspapas.back.persistence.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IRoleMapper.class})
public interface IUserMapper {
    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);
}