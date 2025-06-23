package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;
import lombok.Data;

@Data
public class ActualizarInfoMedicaDTO {
    private Double peso;
    private Double estatura;
    private String actividadFisica;
    private String alergias;
    private String sintomas;
}