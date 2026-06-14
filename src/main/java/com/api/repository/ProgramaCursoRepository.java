package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.ProgramaCurso;

public interface ProgramaCursoRepository extends JpaRepository<ProgramaCurso, Long> {
    List<ProgramaCurso> findByIdProgramaId(Long programaId);
    List<ProgramaCurso> findByIdCursoId(Long cursoId);
}
