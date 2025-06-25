package com.diagnosticos.Vitalia.domain.model;

public class Paciente {
    private String nombre;
    private int edad;

    public Paciente() {}

    public Paciente(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}