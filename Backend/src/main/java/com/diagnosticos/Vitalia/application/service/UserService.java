package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.RegistroPacienteDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarInfoMedicaDTO;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registrarPaciente(@NotNull RegistroPacienteDTO dto) {
        if (userRepository.existsByCorreo(dto.getCorreo())) {
            throw new IllegalArgumentException("❌ Correo ya registrado");
        }
        if (userRepository.existsByCedula(dto.getCedula())) {
            throw new IllegalArgumentException("❌ Cédula ya registrada");
        }

        UserEntity usuario = new UserEntity();
        usuario.setNombre(dto.getNombre());
        usuario.setCedula(dto.getCedula());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setRol("PACIENTE");

        return userRepository.save(usuario);
    }

    @Transactional
    public void actualizarInfoMedica(Long id, ActualizarInfoMedicaDTO dto) {
        PacienteEntity paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con id: " + id));

        paciente.setPeso(dto.getPeso());
        paciente.setEstatura(dto.getEstatura());
        paciente.setActividadFisica(dto.getActividadFisica());
        paciente.setAlergias(dto.getAlergias());
        paciente.setSintomas(dto.getSintomas());

        pacienteRepository.save(paciente);
    }

    public List<UserEntity> obtenerTodosLosUsuarios() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> obtenerUsuarioPorId(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> obtenerUsuarioPorCedula(String cedula) {
        return userRepository.findByCedula(cedula);
    }

    public UserEntity actualizarUsuario(Long id, UserEntity datosActualizados) {
        UserEntity usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));

        if (!"PACIENTE".equalsIgnoreCase(usuario.getRol())) {
            throw new IllegalArgumentException("❌ Solo se pueden actualizar usuarios con rol PACIENTE");
        }

        // Validar que no se pueda cambiar a otro rol
        if (!"PACIENTE".equalsIgnoreCase(datosActualizados.getRol())) {
            throw new IllegalArgumentException("❌ No está permitido cambiar el rol del usuario PACIENTE");
        }

        // Verificar si otro usuario ya tiene ese correo
        UserEntity usuarioPorCorreo = userRepository.findByCorreo(datosActualizados.getCorreo());
        if (usuarioPorCorreo != null && !usuarioPorCorreo.getId().equals(id)) {
            throw new IllegalArgumentException("❌ Correo ya registrado por otro usuario");
        }

        // Verificar si otro usuario ya tiene esa cédula
        userRepository.findByCedula(datosActualizados.getCedula()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new IllegalArgumentException("❌ Cédula ya registrada por otro usuario");
            }
        });

        usuario.setNombre(datosActualizados.getNombre());
        usuario.setCedula(datosActualizados.getCedula());
        usuario.setCorreo(datosActualizados.getCorreo());
        usuario.setFechaNacimiento(datosActualizados.getFechaNacimiento());
        // No se actualiza el rol: usuario.setRol(...)

        if (datosActualizados.getContrasena() != null && !datosActualizados.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(datosActualizados.getContrasena()));
        }

        return userRepository.save(usuario);
    }



    public UserEntity eliminarUsuario(Long id) {
        UserEntity usuario = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));

        if (!"PACIENTE".equalsIgnoreCase(usuario.getRol())) {
            throw new IllegalArgumentException("❌ Solo se pueden eliminar usuarios con rol PACIENTE");
        }

        userRepository.deleteById(id);
        return usuario;
    }
}