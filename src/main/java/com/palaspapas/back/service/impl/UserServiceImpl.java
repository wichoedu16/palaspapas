package com.palaspapas.back.service.impl;

import com.palaspapas.back.domain.User;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.exception.NotFoundException;
import com.palaspapas.back.persistence.entities.RoleEntity;
import com.palaspapas.back.persistence.entities.UserEntity;
import com.palaspapas.back.persistence.mappers.IUserMapper;
import com.palaspapas.back.persistence.repositories.IRoleRepository;
import com.palaspapas.back.persistence.repositories.IUserRepository;
import com.palaspapas.back.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User create(User user) {
        validateNewUser(user);

        UserEntity userEntity = userMapper.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity roleEntity = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new NotFoundException("Role not found"));
        userEntity.setRole(roleEntity);

        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDomain(savedUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    private void validateNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already exists");
        }
    }

    @Override
    @Transactional
    public User assignRole(Long userId, Long roleId) {
        var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var roleEntity = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found"));

        userEntity.setRole(roleEntity);
        var updatedUser = userRepository.save(userEntity);
        return userMapper.toDomain(updatedUser);
    }
}