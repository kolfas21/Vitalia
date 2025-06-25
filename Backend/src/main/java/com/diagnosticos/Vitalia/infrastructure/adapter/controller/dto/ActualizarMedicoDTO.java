// ActualizarMedicoDTO.java
package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;

@Data
public class ActualizarMedicoDTO {
    private String nombre;
    private String cedula;
    private String correo;
    private String especialidad;
    private String registroProfesional;
}
