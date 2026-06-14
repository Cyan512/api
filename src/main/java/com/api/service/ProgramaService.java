package com.api.service;

import java.util.List;

import com.api.dto.ProgramaRequest;
import com.api.model.Programa;

public interface ProgramaService {
    List<Programa> getAllProgramas();
    List<Programa> getProgramasByTipoSlug(String tipoSlug);
    Programa getProgramaBySlug(String slug);
    Programa createPrograma(ProgramaRequest request);
}
