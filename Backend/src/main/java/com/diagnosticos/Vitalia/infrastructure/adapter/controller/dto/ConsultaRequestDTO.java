package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;

@Data
public class ConsultaRequestDTO {
    private Long idPaciente;
    private Long idMedico;
    // Agrega aquí otros campos necesarios para la consulta
}