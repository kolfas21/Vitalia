package com.diagnosticos.Vitalia.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Component
public class DiagnosticoIaClient {
    private final RestTemplate restTemplate = new RestTemplate();

    // Puedes hacer este método más parametrizable si gustas
    public String obtenerDiagnostico(String prompt) {
        String url = "http://localhost:11434/api/generate";
        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek-r1");
        body.put("prompt", prompt);
        body.put("stream", false);
        body.put("options", Map.of("temperature", 0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("response");
        }
        throw new RuntimeException("Error llamando a la IA: " + response.getStatusCode());
    }
}