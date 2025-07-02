package com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PacienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @OneToOne
    @JoinColumn(name = "id_user") // CORRECTO
    private UserEntity user;

    private LocalDate fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String ocupacion;
    private String actividadFisica;
    private Double peso;
    private Double estatura;
    private String alergias;
    private String sintomas;
}