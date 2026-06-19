package com.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record ProgramaCursoRequest(
        Integer semestre,

        Boolean electivo,

        @NotNull(message = "El costo cuota es obligatorio") BigDecimal costoCuota,

        @NotNull(message = "El programa es obligatorio") Long idPrograma,

        @NotNull(message = "El curso es obligatorio") Long idCurso

) {
}
