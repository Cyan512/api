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

import com.api.dto.FacultadRequest;
import com.api.model.Facultad;
import com.api.service.FacultadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/facultades")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FacultadController {

    private final FacultadService facultadService;

    @GetMapping
    public List<Facultad> listar() {
        return facultadService.getAllFacultades();
    }

    @PostMapping
    public ResponseEntity<Facultad> crear(@Valid @RequestBody FacultadRequest request) {
        var facultad = facultadService.createFacultad(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(facultad.getId())
                .toUri();
        return ResponseEntity.created(location).body(facultad);
    }
}
