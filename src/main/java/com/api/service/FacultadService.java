package com.api.service;

import java.util.List;

import com.api.dto.FacultadRequest;
import com.api.model.Facultad;

public interface FacultadService {
    List<Facultad> getAllFacultades();
    Facultad getFacultadById(Long id);
    Facultad createFacultad(FacultadRequest request);
    Facultad updateFacultad(Long id, FacultadRequest request);
    void deleteFacultad(Long id);
}
