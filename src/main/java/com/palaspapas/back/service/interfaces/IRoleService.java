package com.palaspapas.back.service.interfaces;

import com.palaspapas.back.domain.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IRoleService {
    Role createRole(Role role);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    Role update(Role role);
    void delete(Long id);
    Role addPermissionsToRole(Long roleId, Set<Long> permissionIds);
}
