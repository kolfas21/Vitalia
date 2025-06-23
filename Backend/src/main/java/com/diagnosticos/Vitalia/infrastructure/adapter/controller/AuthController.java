package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.AuthService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.LoginRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.LoginResponseDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            UserEntity user = authService.login(request.getCorreo(), request.getContrasena());
            return ResponseEntity.ok(new LoginResponseDTO("✅ Inicio de sesión exitoso", user.getRol(), user.getId()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
        }
    }
}