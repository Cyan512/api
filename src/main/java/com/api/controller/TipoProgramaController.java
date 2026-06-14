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

import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;
import com.api.service.TipoProgramaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tipos-programa")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TipoProgramaController {

    private final TipoProgramaService tipoProgramaService;

    @GetMapping
    public List<TipoPrograma> listar() {
        return tipoProgramaService.getAllTipoProgramas();
    }

    @PostMapping
    public ResponseEntity<TipoPrograma> crear(@Valid @RequestBody TipoProgramaRequest request) {
        var tipoPrograma = tipoProgramaService.createTipoPrograma(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tipoPrograma.getId())
                .toUri();
        return ResponseEntity.created(location).body(tipoPrograma);
    }
}
