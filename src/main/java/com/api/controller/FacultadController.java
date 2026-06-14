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
import com.api.dto.FacultadRequest;
import com.api.model.Facultad;
import com.api.service.FacultadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/facultades")
@RequiredArgsConstructor
public class FacultadController {

    private final FacultadService facultadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Facultad>>> listar() {
        var facultades = facultadService.getAllFacultades();
        return ResponseEntity.ok(ApiResponse.success(facultades, "Facultades obtenidas exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Facultad>> obtenerPorId(@PathVariable Long id) {
        var facultad = facultadService.getFacultadById(id);
        return ResponseEntity.ok(ApiResponse.success(facultad, "Facultad encontrada exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Facultad>> crear(@Valid @RequestBody FacultadRequest request) {
        var facultad = facultadService.createFacultad(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(facultad.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(facultad, "Facultad creada exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Facultad>> actualizar(@PathVariable Long id, @Valid @RequestBody FacultadRequest request) {
        var facultad = facultadService.updateFacultad(id, request);
        return ResponseEntity.ok(ApiResponse.success(facultad, "Facultad actualizada exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        facultadService.deleteFacultad(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Facultad eliminada exitosamente"));
    }
}
