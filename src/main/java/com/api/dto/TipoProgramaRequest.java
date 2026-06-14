package com.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TipoProgramaRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    String imagenCard,

    String imagenBg
) {}
