package com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto;

import lombok.Data;
import java.util.Map;

/**
 * DTO para enviar el diagnóstico generado al front.
 * Forma parte de la infraestructura.
 */
@Data
public class    DiagnosticoRespuestaDTO {
    private Map<String, Object> data; // O ajusta el modelo según la estructura generada por la IA
    private String rawJson;           // El JSON como string (opcional)
}