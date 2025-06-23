package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.PacienteService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.PacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarPacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody PacienteDTO dto) {
        pacienteService.registrarPaciente(dto);
        return ResponseEntity.ok("✅ Paciente registrado correctamente");
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
}
