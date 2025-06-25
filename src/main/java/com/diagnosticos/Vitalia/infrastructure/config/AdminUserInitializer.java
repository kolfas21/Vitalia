package com.diagnosticos.Vitalia.infrastructure.config;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;



    @PostConstruct
    public void createDefaultAdmin() {
        String adminEmail = "admin@demo.com";
        if (!userRepository.existsByCorreo(adminEmail)) {
            UserEntity admin = new UserEntity();
            admin.setNombre("admin");
            admin.setCorreo(adminEmail);
            admin.setContrasena(passwordEncoder.encode("admin123")); // o tu contraseña preferida
            admin.setCedula("111111111");
            admin.setFechaNacimiento(java.time.LocalDate.of(1995, 4, 30));
            admin.setRol("ADMIN");
            userRepository.save(admin);
            System.out.println("Usuario admin creado automáticamente");
        }
    }
}