package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.ConsultaMedicaService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarInfoMedicaDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaDetailDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaResponseDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import com.diagnosticos.Vitalia.application.service.PacienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ConsultaController {

    private final ConsultaMedicaService consultaService;
    private final PacienteService pacienteService;

    public ConsultaController(ConsultaMedicaService consultaService, PacienteService pacienteService) {
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
    }

    @PostMapping("/consulta")
    public ResponseEntity<?> registrarConsulta(@RequestBody ConsultaRequestDTO request) {
        try {
            ConsultaMedicaEntity consulta = consultaService.crearConsulta(request);

            ConsultaResponseDTO response = new ConsultaResponseDTO(
                    consulta.getIdConsulta(),
                    consulta.getUsuario().getNombre(),
                    consulta.getUsuario().getCedula(),
                    consulta.getMedico().getNombre(),
                    consulta.getMedico().getEspecialidad(),
                    consulta.getFechaHora()
            );

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }

    @GetMapping("/consulta/{id}")
    public ResponseEntity<?> obtenerConsulta(@PathVariable Long id) {
        try {
            ConsultaMedicaEntity consulta = consultaService.buscarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada con id: " + id));

            ConsultaResponseDTO response = new ConsultaResponseDTO(
                    consulta.getIdConsulta(),
                    consulta.getUsuario().getNombre(),
                    consulta.getUsuario().getCedula(),
                    consulta.getMedico().getNombre(),
                    consulta.getMedico().getEspecialidad(),
                    consulta.getFechaHora()
            );

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }

    @GetMapping("/consulta/medico/{idMedico}")
    public ResponseEntity<?> obtenerConsultasPorMedico(@PathVariable Long idMedico) {
        try {
            var consultas = consultaService.buscarConsultasPorMedico(idMedico);

            var respuesta = consultas.stream().map(consulta -> new ConsultaResponseDTO(
                    consulta.getIdConsulta(),
                    consulta.getUsuario().getNombre(),
                    consulta.getUsuario().getCedula(),
                    consulta.getMedico().getNombre(),
                    consulta.getMedico().getEspecialidad(),
                    consulta.getFechaHora()
            )).toList();

            return ResponseEntity.ok(respuesta);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }

    @GetMapping("/consulta/{id}/detalle")
    public ResponseEntity<ConsultaDetailDTO> verDetalleConsulta(@PathVariable Long id) {
        ConsultaMedicaEntity consulta = consultaService.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada"));

        UserEntity usuario = consulta.getUsuario();
        PacienteEntity paciente = usuario.getPaciente(); // puede ser null

        ConsultaDetailDTO detalle = new ConsultaDetailDTO(
                consulta.getIdConsulta(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getCedula(),
                usuario.getFechaNacimiento().toString(),
                paciente != null ? paciente.getSintomas() : null,
                paciente != null ? paciente.getAlergias() : null,
                paciente != null ? paciente.getActividadFisica() : null,
                paciente != null ? paciente.getOcupacion() : null,
                paciente != null ? paciente.getPeso() : null,
                paciente != null ? paciente.getEstatura() : null
        );

        return ResponseEntity.ok(detalle);
    }

    @PutMapping("/consulta/{id}/paciente")
    public ResponseEntity<String> actualizarInfoPacienteDesdeConsulta(@PathVariable Long id, @RequestBody ActualizarInfoMedicaDTO dto) {
        ConsultaMedicaEntity consulta = consultaService.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada"));

        UserEntity usuario = consulta.getUsuario();

        pacienteService.crearOActualizarDesdeConsulta(usuario.getId(), dto);

        return ResponseEntity.ok("✅ Información médica actualizada");
    }
}