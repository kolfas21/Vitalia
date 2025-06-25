package com.diagnosticos.Vitalia.infrastructure.strategy;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DiagnosticoStrategyBasico implements DiagnosticoStrategy {

    @Override
    public String diagnosticar(String nombrePaciente, List<String> sintomas) {
        // Ejemplo super simple para prueba: 
        return "Diagnóstico para " + nombrePaciente + ". Síntomas reportados: " + sintomas;
    }
}