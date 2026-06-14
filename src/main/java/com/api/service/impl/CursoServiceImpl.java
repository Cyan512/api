package com.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.dto.CursoRequest;
import com.api.model.Curso;
import com.api.repository.CursoRepository;
import com.api.service.CursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    @Override
    @Transactional
    public Curso createCurso(CursoRequest request) {
        var curso = Curso.builder()
                .nombre(request.nombre())
                .creditos(request.creditos())
                .categoria(request.categoria())
                .build();
        return cursoRepository.save(curso);
    }

}