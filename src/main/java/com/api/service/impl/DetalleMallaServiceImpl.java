package com.api.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.DetalleMallaRequest;
import com.api.model.Curso;
import com.api.model.DetalleMalla;
import com.api.model.Programa;
import com.api.repository.CursoRepository;
import com.api.repository.DetalleMallaRepository;
import com.api.repository.ProgramaRepository;
import com.api.service.DetalleMallaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleMallaServiceImpl implements DetalleMallaService {

    private final DetalleMallaRepository detalleMallaRepository;
    private final ProgramaRepository programaRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleMalla> getAll() {
        return detalleMallaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleMalla getById(Long id) {
        return detalleMallaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Detalle de malla no encontrado con id: " + id
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleMalla> getByProgramaId(Long programaId) {
        return detalleMallaRepository.findByProgramaId(programaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleMalla> getByProgramaIdAndSemestre(Long programaId, Integer numSemestre) {
        return detalleMallaRepository.findByProgramaIdAndNumSemestre(programaId, numSemestre);
    }

    @Override
    @Transactional
    public DetalleMalla create(DetalleMallaRequest request) {
        Programa programa = programaRepository.findById(request.programaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Programa no encontrado con id: " + request.programaId()
                ));

        Curso curso = null;
        if (request.cursoId() != null) {
            curso = cursoRepository.findById(request.cursoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Curso no encontrado con id: " + request.cursoId()
                    ));
        }

        var detalleMalla = DetalleMalla.builder()
                .numSemestre(request.numSemestre())
                .costoSoles(request.costoSoles())
                .curso(curso)
                .programa(programa)
                .build();

        return detalleMallaRepository.save(detalleMalla);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var detalleMalla = getById(id);
        detalleMallaRepository.delete(detalleMalla);
    }

}
