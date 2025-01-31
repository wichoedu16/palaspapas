package com.palaspapas.back.service.impl;

import com.palaspapas.back.domain.Role;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.persistence.mappers.IRoleMapper;
import com.palaspapas.back.persistence.repositories.IRoleRepository;
import com.palaspapas.back.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;
    private final IRoleMapper roleMapper;

    @Override
    @Transactional
    public Role createRole(Role role) {
        validateRoleName(role.getName());
        var roleEntity = roleMapper.toEntity(role);
        var savedRole = roleRepository.save(roleEntity);
        return roleMapper.toDomain(savedRole);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Role> findAll() {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Role addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        return null;
    }

    private void validateRoleName(String name) {
        if (roleRepository.existsByName(name)) {
            throw new BusinessException("Role name already exists");
        }
    }
}