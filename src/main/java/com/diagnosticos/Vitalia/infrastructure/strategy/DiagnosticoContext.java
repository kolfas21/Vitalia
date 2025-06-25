package com.diagnosticos.Vitalia.infrastructure.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiagnosticoContext {

    
    private final DiagnosticoStrategy strategy;

    public String procesar(String nombrePaciente, List<String> sintomas) {
        return strategy.diagnosticar(nombrePaciente, sintomas);
    }
}