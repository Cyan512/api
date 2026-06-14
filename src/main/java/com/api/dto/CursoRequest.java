package com.api.dto;

import com.api.model.Categoria;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    Integer creditos,

    @NotNull(message = "La categoría es obligatoria")
    Categoria categoria
) {}
