package com.diagnosticos.Vitalia.application.service;



import com.diagnosticos.Vitalia.domain.repository.ConsultaMedicaRepository;
import com.diagnosticos.Vitalia.domain.repository.MedicoRepository;
import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.application.service.ConsultaMedicaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultaMedicaServiceImpl implements ConsultaMedicaService {

    private final ConsultaMedicaRepository consultaRepo;
    private final PacienteRepository pacienteRepo;
    private final MedicoRepository medicoRepo;

    @Override
    public ConsultaMedicaEntity crearConsulta(ConsultaRequestDTO dto) {
        PacienteEntity paciente = pacienteRepo.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("❌ Paciente no encontrado con ID: " + dto.getIdPaciente()));

        MedicoEntity medico = medicoRepo.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("❌ Médico no encontrado con ID: " + dto.getIdMedico()));

        ConsultaMedicaEntity consulta = new ConsultaMedicaEntity(
                null,
                LocalDate.now(),
                paciente,
                medico
        );

        return consultaRepo.save(consulta);
    }

    @Override
    public Optional<ConsultaMedicaEntity> buscarPorId(Long idConsulta) {
        return consultaRepo.findById(idConsulta);
    }
}