package com.api.dto;

import com.api.model.Modalidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProgramaRequest(
                @NotBlank(message = "El nombre es obligatorio") String nombre,

                Boolean convocatoria,

                String imagen,

                @NotBlank(message = "El objetivo general es obligatorio") String objetivoGeneral,

                @NotBlank(message = "El objetivos especificos es obligatorio") String objetivosEspecificos,

                @NotBlank(message = "El objetivo perfil posgraduado es obligatorio") String perfilPosgraduado,

                @NotNull(message = "La facultad es obligatoria") Long idFacultad,

                @NotNull(message = "El tipo de programa es obligatorio") Long idTipoPrograma,

                @NotNull(message = "La modalidad es obligatoria") Modalidad modalidad,

                @NotBlank(message = "Las lineas de investigacion es obligatorio") String lineasInvestigacion

) {
}
