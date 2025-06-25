package com.diagnosticos.Vitalia.domain.model;

import java.util.List;

public class Diagnostico {
    private String fecha;
    private String condicion;
    private List<String> sintomas;
    private Recomendacion recomendacion;

    // getters/setters/constructores internos

    public static class Recomendacion {
        private List<Medicamento> medicamentos;
        private String incapacidad;
        // getters/setters/constructores
    }

    public static class Medicamento {
        private String nombre;
        private String dosis;
        private String frecuencia;
        // getters/setters/constructores
    }
}