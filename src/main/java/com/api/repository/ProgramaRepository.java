package com.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.model.Modalidad;
import com.api.model.Programa;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    boolean existsBySlug(String slug);
    Optional<Programa> findBySlug(String slug);

    @Query("""
        SELECT p FROM Programa p
        WHERE (:tipoSlug IS NULL OR p.idTipoPrograma.slug = :tipoSlug)
        AND (:q IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
        AND (:modalidad IS NULL OR p.modalidad = :modalidad)
        AND (:idFacultad IS NULL OR p.idFacultad.id = :idFacultad)
        AND (:convocatoria IS NULL OR p.convocatoria = :convocatoria)
        """)
    List<Programa> findAllWithFilters(
        @Param("tipoSlug") String tipoSlug,
        @Param("q") String q,
        @Param("modalidad") Modalidad modalidad,
        @Param("idFacultad") Long idFacultad,
        @Param("convocatoria") Boolean convocatoria
    );
}
