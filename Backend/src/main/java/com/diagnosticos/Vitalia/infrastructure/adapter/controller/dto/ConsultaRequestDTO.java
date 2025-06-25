package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ConsultaRequestDTO {
    private Long idUsuario;
    private Long idMedico;
    private LocalDateTime fechaConsulta;

}
