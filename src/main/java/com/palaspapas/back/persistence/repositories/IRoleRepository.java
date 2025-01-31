package com.palaspapas.back.persistence.repositories;

import com.palaspapas.back.persistence.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String user);
    boolean existsByName(String name);
}
