package com.api.service;

import java.util.List;

import com.api.dto.ProgramaCursoRequest;
import com.api.model.ProgramaCurso;

public interface ProgramaCursoService {
    List<ProgramaCurso> getAllProgramaCursos();
    ProgramaCurso getProgramaCursoById(Long id);
    ProgramaCurso createProgramaCurso(ProgramaCursoRequest request);
    void deleteProgramaCurso(Long id);
    List<ProgramaCurso> getProgramaCursosByProgramaId(Long programaId);
    List<ProgramaCurso> getProgramaCursosByCursoId(Long cursoId);
}
