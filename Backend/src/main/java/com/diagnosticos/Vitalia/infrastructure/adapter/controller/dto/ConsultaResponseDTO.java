package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConsultaResponseDTO {
    private Long idConsulta;
    private String nombrePaciente;
    private String cedulaPaciente;
    private String nombreMedico;
    private String especialidad;
    private LocalDateTime fechaConsulta;
}