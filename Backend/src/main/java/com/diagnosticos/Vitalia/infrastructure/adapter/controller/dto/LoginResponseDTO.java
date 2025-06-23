package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private String mensaje;
    private String rol;
    private Long id;
}