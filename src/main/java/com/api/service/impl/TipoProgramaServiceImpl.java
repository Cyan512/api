package com.api.service.impl;

import java.text.Normalizer;

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
