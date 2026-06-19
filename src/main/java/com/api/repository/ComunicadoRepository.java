package com.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Comunicado;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    boolean existsBySlug(String slug);
    Optional<Comunicado> findBySlug(String slug);
}
