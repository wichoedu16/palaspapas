package com.palaspapas.back.config;
import com.palaspapas.back.persistence.entities.UserEntity;
import com.palaspapas.back.persistence.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (shouldCreateAdminUser()) {
            createAdminIfNotExists();
        }
    }

    private boolean shouldCreateAdminUser() {
        String[] activeProfiles = environment.getActiveProfiles();
        return Arrays.asList(activeProfiles).contains("dev") ||
                "true".equals(environment.getProperty("app.create-admin"));
    }

    private void createAdminIfNotExists() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .firstName("Admin")
                    .lastName("System")
                    .email("admin@palaspapas.com")
                    .role("ADMIN")
                    .status(true)
                    .build();

            userRepository.save(admin);
            log.info("Usuario administrador creado exitosamente: {}", adminUsername);
        }
    }
}