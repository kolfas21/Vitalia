package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RegistroPacienteDTO {
    private String nombre;
    private String cedula;
    private String correo;
    private String contrasena;
    private LocalDate fechaNacimiento;
}