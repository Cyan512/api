package com.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequiredArgsConstructor
public class ProgramaCursoController {

    private final ProgramaCursoService programaCursoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgramaCurso>>> listar(
            @RequestParam(required = false) Long programaId,
            @RequestParam(required = false) Long cursoId) {
        List<ProgramaCurso> programasCurso;
        if (programaId != null) {
            programasCurso = programaCursoService.getProgramaCursosByProgramaId(programaId);
        } else if (cursoId != null) {
            programasCurso = programaCursoService.getProgramaCursosByCursoId(cursoId);
        } else {
            programasCurso = programaCursoService.getAllProgramaCursos();
        }
        return ResponseEntity.ok(ApiResponse.success(programasCurso, "Programas-curso obtenidos exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgramaCurso>> obtenerPorId(@PathVariable Long id) {
        var programaCurso = programaCursoService.getProgramaCursoById(id);
        return ResponseEntity.ok(ApiResponse.success(programaCurso, "Asociación programa-curso encontrada exitosamente"));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        programaCursoService.deleteProgramaCurso(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Asociación programa-curso eliminada exitosamente"));
    }
}
