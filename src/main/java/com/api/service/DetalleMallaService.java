package com.api.service;

import java.util.List;

import com.api.dto.DetalleMallaRequest;
import com.api.model.DetalleMalla;

public interface DetalleMallaService {
    List<DetalleMalla> getAll();
    DetalleMalla getById(Long id);
    List<DetalleMalla> getByProgramaId(Long programaId);
    List<DetalleMalla> getByProgramaIdAndSemestre(Long programaId, Integer numSemestre);
    DetalleMalla create(DetalleMallaRequest request);
    void delete(Long id);
}
