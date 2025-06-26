package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.UserRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.PacienteEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import com.diagnosticos.Vitalia.domain.repository.PacienteRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PacienteRepository pacienteRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // validacion de googleAuth2
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // se obtienen los datos del usuario desde google

        Map<String, Object> attributes = oAuth2User.getAttributes(); // Datos del perfil (ej: {name="diego", email="diego@gmail.com"})
        String email = (String) attributes.get("email");
        String nombre = (String) attributes.get("name");

        UserEntity user = userRepository.findByCorreo(email);

        // si el usuario no existe se crea
        if (user == null) {
            user = new UserEntity();
            user.setCorreo(email);
            user.setNombre(nombre);
            user.setFechaNacimiento(LocalDate.now());
            user.setRol("PACIENTE");
            user.setContrasena("");
            user = userRepository.save(user);

            PacienteEntity paciente = new PacienteEntity();
            paciente.setUser(user);
            paciente.setFechaNacimiento(LocalDate.now());
            pacienteRepository.save(paciente);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "email"
        );
    }
}