package com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Long id;


    private String especialidad;

    private String registroProfesional;

    // Relación con el usuario (FK al que debe tener rol = "medico")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    // Métodos de acceso convenientes (opcional pero útil)

    public String getNombre() {
        return user != null ? user.getNombre() : null;
    }

    public String getCorreo() {
        return user != null ? user.getCorreo() : null;
    }

    public String getCedula() {
        return user != null ? user.getCedula() : null;
    }

    public String getRol() {
        return user != null ? user.getRol() : null;
    }
}