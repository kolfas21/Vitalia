package com.diagnosticos.Vitalia.application.service;

import com.diagnosticos.Vitalia.domain.repository.ConsultaMedicaRepository;
import com.diagnosticos.Vitalia.domain.repository.DiagnosticoRepository;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.DiagnosticoSolicitudDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.controller.dto.DiagnosticoRespuestaDTO;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.ConsultaMedicaEntity;
import com.diagnosticos.Vitalia.infrastructure.adapter.persistence.entity.DiagnosticoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenerarDiagnosticoService {

    private final ConsultaMedicaRepository consultaRepo;
    private final DiagnosticoRepository diagnosticoRepo;

    public DiagnosticoRespuestaDTO generarDiagnostico(DiagnosticoSolicitudDTO dto) {
        DiagnosticoRespuestaDTO respuesta = new DiagnosticoRespuestaDTO();
        String rawText = "";
        String jsonExtraido = "";
        Map<String, Object> resultado = new HashMap<>();

        try {
            String prompt = construirPrompt(dto);
            rawText = llamarIA(prompt);

            jsonExtraido = extraerJson(rawText);

            if (!jsonExtraido.isEmpty()) {
                resultado = new ObjectMapper().readValue(jsonExtraido, Map.class);

                // ✅ Extraer la condición
                Map<String, Object> diagnosticosMap = (Map<String, Object>) resultado.get("diagnosticos");
                String condicion = (String) diagnosticosMap.get("condicion");

                // 🔍 Buscar la consulta médica asociada
                ConsultaMedicaEntity consulta = consultaRepo.findById(dto.getIdConsulta()).orElse(null);
                if (consulta == null) {
                    resultado.put("error", "Consulta médica no encontrada con ID: " + dto.getIdConsulta());
                } else {
                    // 💾 Guardar diagnóstico
                    DiagnosticoEntity diagnostico = new DiagnosticoEntity();
                    diagnostico.setConsulta(consulta);
                    diagnostico.setResultado(condicion);
                    diagnostico.setProbabilidad(1.0f); // o alguna lógica para determinar esto

                    diagnosticoRepo.save(diagnostico);
                }

            } else {
                resultado.put("error", "La IA no devolvió JSON válido.");
                resultado.put("texto", rawText);
            }
        } catch (RestClientException e) {
            resultado.put("error", "No se pudo conectar con la IA: " + e.getMessage());
        } catch (Exception e) {
            resultado.put("error", "No se pudo parsear la respuesta de la IA: " + e.getMessage());
            resultado.put("texto", rawText);
        }

        respuesta.setData(resultado);
        respuesta.setRawJson(jsonExtraido);
        return respuesta;
    }
    private String construirPrompt(DiagnosticoSolicitudDTO dto) {
        String fecha = LocalDate.now().toString();

        // Si los síntomas son una List<String>:
        String sintomasJson = (dto.getSintomas() != null)
                ? dto.getSintomas()
                        .stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", ", "[", "]"))
                : "[]";

        // Prompt siguiendo el formato Angular exacto, con tips de NO texto extra
        return """
Eres un médico general. Según los síntomas del paciente, debes generar un diagnóstico en FORMATO JSON VÁLIDO.
Sigue exactamente esta estructura (NO agregues texto fuera del JSON):

{
  "paciente": {
    "id": "00001",
    "nombre": "María López",
    "edad": 29,
    "genero": "Femenino"
  },
  "diagnosticos": {
    "fecha": "2025-05-05",
    "condicion": "Faringitis viral",
    "sintomas": ["Dolor de garganta", "Fiebre", "Congestión nasal"],
    "recomendacion": {
      "medicamentos": [
        {
          "nombre": "Paracetamol",
          "dosis": "500 mg",
          "frecuencia": "Cada 8 horas"
        },
        {
          "nombre": "Loratadina",
          "dosis": "10 mg",
          "frecuencia": "Cada 12 horas"
        }
      ],
      "incapacidad": "Reposo por 3 días"
    }
  }
}

Ahora genera un JSON igual para el siguiente paciente (no escribas NADA fuera del JSON):

Paciente:
ID: %s
Nombre: %s
Edad: %d
Género: %s
Síntomas: %s
Fecha de consulta: %s

Responde sólo con el JSON, sin texto extra antes ni después.
""".formatted(
                dto.getId(), dto.getNombre(), dto.getEdad(), dto.getGenero(), sintomasJson, fecha
        );
    }

    private String llamarIA(String prompt) {
        String url = "http://localhost:11434/api/generate";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "medllama2");
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);
        requestBody.put("options", options);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Object obj = response.getBody().get("response");
            return obj != null ? obj.toString() : "";
        }
        return "";
    }

    private String extraerJson(String rawText) {
        int inicio = rawText.indexOf('{');
        int fin = rawText.lastIndexOf('}') + 1;
        if (inicio >= 0 && fin > inicio) {
            return rawText.substring(inicio, fin);
        }
        return "";
    }
}