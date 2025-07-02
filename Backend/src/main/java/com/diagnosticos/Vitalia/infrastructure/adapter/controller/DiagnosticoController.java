package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.GenerarDiagnosticoService;
import com.diagnosticos.Vitalia.domain.model.Usuario;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.DiagnosticoRespuestaDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.DiagnosticoSolicitudDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import com.diagnosticos.Vitalia.infrastructure.strategy.DiagnosticoContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


/**
 * Controller responsable de manejar la petición HTTP del diagnóstico.
 * Está en la capa de infraestructura (entrada/salida del sistema).
 * Patrón "Controller" y "DTO mapping".
 */


import java.util.*;

@RestController
@RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoContext diagnosticoContext;

    // Implementación en memoria
    private final Map<String, Usuario> usuarioDB = new HashMap<>();

    // POST para crear un usuario
    @PostMapping("/usuario")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        usuarioDB.put(usuario.getNombre(), usuario);
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    // GET para traer todos los usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>(usuarioDB.values());
        return ResponseEntity.ok(usuarios);
    }

    private final GenerarDiagnosticoService service;

    @PostMapping("/generar")
    public DiagnosticoRespuestaDTO generarDiagnostico(@RequestBody DiagnosticoSolicitudDTO dto) {
        return service.generarDiagnostico(dto);
    }

    @RestController
    @RequestMapping("/api")
    @RequiredArgsConstructor
    public static class LoginController {

        private final UserRepository userRepo;
        private final PasswordEncoder passwordEncoder;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
            String correo = credentials.get("correo");
            String contrasena = credentials.get("contrasena");

            UserEntity user = userRepo.findByCorreo(correo);
            if (user == null) {
                throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
            }
            // Devuelve datos útiles para Angular
            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "nombre", user.getNombre(),
                    "correo", user.getCorreo(),
                    "rol", user.getRol()
            ));
        }
    }
}