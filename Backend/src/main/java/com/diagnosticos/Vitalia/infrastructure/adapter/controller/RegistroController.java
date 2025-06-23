package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarMedicoDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.PacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.MedicoDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.application.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Necesitarás agregar BCryptPasswordEncoder bean
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

@RestController
@RequestMapping("/api/registro")
@RequiredArgsConstructor
public class RegistroController {

    private final UserRepository userRepo;
    private final PacienteRepository pacienteRepo;
    private final PasswordEncoder passwordEncoder;
    private final MedicoService medicoService; // Agregado MedicoService

    @PostMapping("/paciente")
    public ResponseEntity<String> registrarPaciente(@RequestBody PacienteDTO dto) {
        if (userRepo.existsByCorreo(dto.getCorreo())) {
            return ResponseEntity.badRequest().body("❌ Correo ya registrado");
        }
        if (userRepo.existsByCedula(dto.getCedula())) {
            return ResponseEntity.badRequest().body("❌ Cédula ya registrada");
        }
        UserEntity user = new UserEntity();
        user.setNombre(dto.getNombre());
        user.setCedula(dto.getCedula());
        user.setCorreo(dto.getCorreo());
        user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
// Corregido: si ya es LocalDate, no parsear
        user.setFechaNacimiento(dto.getFechaNacimiento());
        user.setRol("PACIENTE");
        user = userRepo.save(user);
        PacienteEntity paciente = new PacienteEntity();
        paciente.setUser(user);
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setEstadoCivil(dto.getEstadoCivil());
        paciente.setOcupacion(dto.getOcupacion());
        paciente.setActividadFisica(dto.getActividadFisica());
        paciente.setPeso(dto.getPeso());
        paciente.setEstatura(dto.getEstatura());
        pacienteRepo.save(paciente);
        return ResponseEntity.ok("✅ Paciente registrado correctamente");
    }

    @PostMapping("/medico")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarMedico(@RequestBody MedicoDTO dto) {
        try {
            medicoService.registrarMedico(dto);
            return ResponseEntity.ok(Map.of("mensaje", "Médico registrado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/medico/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarMedico(
            @PathVariable Long id,
            @RequestBody ActualizarMedicoDTO dto) {
        try {
            medicoService.actualizarMedico(id, dto);
            return ResponseEntity.ok("✅ Médico actualizado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @DeleteMapping("/medico/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarMedico(@PathVariable Long id) {
        try {
            medicoService.eliminarMedico(id);
            return ResponseEntity.ok("✅ Médico eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Error al eliminar médico: " + e.getMessage());
        }
    }

    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')") // Si solo el súper admin puede crear admins
    public ResponseEntity<?> registrarAdmin(@RequestBody MedicoDTO dto) {
        try {
            if (userRepo.existsByCorreo(dto.getCorreo())) {
                return ResponseEntity.badRequest().body(Map.of("error", "❌ Correo ya registrado"));
            }
            if (userRepo.existsByCedula(dto.getCedula())) {
                return ResponseEntity.badRequest().body(Map.of("error", "❌ Cédula ya registrada"));
            }

            UserEntity user = new UserEntity();
            user.setNombre(dto.getNombre());
            user.setCedula(dto.getCedula());
            user.setCorreo(dto.getCorreo());
            user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            user.setRol("ADMIN");
            userRepo.save(user);

            return ResponseEntity.ok(Map.of("mensaje", "✅ Administrador registrado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "❌ Error al registrar: " + e.getMessage()));
        }
    }

    @PutMapping("/admins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarAdmin(@PathVariable Long id, @RequestBody ActualizarMedicoDTO dto) {
        try {
            UserEntity admin = userRepo.findById(id)
                    .filter(u -> "ADMIN".equals(u.getRol()))
                    .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado con id: " + id));

            admin.setNombre(dto.getNombre());
            admin.setCorreo(dto.getCorreo());
            admin.setCedula(dto.getCedula());
            userRepo.save(admin);

            return ResponseEntity.ok("✅ Administrador actualizado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @DeleteMapping("/admins/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarAdmin(@PathVariable Long id) {
        try {
            UserEntity admin = userRepo.findById(id)
                    .filter(u -> "ADMIN".equals(u.getRol()))
                    .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado con id: " + id));

            userRepo.delete(admin);
            return ResponseEntity.ok("✅ Administrador eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }








}