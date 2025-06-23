package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.MedicoService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.MedicoDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarMedicoDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody MedicoDTO dto) {
        medicoService.registrarMedico(dto);
        return ResponseEntity.ok("✅ Médico registrado correctamente");
    }

    @GetMapping
    public ResponseEntity<List<MedicoEntity>> obtenerTodos() {
        return ResponseEntity.ok(medicoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoEntity> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody ActualizarMedicoDTO dto) {
        medicoService.actualizarMedico(id, dto);
        return ResponseEntity.ok("✅ Médico actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        medicoService.eliminarMedico(id);
        return ResponseEntity.ok("✅ Médico eliminado correctamente");
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<MedicoEntity> obtenerPorCedula(@PathVariable String cedula) {
        return ResponseEntity.ok(medicoService.obtenerPorCedula(cedula));
    }
}
