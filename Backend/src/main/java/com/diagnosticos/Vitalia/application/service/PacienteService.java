package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.PacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarPacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public void registrarPaciente(PacienteDTO dto) {
        if (userRepo.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("❌ Correo ya registrado");
        }
        if (userRepo.existsByCedula(dto.getCedula())) {
            throw new IllegalArgumentException("❌ Cédula ya registrada");
        }

        UserEntity user = new UserEntity();
        user.setNombre(dto.getNombre());
        user.setCedula(dto.getCedula());
        user.setCorreo(dto.getCorreo());
        user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        user.setFechaNacimiento(dto.getFechaNacimiento());
        user.setRol("PACIENTE");
        user = userRepo.save(user);

        PacienteEntity paciente = new PacienteEntity();
        paciente.setUser(user);
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setEstadoCivil(dto.getEstadoCivil());
        paciente.setOcupacion(dto.getOcupacion());
        paciente.setActividadFisica(dto.getActividadFisica());
        paciente.setPeso(dto.getPeso());
        paciente.setEstatura(dto.getEstatura());

        pacienteRepo.save(paciente);
    }

    public List<PacienteEntity> obtenerTodos() {
        return pacienteRepo.findAll();
    }

    public PacienteEntity obtenerPorId(Long id) {
        return pacienteRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con id: " + id));
    }

    public void actualizarPaciente(Long id, ActualizarPacienteDTO dto) {
        PacienteEntity paciente = obtenerPorId(id);
        UserEntity user = paciente.getUser();

        if (dto.getNombre() != null) user.setNombre(dto.getNombre());
        if (dto.getCorreo() != null) user.setCorreo(dto.getCorreo());

        if (dto.getOcupacion() != null) paciente.setOcupacion(dto.getOcupacion());
        if (dto.getActividadFisica() != null) paciente.setActividadFisica(dto.getActividadFisica());
        if (dto.getPeso() != null) paciente.setPeso(dto.getPeso());
        if (dto.getEstatura() != null) paciente.setEstatura(dto.getEstatura());

        userRepo.save(user);
        pacienteRepo.save(paciente);
    }

    public void eliminarPaciente(Long id) {
        if (!pacienteRepo.existsById(id)) {
            throw new IllegalArgumentException("Paciente no encontrado con id: " + id);
        }
        pacienteRepo.deleteById(id);
    }
}
