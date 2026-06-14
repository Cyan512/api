package com.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.dto.ApiResponse;
import com.api.dto.CursoRequest;
import com.api.model.Curso;
import com.api.service.CursoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CursoController {
    private final CursoService cursoService;

    @GetMapping("/v1/cursos")
    public ResponseEntity<ApiResponse<List<Curso>>> listar() {
        var cursos = cursoService.getAllCursos();
        return ResponseEntity.ok(ApiResponse.success(cursos, "Cursos obtenidos exitosamente"));
    }

    @GetMapping("/v1/cursos/{id}")
    public ResponseEntity<ApiResponse<Curso>> obtenerPorId(@PathVariable Long id) {
        var curso = cursoService.getCursoById(id);
        return ResponseEntity.ok(ApiResponse.success(curso, "Curso encontrado exitosamente"));
    }

    @PostMapping("/v1/cursos")
    public ResponseEntity<ApiResponse<Curso>> crear(@Valid @RequestBody CursoRequest request) {
        var curso = cursoService.createCurso(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(curso.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(curso, "Curso creado exitosamente"));
    }

    @PutMapping("/v1/cursos/{id}")
    public ResponseEntity<ApiResponse<Curso>> actualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
        var curso = cursoService.updateCurso(id, request);
        return ResponseEntity.ok(ApiResponse.success(curso, "Curso actualizado exitosamente"));
    }

    @DeleteMapping("/v1/cursos/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        cursoService.deleteCurso(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Curso eliminado exitosamente"));
    }
}
