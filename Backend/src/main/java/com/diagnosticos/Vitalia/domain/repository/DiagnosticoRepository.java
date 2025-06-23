package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.DiagnosticoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticoRepository extends JpaRepository<DiagnosticoEntity, Long> {
}