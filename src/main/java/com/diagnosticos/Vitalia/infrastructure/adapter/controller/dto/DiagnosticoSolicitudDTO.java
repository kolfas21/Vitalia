package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class DiagnosticoSolicitudDTO {
    private Long idConsulta;
    private String id;
    private String nombre;
    private int edad;
    private String genero;
    private List<String> sintomas;
}