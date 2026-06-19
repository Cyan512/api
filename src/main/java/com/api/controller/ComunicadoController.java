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
import com.api.dto.ComunicadoRequest;
import com.api.model.Comunicado;
import com.api.service.ComunicadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comunicados")
@RequiredArgsConstructor
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Comunicado>>> listar() {
        var comunicados = comunicadoService.getAllComunicados();
        return ResponseEntity.ok(ApiResponse.success(comunicados, "Comunicados obtenidos exitosamente"));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Comunicado>> obtenerPorSlug(@PathVariable String slug) {
        var comunicado = comunicadoService.getComunicadoBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(comunicado, "Comunicado encontrado exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Comunicado>> crear(@Valid @RequestBody ComunicadoRequest request) {
        var comunicado = comunicadoService.createComunicado(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(comunicado.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(comunicado, "Comunicado creado exitosamente"));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ApiResponse<Comunicado>> actualizar(@PathVariable String slug, @Valid @RequestBody ComunicadoRequest request) {
        var comunicado = comunicadoService.updateComunicado(slug, request);
        return ResponseEntity.ok(ApiResponse.success(comunicado, "Comunicado actualizado exitosamente"));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable String slug) {
        comunicadoService.deleteComunicado(slug);
        return ResponseEntity.ok(ApiResponse.success(null, "Comunicado eliminado exitosamente"));
    }
}
