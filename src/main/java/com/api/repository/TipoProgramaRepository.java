package com.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.TipoPrograma;

public interface TipoProgramaRepository extends JpaRepository<TipoPrograma, Long> {
    boolean existsBySlug(String slug);
    Optional<TipoPrograma> findBySlug(String slug);
}
