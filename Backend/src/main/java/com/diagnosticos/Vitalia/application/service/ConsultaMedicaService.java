package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import java.util.Optional;

public interface ConsultaMedicaService {

    /**
     * Crea y persiste una nueva consulta médica según las reglas de negocio.
     * @param dto información de paciente, médico y fecha/hora.
     * @return la entidad guardada con ID asignado.
     */
    ConsultaMedicaEntity crearConsulta(ConsultaRequestDTO dto);

    /**
     * Busca una consulta por su identificador.
     * @param idConsulta ID de la consulta.
     * @return Optional con la entidad si existe.
     */
    Optional<ConsultaMedicaEntity> buscarPorId(Long idConsulta);
}
