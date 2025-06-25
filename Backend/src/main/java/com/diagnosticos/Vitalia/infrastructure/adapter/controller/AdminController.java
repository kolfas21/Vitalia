package com.diagnosticos.Vitalia.infrastructure.adapter.controller;

import com.diagnosticos.Vitalia.application.service.AdminService;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.AdminDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.ActualizarAdminDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.MedicoEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody AdminDTO dto) {
        adminService.registrarAdmin(dto);
        return ResponseEntity.ok("✅ Administrador registrado correctamente");
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> obtenerTodos() {
        return ResponseEntity.ok(adminService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody ActualizarAdminDTO dto) {
        adminService.actualizarAdmin(id, dto);
        return ResponseEntity.ok("✅ Administrador actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        adminService.eliminarAdmin(id);
        return ResponseEntity.ok("✅ Administrador eliminado correctamente");
    }



}
