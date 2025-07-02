package com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos básicos (registro)
    private String nombre;
    private String cedula;
    private String correo;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private String rol;



    // ... getters y setters
}