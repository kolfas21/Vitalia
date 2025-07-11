package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.UserService;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.RegistroPacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarInfoMedicaDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;
    private final UserService userService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        if (userRepo.existsByCorreo(dto.getCorreo())) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Correo ya registrado"));
        }
        if (userRepo.existsByCedula(dto.getCedula())) {
            return ResponseEntity.badRequest().body(Map.of("error", "❌ Cédula ya registrada"));
        }

        try {
            userService.registrarPaciente(dto);
            return ResponseEntity.ok(Map.of("mensaje", "✅ Paciente registrado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UserEntity datosActualizados
    ) {
        try {
            UserEntity usuarioActualizado = userService.actualizarUsuario(id, datosActualizados);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> obtenerTodosLosUsuarios() {
        List<UserEntity> usuarios = userService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> obtenerUsuarioPorId(@PathVariable Long id) {
        return userService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<UserEntity> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        return userService.obtenerUsuarioPorCedula(cedula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            UserEntity usuarioEliminado = userService.eliminarUsuario(id);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "✅ Usuario eliminado correctamente",
                    "usuario", usuarioEliminado
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}