package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.MedicoRepository;
import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.MedicoDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarMedicoDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public void registrarMedico(MedicoDTO dto) {
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
        user.setRol("MEDICO");
        user = userRepo.save(user);

        MedicoEntity medico = new MedicoEntity();
        medico.setEspecialidad(dto.getEspecialidad());
        medico.setRegistroProfesional(dto.getRegistroProfesional());
        medico.setUser(user);

        medicoRepo.save(medico);
    }

    public List<MedicoEntity> obtenerTodos() {
        return medicoRepo.findAll();
    }

    public MedicoEntity obtenerPorCedula(String cedula) {
    return medicoRepo.findByUserCedula(cedula)
        .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con cédula: " + cedula));
    }
    public MedicoEntity obtenerPorId(Long id) {
        return medicoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con id: " + id));
    }


    public void actualizarMedico(Long id, ActualizarMedicoDTO dto) {
        UserEntity user = userRepo.findById(id)
                .filter(u -> "MEDICO".equals(u.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con id: " + id));

        // Actualizar campos del usuario
        user.setNombre(dto.getNombre());
        user.setCorreo(dto.getCorreo());
        user.setCedula(dto.getCedula());
        userRepo.save(user);

        // Buscar y actualizar MedicoEntity
        MedicoEntity medico = medicoRepo.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la información médica"));

        medico.setEspecialidad(dto.getEspecialidad());
        medico.setRegistroProfesional(dto.getRegistroProfesional());
        medicoRepo.save(medico);
    }


    public void eliminarMedico(Long id) {
        UserEntity user = userRepo.findById(id)
                .filter(u -> "MEDICO".equals(u.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con id: " + id));

        // Eliminar primero MedicoEntity
        medicoRepo.findByUserId(id).ifPresent(medicoRepo::delete);

        // Luego eliminar el usuario
        userRepo.deleteById(id);
    }




}
