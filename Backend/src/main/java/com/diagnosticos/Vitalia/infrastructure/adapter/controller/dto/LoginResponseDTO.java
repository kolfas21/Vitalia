package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String mensaje;
    private String token;
    private Long id;
    private String nombre;
    private String cedula;
    private String correo;
    private String rol;
    private LocalDate fechaNacimiento; // ⛔ Falta este en el constructor

}