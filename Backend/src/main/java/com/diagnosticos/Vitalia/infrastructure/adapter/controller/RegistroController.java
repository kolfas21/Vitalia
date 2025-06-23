package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

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


}