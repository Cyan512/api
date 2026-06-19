package com.api.service.impl;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ComunicadoRequest;
import com.api.model.Comunicado;
import com.api.repository.ComunicadoRepository;
import com.api.service.ComunicadoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComunicadoServiceImpl implements ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comunicado> getAllComunicados() {
        return comunicadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comunicado getComunicadoBySlug(String slug) {
        return comunicadoRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Comunicado no encontrado con slug: " + slug
                ));
    }

    @Override
    @Transactional
    public Comunicado createComunicado(ComunicadoRequest request) {
        var slug = generarSlug(request.titulo());

        if (comunicadoRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un comunicado con el titulo: " + request.titulo()
            );
        }

        var comunicado = Comunicado.builder()
                .titulo(request.titulo())
                .slug(slug)
                .resumen(request.resumen())
                .contenido(request.contenido())
                .imagen(request.imagen())
                .fechaPublicacion(LocalDateTime.now())
                .build();
        return comunicadoRepository.save(comunicado);
    }

    @Override
    @Transactional
    public Comunicado updateComunicado(String slug, ComunicadoRequest request) {
        var comunicado = getComunicadoBySlug(slug);
        var newSlug = generarSlug(request.titulo());

        if (!newSlug.equals(slug) && comunicadoRepository.existsBySlug(newSlug)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un comunicado con el titulo: " + request.titulo()
            );
        }

        comunicado.setTitulo(request.titulo());
        comunicado.setSlug(newSlug);
        comunicado.setResumen(request.resumen());
        comunicado.setContenido(request.contenido());
        comunicado.setImagen(request.imagen());
        return comunicadoRepository.save(comunicado);
    }

    @Override
    @Transactional
    public void deleteComunicado(String slug) {
        var comunicado = getComunicadoBySlug(slug);
        comunicadoRepository.delete(comunicado);
    }

    private String generarSlug(String titulo) {
        String slug = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        return slug.isBlank() ? "sin-titulo" : slug;
    }
}
