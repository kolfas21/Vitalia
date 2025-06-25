package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.ConsultaMedicaService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ConsultaController {

    private final ConsultaMedicaService consultaService;

    public ConsultaController(ConsultaMedicaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/consulta")
    public ResponseEntity<?> registrarConsulta(@RequestBody ConsultaRequestDTO request) {
        try {
            ConsultaMedicaEntity consulta = consultaService.crearConsulta(request);

            Map<String, Object> response = new HashMap<>();
            response.put("idConsulta", consulta.getIdConsulta());
            response.put("nombreMedico", consulta.getMedico().getNombre());
            response.put("especialidad", consulta.getMedico().getEspecialidad());
            response.put("fechaConsulta", consulta.getFechaHora());

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }

}
