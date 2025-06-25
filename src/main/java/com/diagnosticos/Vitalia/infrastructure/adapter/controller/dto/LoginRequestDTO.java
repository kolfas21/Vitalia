package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String correo;
    private String contrasena;
}