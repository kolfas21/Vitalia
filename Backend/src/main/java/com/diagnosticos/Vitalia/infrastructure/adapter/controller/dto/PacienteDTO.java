// PacienteDTO.java
package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PacienteDTO {
    private String nombre;
    private String cedula;
    private String correo;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String ocupacion;
    private String actividadFisica;
    private Double peso;
    private Double estatura;
}
