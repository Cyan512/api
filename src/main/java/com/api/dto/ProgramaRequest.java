package com.api.dto;

import java.math.BigDecimal;

import com.api.enums.Modalidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProgramaRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,

        Boolean enConvocatoria,

        @NotBlank(message = "La imagen es obligatoria") String imageUrl,

        @NotBlank(message = "El objetivo general es obligatorio") String objetivoGeneral,

        @NotEmpty(message = "Los objetivos especificos son obligatorios") String[] objetivosEspecificos,

        @NotBlank(message = "El perfil posgraduado es obligatorio") String perfilPosgraduado,

        @NotNull(message = "La facultad es obligatoria") Long facultadId,

        @NotNull(message = "El tipo de programa es obligatorio") Long tipoProgramaId,

        @NotNull(message = "La modalidad es obligatoria") Modalidad modalidad,

        @NotEmpty(message = "Las lineas de investigacion son obligatorias") String[] lineasInvestigacion,

        @NotNull(message = "El costo de matricula es obligatorio") BigDecimal costoMatricula

) {
}
