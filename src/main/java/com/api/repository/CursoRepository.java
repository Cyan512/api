package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
