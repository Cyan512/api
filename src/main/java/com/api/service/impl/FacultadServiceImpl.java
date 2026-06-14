package com.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Facultad createFacultad(FacultadRequest request) {
        var facultad = Facultad.builder()
                .nombre(request.nombre())
                .build();
        return facultadRepository.save(facultad);
    }
}
