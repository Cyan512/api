package com.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProgramaCursoRequest(
    @NotNull(message = "El programa es obligatorio")
    Long idPrograma,

    @NotNull(message = "El curso es obligatorio")
    Long idCurso,

    @NotBlank(message = "El semestre es obligatorio")
    String semestres
) {}
