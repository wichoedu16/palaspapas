package com.palaspapas.back.config;

import com.palaspapas.back.exception.DuplicateResourceException;
import com.palaspapas.back.persistence.entities.PermissionEntity;
import com.palaspapas.back.persistence.entities.RoleEntity;
import com.palaspapas.back.persistence.entities.UserEntity;
import com.palaspapas.back.persistence.repositories.IPermissionRepository;
import com.palaspapas.back.persistence.repositories.IRoleRepository;
import com.palaspapas.back.persistence.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!prod") // No ejecutar en producción
public class DataInitializer implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    private static final Map<String, String> INITIAL_PERMISSIONS = Map.of(
            "ADMIN_READ", "Admin Read Access",
            "ADMIN_WRITE", "Admin Write Access",
            "ADMIN_DELETE", "Admin Delete Access",
            "USER_READ", "User Read Access",
            "USER_WRITE", "User Write Access",
            "USER_DELETE", "User Delete Access",
            "PRODUCT_READ", "Product Read Access",
            "PRODUCT_WRITE", "Product Write Access",
            "PRODUCT_DELETE", "Product Delete Access"
    );

    @Override
    @Transactional
    public void run(String... args) {
        long startTime = System.currentTimeMillis();
        log.info("Iniciando carga de datos iniciales...");

        try {
            // Verificar si ya existen datos
            if (userRepository.count() > 0) {
                log.info("Los datos iniciales ya existen. Saltando inicialización.");
                return;
            }

            Set<PermissionEntity> permissions = createInitialPermissions();
            RoleEntity adminRole = createAdminRole(permissions);
            createAdminUser(adminRole);

            log.info("Datos iniciales creados exitosamente en {} ms",
                    System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("Error al crear datos iniciales", e);
            throw new DuplicateResourceException("Error en la inicialización de datos");
        }
    }

    private Set<PermissionEntity> createInitialPermissions() {
        List<PermissionEntity> permissions = new ArrayList<>();

        INITIAL_PERMISSIONS.forEach((code, name) -> {
            PermissionEntity permission = new PermissionEntity();
            permission.setCode(code);
            permission.setName(name);
            permission.setStatus(true);
            permissions.add(permission);
        });

        return new HashSet<>(permissionRepository.saveAll(permissions));
    }

    private RoleEntity createAdminRole(Set<PermissionEntity> permissions) {
        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setDescription("Administrator role with full access");
        adminRole.setPermissions(permissions);
        adminRole.setStatus(true);
        return roleRepository.save(adminRole);
    }

    private void createAdminUser(RoleEntity adminRole) {
        UserEntity admin = new UserEntity();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstName("Admin");
        admin.setLastName("System");
        admin.setEmail("admin@palaspapas.com");
        admin.setRole(adminRole);
        admin.setStatus(true);

        userRepository.save(admin);
        log.info("Usuario administrador creado exitosamente: {}", adminUsername);
    }
}