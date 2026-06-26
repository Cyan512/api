package com.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleMallaRequest(
        @NotNull(message = "El programa es obligatorio") Long programaId,

        @NotNull(message = "El numero de semestre es obligatorio")
        @Positive(message = "El semestre debe ser mayor a 0")
        Integer numSemestre,

        @NotNull(message = "El orden es obligatorio") Integer orden,

        @NotNull(message = "Indicar si es espacio electivo es obligatorio") Boolean esEspacioElectivo,

        Long cursoId,

        @NotNull(message = "El costo en soles es obligatorio") BigDecimal costoSoles
) {

    @AssertTrue(message = "Un espacio no electivo requiere un curso asignado")
    private boolean isCursoValido() {
        if (Boolean.FALSE.equals(esEspacioElectivo)) {
            return cursoId != null;
        }
        return true;
    }

    @AssertFalse(message = "Un espacio electivo no debe tener curso fijo asignado")
    private boolean isElectivoSinCurso() {
        return Boolean.TRUE.equals(esEspacioElectivo) && cursoId != null;
    }
}
