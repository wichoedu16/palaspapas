package com.palaspapas.back.service.impl;

import com.palaspapas.back.domain.Permission;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.exception.NotFoundException;
import com.palaspapas.back.persistence.mappers.IPermissionMapper;
import com.palaspapas.back.persistence.repositories.IPermissionRepository;
import com.palaspapas.back.service.interfaces.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService {

    private final IPermissionRepository permissionRepository;
    private final IPermissionMapper permissionMapper;

    @Override
    @Transactional
    public Permission create(Permission permission) {
        validateNewPermission(permission);
        var entity = permissionMapper.toEntity(permission);
        var savedEntity = permissionRepository.save(entity);
        return permissionMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findByCode(String code) {
        return permissionRepository.findByCode(code)
                .map(permissionMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Permission> findByCodeIn(Set<String> codes) {
        return permissionMapper.toDomainSet(permissionRepository.findByCodeIn(codes));
    }

    @Override
    @Transactional
    public Permission update(Permission permission) {
        if (!permissionRepository.existsById(permission.getId())) {
            throw new NotFoundException("Permission not found");
        }
        var entity = permissionMapper.toEntity(permission);
        var updatedEntity = permissionRepository.save(entity);
        return permissionMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new NotFoundException("Permission not found");
        }
        permissionRepository.deleteById(id);
    }

    private void validateNewPermission(Permission permission) {
        if (permissionRepository.existsByCode(permission.getCode())) {
            throw new BusinessException("Permission code already exists");
        }
    }
}