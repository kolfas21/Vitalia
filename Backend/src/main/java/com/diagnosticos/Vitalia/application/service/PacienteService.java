package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarInfoMedicaDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.PacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarPacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;

import java.util.List;

public interface PacienteService {
    void registrarPaciente(PacienteDTO dto);
    List<PacienteEntity> obtenerTodos();
    PacienteEntity obtenerPorId(Long id);
    void actualizarPaciente(Long id, ActualizarPacienteDTO dto);
    void eliminarPaciente(Long id);
    void crearOActualizarDesdeConsulta(Long userId, ActualizarInfoMedicaDTO dto);
}