package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.ConsultaMedicaRepository;
import com.diagnosticos.Vitalia.domain.repository.MedicoRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ConsultaRequestDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity.TipoConsulta;
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
    private final PacienteRepository pacienteRepository;

    public ConsultaMedicaServiceImpl(ConsultaMedicaRepository consultaRepository,
                                     UserRepository userRepository,
                                     MedicoRepository medicoRepository,
                                     PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.userRepository = userRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public ConsultaMedicaEntity crearConsulta(ConsultaRequestDTO request) {
        // Validar fecha futura
        if (request.getFechaConsulta().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha debe ser futura");
        }

        // Buscar usuario solicitante
        UserEntity user = userRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + request.getIdUsuario()));

        // Buscar médico
        MedicoEntity medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con id: " + request.getIdMedico()));

        ConsultaMedicaEntity consulta = new ConsultaMedicaEntity();
        consulta.setMedico(medico);
        consulta.setFechaHora(request.getFechaConsulta());
        consulta.setUsuario(user);

        // Determinar tipo de consulta y validar según el caso
        if (request.getTipoConsulta() == TipoConsulta.MEDICO_MEDICO) {
            // Validar que el usuario solicitante sea médico
            if (!esMedico(user)) {
                throw new IllegalArgumentException("Solo los médicos pueden solicitar interconsultas");
            }
            consulta.setTipoConsulta(TipoConsulta.MEDICO_MEDICO);
            consulta.setPaciente(null); // No hay paciente en interconsultas

        } else if (request.getTipoConsulta() == TipoConsulta.PACIENTE_MEDICO) {
            // Buscar paciente asociado al usuario o por ID específico
            PacienteEntity paciente = null;

            if (request.getIdPaciente() != null) {
                // Si se especifica un ID de paciente específico
                paciente = pacienteRepository.findById(request.getIdPaciente())
                        .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con id: " + request.getIdPaciente()));
            } else {
                // Buscar paciente asociado al usuario
                paciente = user.getPaciente();
                if (paciente == null) {
                    throw new EntityNotFoundException("El usuario no tiene un paciente asociado");
                }
            }

            consulta.setTipoConsulta(TipoConsulta.PACIENTE_MEDICO);
            consulta.setPaciente(paciente);

        } else {
            throw new IllegalArgumentException("Tipo de consulta no válido");
        }

        return consultaRepository.save(consulta);
    }

    // Método auxiliar para verificar si un usuario es médico
    private boolean esMedico(UserEntity user) {
        // Asumiendo que tienes un campo 'rol' en UserEntity
        // Ajusta según tu implementación específica
        return "MEDICO".equals(user.getRol()) || user.getMedico() != null;
    }

    @Override
    public Optional<ConsultaMedicaEntity> buscarPorId(Long idConsulta) {
        return consultaRepository.findById(idConsulta);
    }