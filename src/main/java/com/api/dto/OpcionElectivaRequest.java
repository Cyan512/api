package com.api.dto;

import jakarta.validation.constraints.NotNull;

public record OpcionElectivaRequest(
        @NotNull(message = "El programa es obligatorio") Long programaId,

        @NotNull(message = "El curso es obligatorio") Long cursoId
) {
}
