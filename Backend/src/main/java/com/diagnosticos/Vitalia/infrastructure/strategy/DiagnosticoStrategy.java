package com.diagnosticos.Vitalia.infrastructure.strategy;

import java.util.List;

public interface DiagnosticoStrategy {
    String diagnosticar(String nombrePaciente, List<String> sintomas);
}