package com.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TipoProgramaRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotBlank(message = "La imagen de tarjeta es obligatoria")
    String cardImageUrl,

    @NotBlank(message = "La imagen de fondo es obligatoria")
    String heroBgUrl
) {}
