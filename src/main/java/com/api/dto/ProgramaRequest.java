package com.api.dto;

import com.api.model.Modalidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProgramaRequest(
    @NotNull(message = "El tipo de programa es obligatorio")
    Long idTipoPrograma,

    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    @NotNull(message = "La facultad es obligatoria")
    Long idFacultad,

    Boolean convocatoria,

    @NotNull(message = "La modalidad es obligatoria")
    Modalidad modalidad
) {}
