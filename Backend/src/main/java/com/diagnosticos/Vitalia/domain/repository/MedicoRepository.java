package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {
    Optional<MedicoEntity> findById(Long id);
    void deleteById(Long id);
    List<MedicoEntity> findAll();
    Optional<MedicoEntity> findByUserCedula(String cedula);

}