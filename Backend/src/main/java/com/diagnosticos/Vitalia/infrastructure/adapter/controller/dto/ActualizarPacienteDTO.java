// ActualizarPacienteDTO.java
package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;

@Data
public class ActualizarPacienteDTO {
    private String nombre;
    private String correo;
    private String ocupacion;
    private String actividadFisica;
    private Double peso;
    private Double estatura;
}
