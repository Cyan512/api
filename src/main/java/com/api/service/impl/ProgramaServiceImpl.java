package com.api.service.impl;

import java.text.Normalizer;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ProgramaRequest;
import com.api.enums.Modalidad;
import com.api.model.Programa;
import com.api.repository.FacultadRepository;
import com.api.repository.ProgramaRepository;
import com.api.repository.ProgramaSpecification;
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
        var spec = ProgramaSpecification.conFiltros(tipoSlug, q, modalidad, idFacultad, convocatoria);
        return programaRepository.findAll(spec);
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
        var tipoPrograma = tipoProgramaRepository.findById(request.tipoProgramaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de programa no encontrado con id: " + request.tipoProgramaId()
                ));

        var facultad = facultadRepository.findById(request.facultadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Facultad no encontrada con id: " + request.facultadId()
                ));

        var slug = generarSlug(request.nombre());

        if (programaRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un programa con el nombre: " + request.nombre()
            );
        }

        var programa = Programa.builder()
                .tipoPrograma(tipoPrograma)
                .nombre(request.nombre())
                .facultad(facultad)
                .slug(slug)
                .enConvocatoria(request.enConvocatoria() != null ? request.enConvocatoria() : false)
                .modalidad(request.modalidad())
                .imageUrl(request.imageUrl())
                .objetivoGeneral(request.objetivoGeneral())
                .objetivosEspecificos(request.objetivosEspecificos())
                .lineasInvestigacion(request.lineasInvestigacion())
                .perfilPosgraduado(request.perfilPosgraduado())
                .costoMatricula(request.costoMatricula())
                .build();
        return programaRepository.save(programa);
    }

    @Override
    @Transactional
    public Programa updatePrograma(String slug, ProgramaRequest request) {
        var programa = getProgramaBySlug(slug);

        var tipoPrograma = tipoProgramaRepository.findById(request.tipoProgramaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de programa no encontrado con id: " + request.tipoProgramaId()
                ));

        var facultad = facultadRepository.findById(request.facultadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Facultad no encontrada con id: " + request.facultadId()
                ));

        var newSlug = generarSlug(request.nombre());

        if (!newSlug.equals(slug) && programaRepository.existsBySlug(newSlug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un programa con el nombre: " + request.nombre()
            );
        }

        programa.setTipoPrograma(tipoPrograma);
        programa.setNombre(request.nombre());
        programa.setFacultad(facultad);
        programa.setSlug(newSlug);
        programa.setEnConvocatoria(request.enConvocatoria() != null ? request.enConvocatoria() : false);
        programa.setModalidad(request.modalidad());
        programa.setImageUrl(request.imageUrl());
        programa.setObjetivoGeneral(request.objetivoGeneral());
        programa.setObjetivosEspecificos(request.objetivosEspecificos());
        programa.setLineasInvestigacion(request.lineasInvestigacion());
        programa.setPerfilPosgraduado(request.perfilPosgraduado());
        programa.setCostoMatricula(request.costoMatricula());
        return programaRepository.save(programa);
    }

    @Override
    @Transactional
    public void deletePrograma(String slug) {
        var programa = getProgramaBySlug(slug);
        programaRepository.delete(programa);
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
