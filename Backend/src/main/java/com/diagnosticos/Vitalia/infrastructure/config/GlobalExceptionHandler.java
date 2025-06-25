package com.diagnosticos.Vitalia.infrastructure.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(404).body(
                java.util.Map.of("mensaje", e.getMessage())
        );
    }

    // Puedes agregar más handlers si necesitas capturar otros errores como NullPointerException, etc.
}
