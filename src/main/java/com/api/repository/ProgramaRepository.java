package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Programa;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    boolean existsBySlug(String slug);
    Optional<Programa> findBySlug(String slug);
    List<Programa> findByIdTipoProgramaSlug(String slug);
}
