package com.api.service;

import java.util.List;

import com.api.dto.CursoRequest;
import com.api.model.Curso;

public interface CursoService {
    List<Curso> getAllCursos();
    Curso getCursoById(Long id);
    Curso createCurso(CursoRequest request);
    Curso updateCurso(Long id, CursoRequest request);
    void deleteCurso(Long id);
}
