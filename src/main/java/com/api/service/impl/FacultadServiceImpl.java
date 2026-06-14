package com.api.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.FacultadRequest;
import com.api.model.Facultad;
import com.api.repository.FacultadRepository;
import com.api.service.FacultadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacultadServiceImpl implements FacultadService {

    private final FacultadRepository facultadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Facultad> getAllFacultades() {
        return facultadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Facultad getFacultadById(Long id) {
        return facultadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Facultad no encontrada con id: " + id
                ));
    }

    @Override
    @Transactional
    public Facultad createFacultad(FacultadRequest request) {
        var facultad = Facultad.builder()
                .nombre(request.nombre())
                .build();
        return facultadRepository.save(facultad);
    }

    @Override
    @Transactional
    public Facultad updateFacultad(Long id, FacultadRequest request) {
        var facultad = getFacultadById(id);
        facultad.setNombre(request.nombre());
        return facultadRepository.save(facultad);
    }

    @Override
    @Transactional
    public void deleteFacultad(Long id) {
        var facultad = getFacultadById(id);
        facultadRepository.delete(facultad);
    }
}
