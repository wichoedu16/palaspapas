package com.palaspapas.back.service.interfaces;


import com.palaspapas.back.domain.Permission;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IPermissionService {
    Permission create(Permission permission);
    Optional<Permission> findById(Long id);
    Optional<Permission> findByCode(String code);
    List<Permission> findAll();
    Set<Permission> findByCodeIn(Set<String> codes);
    Permission update(Permission permission);
    void delete(Long id);
}