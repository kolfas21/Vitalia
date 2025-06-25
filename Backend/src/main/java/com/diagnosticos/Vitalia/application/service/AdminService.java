package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.AdminDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarAdminDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public void registrarAdmin(AdminDTO dto) {
        if (userRepo.existsByCorreo(dto.getCorreo()))
            throw new IllegalArgumentException("❌ Correo ya registrado");
        if (userRepo.existsByCedula(dto.getCedula()))
            throw new IllegalArgumentException("❌ Cédula ya registrada");

        UserEntity admin = new UserEntity();
        admin.setNombre(dto.getNombre());
        admin.setCorreo(dto.getCorreo());
        admin.setCedula(dto.getCedula());
        admin.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        admin.setRol("ADMIN");
        userRepo.save(admin);
    }

    public List<UserEntity> obtenerTodos() {
        return userRepo.findByRol("ADMIN");
    }



    public UserEntity obtenerPorId(Long id) {
        return userRepo.findById(id)
                .filter(u -> "ADMIN".equals(u.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado con id: " + id));
    }

    public void actualizarAdmin(Long id, ActualizarAdminDTO dto) {
        UserEntity admin = userRepo.findById(id)
                .filter(u -> "ADMIN".equals(u.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado con id: " + id));

        admin.setNombre(dto.getNombre());
        admin.setCorreo(dto.getCorreo());
        admin.setCedula(dto.getCedula());
        userRepo.save(admin);
    }

    public void eliminarAdmin(Long id) {
        UserEntity admin = userRepo.findById(id)
                .filter(u -> "ADMIN".equals(u.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado con id: " + id));
        userRepo.delete(admin);
    }
}
