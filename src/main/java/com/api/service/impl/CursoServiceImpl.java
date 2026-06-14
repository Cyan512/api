package com.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.model.Curso;
import com.api.repository.CursoRepository;
import com.api.service.CursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

}