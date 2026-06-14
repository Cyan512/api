package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.TipoPrograma;

public interface TipoProgramaRepository extends JpaRepository<TipoPrograma, Long> {
    boolean existsBySlug(String slug);
}
