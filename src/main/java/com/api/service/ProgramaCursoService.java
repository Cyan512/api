package com.api.service;

import java.util.List;

import com.api.dto.ProgramaCursoRequest;
import com.api.model.ProgramaCurso;

public interface ProgramaCursoService {
    List<ProgramaCurso> getAllProgramaCursos();
    ProgramaCurso createProgramaCurso(ProgramaCursoRequest request);
}
