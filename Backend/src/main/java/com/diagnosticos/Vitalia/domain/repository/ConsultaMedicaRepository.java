package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedicaEntity, Long> {
    boolean existsByMedicoAndFechaHora(MedicoEntity medico, LocalDateTime fechaHora);
    List<ConsultaMedicaEntity> findByMedico_Id(Long idMedico);
}
