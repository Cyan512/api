package com.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Curso;
import com.api.service.CursoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CursoController {
    private final CursoService cursoService;

    @GetMapping
    public List<Curso> listar() {
        return cursoService.getAllCursos();
    }
}
