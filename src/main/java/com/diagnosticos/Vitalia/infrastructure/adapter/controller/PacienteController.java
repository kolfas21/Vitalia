package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.PacienteService;
import com.diagnosticos.Vitalia.application.service.UserService;
import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarInfoMedicaDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.PacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarPacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final UserService userService;
    private final UserRepository userRepo;
    private final PacienteRepository pacienteRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarPaciente(@RequestBody PacienteDTO dto) {
        try {
            pacienteService.registrarPaciente(dto); // ✅ Usa la lógica centralizada que sí guarda alergias y síntomas
            return ResponseEntity.ok("✅ Paciente registrado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<List<PacienteEntity>> obtenerTodos() {
        return ResponseEntity.ok(pacienteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteEntity> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody ActualizarPacienteDTO dto) {
        pacienteService.actualizarPaciente(id, dto);
        return ResponseEntity.ok("✅ Paciente actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.ok("✅ Paciente eliminado correctamente");
    }

    @PutMapping("/{id}/info-medica")
    public ResponseEntity<String> actualizarInfoMedica(@PathVariable Long id, @RequestBody ActualizarInfoMedicaDTO dto) {
        userService.actualizarInfoMedica(id, dto);
        return ResponseEntity.ok("Información médica del paciente actualizada");
    }
}
