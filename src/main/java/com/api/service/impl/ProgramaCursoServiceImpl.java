package com.api.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ProgramaCursoRequest;
import com.api.model.Curso;
import com.api.model.Programa;
import com.api.model.ProgramaCurso;
import com.api.repository.CursoRepository;
import com.api.repository.ProgramaCursoRepository;
import com.api.repository.ProgramaRepository;
import com.api.service.ProgramaCursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgramaCursoServiceImpl implements ProgramaCursoService {

    private final ProgramaCursoRepository programaCursoRepository;
    private final ProgramaRepository programaRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaCurso> getAllProgramaCursos() {
        return programaCursoRepository.findAll();
    }

    @Override
    @Transactional
    public ProgramaCurso createProgramaCurso(ProgramaCursoRequest request) {
        var programa = programaRepository.findById(request.idPrograma())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Programa no encontrado con id: " + request.idPrograma()
                ));

        var curso = cursoRepository.findById(request.idCurso())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Curso no encontrado con id: " + request.idCurso()
                ));

        var programaCurso = ProgramaCurso.builder()
                .idPrograma(programa)
                .idCurso(curso)
                .semestres(request.semestres())
                .build();
        return programaCursoRepository.save(programaCurso);
    }
}
