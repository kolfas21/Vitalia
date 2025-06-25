package com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diagnostico")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DiagnosticoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDiagnostico;

    private String resultado;
    private Float probabilidad;

    @ManyToOne
    @JoinColumn(name = "idConsulta")
    private ConsultaMedicaEntity consulta;
}
