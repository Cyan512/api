package com.api.service.impl;

import java.text.Normalizer;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;
import com.api.repository.TipoProgramaRepository;
import com.api.service.TipoProgramaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoProgramaServiceImpl implements TipoProgramaService {

    private final TipoProgramaRepository tipoProgramaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoPrograma> getAllTipoProgramas() {
        return tipoProgramaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoPrograma getTipoProgramaBySlug(String slug) {
        return tipoProgramaRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de programa no encontrado con slug: " + slug
                ));
    }

    @Override
    @Transactional
    public TipoPrograma createTipoPrograma(TipoProgramaRequest request) {
        var slug = generarSlug(request.nombre());

        if (tipoProgramaRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un tipo de programa con el nombre: " + request.nombre()
            );
        }

        var tipoPrograma = TipoPrograma.builder()
                .nombre(request.nombre())
                .imagenCard(request.imagenCard())
                .imagenBg(request.imagenBg())
                .slug(slug)
                .build();
        return tipoProgramaRepository.save(tipoPrograma);
    }

    @Override
    @Transactional
    public TipoPrograma updateTipoPrograma(String slug, TipoProgramaRequest request) {
        var tipoPrograma = getTipoProgramaBySlug(slug);
        var newSlug = generarSlug(request.nombre());

        if (!newSlug.equals(slug) && tipoProgramaRepository.existsBySlug(newSlug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un tipo de programa con el nombre: " + request.nombre()
            );
        }

        tipoPrograma.setNombre(request.nombre());
        tipoPrograma.setImagenCard(request.imagenCard());
        tipoPrograma.setImagenBg(request.imagenBg());
        tipoPrograma.setSlug(newSlug);
        return tipoProgramaRepository.save(tipoPrograma);
    }

    @Override
    @Transactional
    public void deleteTipoPrograma(String slug) {
        var tipoPrograma = getTipoProgramaBySlug(slug);
        tipoProgramaRepository.delete(tipoPrograma);
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
