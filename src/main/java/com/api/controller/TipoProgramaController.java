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
import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;
import com.api.service.TipoProgramaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipos-programa")
@RequiredArgsConstructor
public class TipoProgramaController {

    private final TipoProgramaService tipoProgramaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoPrograma>>> listar() {
        var tipos = tipoProgramaService.getAllTipoProgramas();
        return ResponseEntity.ok(ApiResponse.success(tipos, "Tipos de programa obtenidos exitosamente"));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<TipoPrograma>> obtenerPorSlug(@PathVariable String slug) {
        var tipoPrograma = tipoProgramaService.getTipoProgramaBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(tipoPrograma, "Tipo de programa encontrado exitosamente"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TipoPrograma>> crear(@Valid @RequestBody TipoProgramaRequest request) {
        var tipoPrograma = tipoProgramaService.createTipoPrograma(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tipoPrograma.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(tipoPrograma, "Tipo de programa creado exitosamente"));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ApiResponse<TipoPrograma>> actualizar(@PathVariable String slug, @Valid @RequestBody TipoProgramaRequest request) {
        var tipoPrograma = tipoProgramaService.updateTipoPrograma(slug, request);
        return ResponseEntity.ok(ApiResponse.success(tipoPrograma, "Tipo de programa actualizado exitosamente"));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable String slug) {
        tipoProgramaService.deleteTipoPrograma(slug);
        return ResponseEntity.ok(ApiResponse.success(null, "Tipo de programa eliminado exitosamente"));
    }
}
