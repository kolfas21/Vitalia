package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;

import java.util.Optional;

public interface ConsultaMedicaService {
    ConsultaMedicaEntity crearConsulta(ConsultaRequestDTO dto);
    Optional<ConsultaMedicaEntity> buscarPorId(Long idConsulta);
}