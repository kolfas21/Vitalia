package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity login(String correo, String contrasena) {
        UserEntity user = userRepository.findByCorreo(correo);
        if (user == null || !passwordEncoder.matches(contrasena, user.getContrasena())) {
            throw new IllegalArgumentException("Correo o contraseña inválidos");
        }
        return user;
    }
}