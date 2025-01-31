package com.palaspapas.back.persistence.repositories;

import com.palaspapas.back.persistence.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;

public interface IPermissionRepository extends JpaRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByCode(String code);
    boolean existsByCode(String code);
    Set<PermissionEntity> findByCodeIn(Set<String> codes);
}