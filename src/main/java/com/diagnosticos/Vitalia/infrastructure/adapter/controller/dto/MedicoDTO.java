
package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;

@Data
public class MedicoDTO {
    private String nombre;
    private String cedula;
    private String correo;
    private String contrasena;
    private String especialidad;
    private String registroProfesional;
}
