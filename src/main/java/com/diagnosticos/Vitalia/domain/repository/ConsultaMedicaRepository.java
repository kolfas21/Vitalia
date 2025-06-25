package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedicaEntity, Long> {
}
