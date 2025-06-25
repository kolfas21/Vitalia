package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.ConsultaMedicaRepository;
import com.diagnosticos.Vitalia.domain.repository.MedicoRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        UserEntity user = userRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + request.getIdUsuario()));

        PacienteEntity paciente = user.getPaciente();
        if (paciente == null) {
            throw new EntityNotFoundException("El usuario no tiene un paciente asociado");
        }

        MedicoEntity medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con id: " + request.getIdMedico()));

        ConsultaMedicaEntity consulta = new ConsultaMedicaEntity();
        consulta.setPaciente(paciente); // ✅
        consulta.setMedico(medico);
        consulta.setFechaHora(request.getFechaConsulta());
        consulta.setUsuario(user);// ✅

        return consultaRepository.save(consulta);
    }

    @Override
    public Optional<ConsultaMedicaEntity> buscarPorId(Long idConsulta) {
        return consultaRepository.findById(idConsulta);
    }

}
