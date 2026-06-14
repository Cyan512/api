package com.api.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    @Transactional(readOnly = true)
    public Curso getCursoById(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso no encontrado con id: " + id
                ));
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

    @Override
    @Transactional
    public Curso updateCurso(Long id, CursoRequest request) {
        var curso = getCursoById(id);
        curso.setNombre(request.nombre());
        curso.setCreditos(request.creditos());
        curso.setCategoria(request.categoria());
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void deleteCurso(Long id) {
        var curso = getCursoById(id);
        cursoRepository.delete(curso);
    }
}