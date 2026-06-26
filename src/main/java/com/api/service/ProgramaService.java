package com.api.service;

import java.util.List;

import com.api.dto.ProgramaRequest;
import com.api.enums.Modalidad;
import com.api.model.Programa;

public interface ProgramaService {
    List<Programa> getProgramas(String tipoSlug, String q, Modalidad modalidad, Long idFacultad, Boolean convocatoria);
    Programa getProgramaBySlug(String slug);
    Programa createPrograma(ProgramaRequest request);
    Programa updatePrograma(String slug, ProgramaRequest request);
    void deletePrograma(String slug);
}
