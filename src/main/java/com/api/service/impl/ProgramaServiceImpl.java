package com.api.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ProgramaRequest;
import com.api.model.Facultad;
import com.api.model.Programa;
import com.api.model.TipoPrograma;
import com.api.repository.FacultadRepository;
import com.api.repository.ProgramaRepository;
import com.api.repository.TipoProgramaRepository;
import com.api.service.ProgramaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository programaRepository;
    private final TipoProgramaRepository tipoProgramaRepository;
    private final FacultadRepository facultadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Programa> getAllProgramas() {
        return programaRepository.findAll();
    }

    @Override
    @Transactional
    public Programa createPrograma(ProgramaRequest request) {
        var tipoPrograma = tipoProgramaRepository.findById(request.idTipoPrograma())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de programa no encontrado con id: " + request.idTipoPrograma()
                ));

        var facultad = facultadRepository.findById(request.idFacultad())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Facultad no encontrada con id: " + request.idFacultad()
                ));

        var programa = Programa.builder()
                .idTipoPrograma(tipoPrograma)
                .nombre(request.nombre())
                .idFacultad(facultad)
                .slug(request.slug())
                .convocatoria(request.convocatoria())
                .modalidad(request.modalidad())
                .build();
        return programaRepository.save(programa);
    }
}
