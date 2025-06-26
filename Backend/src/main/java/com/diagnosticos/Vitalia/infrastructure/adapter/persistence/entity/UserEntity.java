package com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    // Relación con paciente (no afecta a médico ni otros que usen esta entidad)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private PacienteEntity paciente;

    // Puedes agregar relaciones con médico u otras entidades aquí si las necesitas
}