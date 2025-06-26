package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ConsultaDetailDTO {
    private Long idConsulta;
    private String nombre;
    private String correo;
    private String cedula;
    private String fechaNacimiento;

    private String sintomas;
    private String alergias;
    private String actividadFisica;
    private String ocupacion;
    private Double peso;
    private Double estatura;
}