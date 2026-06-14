package com.api.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.api.model.Modalidad;
import com.api.model.Programa;

import jakarta.persistence.criteria.Predicate;

public class ProgramaSpecification {

    public static Specification<Programa> conFiltros(
            String tipoSlug, String q, Modalidad modalidad,
            Long idFacultad, Boolean convocatoria) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipoSlug != null) {
                predicates.add(cb.equal(root.get("idTipoPrograma").get("slug"), tipoSlug));
            }
            if (q != null && !q.isEmpty()) {
                predicates.add(cb.like(
                    cb.lower(root.get("nombre")),
                    "%" + q.toLowerCase() + "%"
                ));
            }
            if (modalidad != null) {
                predicates.add(cb.equal(root.get("modalidad"), modalidad));
            }
            if (idFacultad != null) {
                predicates.add(cb.equal(root.get("idFacultad").get("id"), idFacultad));
            }
            if (convocatoria != null) {
                predicates.add(cb.equal(root.get("convocatoria"), convocatoria));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
