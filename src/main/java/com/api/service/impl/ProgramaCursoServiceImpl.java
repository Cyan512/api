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
    @Transactional(readOnly = true)
    public ProgramaCurso getProgramaCursoById(Long id) {
        return programaCursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asociación programa-curso no encontrada con id: " + id
                ));
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

    @Override
    @Transactional
    public void deleteProgramaCurso(Long id) {
        var programaCurso = getProgramaCursoById(id);
        programaCursoRepository.delete(programaCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaCurso> getProgramaCursosByProgramaId(Long programaId) {
        if (!programaRepository.existsById(programaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Programa no encontrado con id: " + programaId
            );
        }
        return programaCursoRepository.findByIdProgramaId(programaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaCurso> getProgramaCursosByCursoId(Long cursoId) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Curso no encontrado con id: " + cursoId
            );
        }
        return programaCursoRepository.findByIdCursoId(cursoId);
    }
}
