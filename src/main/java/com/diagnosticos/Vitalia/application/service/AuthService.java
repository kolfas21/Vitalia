package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.LoginResponseDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import com.diagnosticos.Vitalia.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(String correo, String contrasena) {
        System.out.println("📩 Correo recibido: " + correo);
        System.out.println("🔑 Contraseña recibida: " + contrasena);

        UserEntity user = userRepository.findByCorreo(correo);

        if (user == null) {
            System.out.println("❌ Usuario no encontrado");
            throw new IllegalArgumentException("Correo o contraseña inválidos");
        }

        System.out.println("🧾 Contraseña almacenada: " + user.getContrasena());

        boolean passwordValida = passwordEncoder.matches(contrasena, user.getContrasena());
        System.out.println("🔍 ¿Contraseña válida?: " + passwordValida);

        if (!passwordValida) {
            System.out.println("❌ Contraseña incorrecta");
            throw new IllegalArgumentException("Correo o contraseña inválidos");
        }

        String token = jwtUtil.generateToken(user.getCorreo());
        System.out.println("✅ Token generado: " + token);

        return new LoginResponseDTO(
                "✅ Inicio de sesión exitoso",
                token,
                user.getId(),
                user.getNombre(),
                user.getCedula(),
                user.getCorreo(),
                user.getRol(),
                user.getFechaNacimiento() // ✅ ¡ESTO FALTABA!
        );
    }
}