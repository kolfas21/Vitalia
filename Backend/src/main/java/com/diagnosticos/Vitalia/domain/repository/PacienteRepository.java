package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {
    // Si necesitas métodos personalizados, puedes agregarlos aquí
}