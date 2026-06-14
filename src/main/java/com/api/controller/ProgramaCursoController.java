package com.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.dto.ApiResponse;
import com.api.dto.ProgramaCursoRequest;
import com.api.model.ProgramaCurso;
import com.api.service.ProgramaCursoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/programas-cursos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProgramaCursoController {

    private final ProgramaCursoService programaCursoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgramaCurso>>> listar() {
        var programasCurso = programaCursoService.getAllProgramaCursos();
        return ResponseEntity.ok(ApiResponse.success(programasCurso, "Programas-curso obtenidos exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProgramaCurso>> crear(@Valid @RequestBody ProgramaCursoRequest request) {
        var programaCurso = programaCursoService.createProgramaCurso(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(programaCurso.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(programaCurso, "Programa-curso creado exitosamente"));
    }
}
