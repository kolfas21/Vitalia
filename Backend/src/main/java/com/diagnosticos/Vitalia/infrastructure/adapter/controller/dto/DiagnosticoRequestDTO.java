package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class DiagnosticoRequestDTO {
    private Long idConsulta;
    private List<String> sintomas;
}