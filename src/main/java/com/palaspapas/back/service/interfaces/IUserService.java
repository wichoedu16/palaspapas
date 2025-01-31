package com.palaspapas.back.service.interfaces;

import com.palaspapas.back.domain.User;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    User create(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    User update(User user);
    void delete(Long id);
    User assignRole(Long userId, Long roleId);
}