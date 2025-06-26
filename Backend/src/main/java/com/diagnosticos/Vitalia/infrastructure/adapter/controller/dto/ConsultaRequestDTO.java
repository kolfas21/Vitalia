package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaRequestDTO {
    private Long idUsuario;      // Usuario con rol "paciente"
    private Long idMedico;       // Médico que atenderá
    private LocalDateTime fechaConsulta;
}