package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.DetalleMalla;

public interface DetalleMallaRepository extends JpaRepository<DetalleMalla, Long> {
    List<DetalleMalla> findByProgramaId(Long programaId);
    List<DetalleMalla> findByProgramaIdAndNumSemestre(Long programaId, Integer numSemestre);
}
