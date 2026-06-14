package com.api.dto;

import jakarta.validation.constraints.NotBlank;

public record FacultadRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre
) {}
