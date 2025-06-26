package com.diagnosticos.Vitalia.domain.repository;

import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByCorreo(String correo);
    boolean existsByCedula(String cedula);
    UserEntity findByCorreo(String correo);
    List<UserEntity> findByRol(String rol);
    Optional<UserEntity> findByCedula(String cedula);

}

