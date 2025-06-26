package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.ConsultaMedicaRepository;
import com.diagnosticos.Vitalia.domain.repository.MedicoRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaMedicaServiceImpl implements ConsultaMedicaService {

    private final ConsultaMedicaRepository consultaRepository;
    private final UserRepository userRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaMedicaServiceImpl(ConsultaMedicaRepository consultaRepository,
                                     UserRepository userRepository,
                                     MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.userRepository = userRepository;
        this.medicoRepository = medicoRepository;
    }

    @Override
    public ConsultaMedicaEntity crearConsulta(ConsultaRequestDTO request) {
        if (request.getFechaConsulta().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha debe ser futura");
        }

        UserEntity usuario = userRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + request.getIdUsuario()));

        if (!"paciente".equalsIgnoreCase(usuario.getRol())) {
            throw new IllegalArgumentException("El usuario debe tener rol 'paciente' para crear una consulta");
        }

        MedicoEntity medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con id: " + request.getIdMedico()));

        if (!"medico".equalsIgnoreCase(medico.getUser().getRol())) {
            throw new IllegalArgumentException("El usuario asociado al médico no tiene rol 'medico'");
        }

        if (consultaRepository.existsByMedicoAndFechaHora(medico, request.getFechaConsulta())) {
            throw new IllegalArgumentException("El médico ya tiene una consulta programada para esa fecha y hora");
        }

        ConsultaMedicaEntity consulta = new ConsultaMedicaEntity();
        consulta.setUsuario(usuario);
        consulta.setMedico(medico);
        consulta.setFechaHora(request.getFechaConsulta());
        consulta.setFechaCreacion(LocalDateTime.now());

        return consultaRepository.save(consulta);
    }

    @Override
    public Optional<ConsultaMedicaEntity> buscarPorId(Long idConsulta) {
        return consultaRepository.findById(idConsulta);
    }

    @Override
    public List<ConsultaMedicaEntity> buscarConsultasPorMedico(Long idMedico) {
        if (!medicoRepository.existsById(idMedico)) {
            throw new EntityNotFoundException("Médico no encontrado con id: " + idMedico);
        }

        return consultaRepository.findByMedico_Id(idMedico);
    }

}