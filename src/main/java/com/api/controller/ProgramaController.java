package com.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.dto.ApiResponse;
import com.api.dto.ProgramaRequest;
import com.api.model.Programa;
import com.api.service.ProgramaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/programas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProgramaController {

    private final ProgramaService programaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Programa>>> listar(
            @RequestParam(required = false) String tipoSlug) {
        var programas = tipoSlug != null
                ? programaService.getProgramasByTipoSlug(tipoSlug)
                : programaService.getAllProgramas();
        return ResponseEntity.ok(ApiResponse.success(programas, "Programas obtenidos exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Programa>> crear(@Valid @RequestBody ProgramaRequest request) {
        var programa = programaService.createPrograma(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(programa.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(programa, "Programa creado exitosamente"));
    }
}
