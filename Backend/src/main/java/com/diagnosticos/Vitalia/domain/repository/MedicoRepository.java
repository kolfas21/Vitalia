package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {
    Optional<MedicoEntity> findById(Long id);
    void deleteById(Long id);
    List<MedicoEntity> findAll();
    Optional<MedicoEntity> findByUserId(Long userId);
    @Query("SELECT m FROM MedicoEntity m WHERE m.user.cedula = :cedula")
    Optional<MedicoEntity> findByUserCedula(@Param("cedula") String cedula);



}