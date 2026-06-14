package com.api.service.impl;

import java.text.Normalizer;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ProgramaRequest;
import com.api.model.Facultad;
import com.api.model.Modalidad;
import com.api.model.Programa;
import com.api.model.TipoPrograma;
import com.api.repository.FacultadRepository;
import com.api.repository.ProgramaRepository;
import com.api.repository.TipoProgramaRepository;
import com.api.service.ProgramaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository programaRepository;
    private final TipoProgramaRepository tipoProgramaRepository;
    private final FacultadRepository facultadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Programa> getProgramas(String tipoSlug, String q, Modalidad modalidad, Long idFacultad, Boolean convocatoria) {
        if (tipoSlug != null && !tipoProgramaRepository.existsBySlug(tipoSlug)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Tipo de programa no encontrado con slug: " + tipoSlug
            );
        }
        return programaRepository.findAllWithFilters(tipoSlug, q, modalidad, idFacultad, convocatoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Programa getProgramaBySlug(String slug) {
        return programaRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Programa no encontrado con slug: " + slug
                ));
    }

    @Override
    @Transactional
    public Programa createPrograma(ProgramaRequest request) {
        var tipoPrograma = tipoProgramaRepository.findById(request.idTipoPrograma())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de programa no encontrado con id: " + request.idTipoPrograma()
                ));

        var facultad = facultadRepository.findById(request.idFacultad())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Facultad no encontrada con id: " + request.idFacultad()
                ));

        var slug = generarSlug(request.nombre());

        if (programaRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un programa con el nombre: " + request.nombre()
            );
        }

        var programa = Programa.builder()
                .idTipoPrograma(tipoPrograma)
                .nombre(request.nombre())
                .idFacultad(facultad)
                .slug(slug)
                .convocatoria(request.convocatoria())
                .modalidad(request.modalidad())
                .build();
        return programaRepository.save(programa);
    }

    private String generarSlug(String nombre) {
        String slug = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        return slug.isBlank() ? "sin-nombre" : slug;
    }
}
