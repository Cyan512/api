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
import com.api.dto.DetalleMallaRequest;
import com.api.model.DetalleMalla;
import com.api.service.DetalleMallaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/detalles-malla")
@RequiredArgsConstructor
public class DetalleMallaController {

    private final DetalleMallaService detalleMallaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DetalleMalla>>> listar(
            @RequestParam(required = false) Long programaId,
            @RequestParam(required = false) Integer numSemestre) {
        List<DetalleMalla> result;
        if (programaId != null && numSemestre != null) {
            result = detalleMallaService.getByProgramaIdAndSemestre(programaId, numSemestre);
        } else if (programaId != null) {
            result = detalleMallaService.getByProgramaId(programaId);
        } else {
            result = detalleMallaService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(result, "Detalles de malla obtenidos exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DetalleMalla>> obtenerPorId(@PathVariable Long id) {
        var detalleMalla = detalleMallaService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(detalleMalla, "Detalle de malla encontrado exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DetalleMalla>> crear(@Valid @RequestBody DetalleMallaRequest request) {
        var detalleMalla = detalleMallaService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(detalleMalla.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(detalleMalla, "Detalle de malla creado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        detalleMallaService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Detalle de malla eliminado exitosamente"));
    }
}
