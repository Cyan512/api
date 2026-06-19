package com.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ComunicadoRequest(
    @NotBlank(message = "El titulo es obligatorio") String titulo,
    @NotBlank(message = "El resumen es obligatorio") String resumen,
    @NotBlank(message = "El contenido es obligatorio") String contenido,
    String imagen
) {}
