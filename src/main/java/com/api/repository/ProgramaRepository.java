package com.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.api.model.Programa;

public interface ProgramaRepository extends JpaRepository<Programa, Long>, JpaSpecificationExecutor<Programa> {
    boolean existsBySlug(String slug);
    Optional<Programa> findBySlug(String slug);
}
