package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.ConsultaMedicaService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.domain.repository.ConsultaMedicaRepository;
import com.diagnosticos.Vitalia.domain.repository.MedicoRepository;
import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/consulta")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaMedicaService consultaService;

    @PostMapping
    public ResponseEntity<?> crearConsulta(@RequestBody ConsultaRequestDTO dto) {
        try {
            ConsultaMedicaEntity consulta = consultaService.crearConsulta(dto);
            return ResponseEntity.ok("Consulta creada correctamente con ID: " + consulta.getIdConsulta());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}