package com.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Programa;
import com.api.service.ProgramaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/programas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProgramaController {

    private final ProgramaService programaService;

    @GetMapping
    public List<Programa> listar() {
        return programaService.getAllProgramas();
    }
}
