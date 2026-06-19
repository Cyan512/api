package com.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ComunicadoRequest;
import com.api.model.Comunicado;
import com.api.repository.ComunicadoRepository;
import com.api.service.impl.ComunicadoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ComunicadoServiceImplTest {

    @Mock
    private ComunicadoRepository comunicadoRepository;

    @InjectMocks
    private ComunicadoServiceImpl comunicadoService;

    @Test
    void getAll_returnsList() {
        var c1 = Comunicado.builder().id(1L).titulo("Comunicado 1").slug("comunicado-1").build();
        var c2 = Comunicado.builder().id(2L).titulo("Comunicado 2").slug("comunicado-2").build();
        when(comunicadoRepository.findAll()).thenReturn(List.of(c1, c2));

        var result = comunicadoService.getAllComunicados();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitulo()).isEqualTo("Comunicado 1");
    }

    @Test
    void getAll_returnsEmptyList() {
        when(comunicadoRepository.findAll()).thenReturn(List.of());

        var result = comunicadoService.getAllComunicados();

        assertThat(result).isEmpty();
    }

    @Test
    void getBySlug_returnsComunicado() {
        var comunicado = Comunicado.builder().id(1L).titulo("Nuevo Comunicado").slug("nuevo-comunicado").build();
        when(comunicadoRepository.findBySlug("nuevo-comunicado")).thenReturn(Optional.of(comunicado));

        var result = comunicadoService.getComunicadoBySlug("nuevo-comunicado");

        assertThat(result.getTitulo()).isEqualTo("Nuevo Comunicado");
    }

    @Test
    void getBySlug_throwsNotFound_whenSlugNotExists() {
        when(comunicadoRepository.findBySlug("inventado")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> comunicadoService.getComunicadoBySlug("inventado"));
    }

    @Test
    void create_success() {
        var request = new ComunicadoRequest("Nuevo Comunicado", "Resumen del comunicado", "Contenido completo", null);
        var saved = Comunicado.builder().id(1L).titulo("Nuevo Comunicado").slug("nuevo-comunicado").build();
        when(comunicadoRepository.existsBySlug("nuevo-comunicado")).thenReturn(false);
        when(comunicadoRepository.save(any())).thenReturn(saved);

        var result = comunicadoService.createComunicado(request);

        assertThat(result.getSlug()).isEqualTo("nuevo-comunicado");
        assertThat(result.getTitulo()).isEqualTo("Nuevo Comunicado");
        verify(comunicadoRepository).save(any());
    }

    @Test
    void create_throwsConflict_whenSlugExists() {
        var request = new ComunicadoRequest("Nuevo Comunicado", "Resumen", "Contenido", null);
        when(comunicadoRepository.existsBySlug("nuevo-comunicado")).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> comunicadoService.createComunicado(request));
    }

    @Test
    void update_success() {
        var existing = Comunicado.builder().id(1L).titulo("Original").slug("original").build();
        var request = new ComunicadoRequest("Original", "Resumen actualizado", "Contenido actualizado", "img.jpg");
        var updated = Comunicado.builder().id(1L).titulo("Original").slug("original").build();

        when(comunicadoRepository.findBySlug("original")).thenReturn(Optional.of(existing));
        when(comunicadoRepository.save(any())).thenReturn(updated);

        var result = comunicadoService.updateComunicado("original", request);

        assertThat(result.getSlug()).isEqualTo("original");
        verify(comunicadoRepository).save(any());
    }

    @Test
    void update_success_slugChanges() {
        var existing = Comunicado.builder().id(1L).titulo("Original").slug("original").build();
        var request = new ComunicadoRequest("Nuevo Titulo", "Resumen actualizado", "Contenido actualizado", null);
        var updated = Comunicado.builder().id(1L).titulo("Nuevo Titulo").slug("nuevo-titulo").build();

        when(comunicadoRepository.findBySlug("original")).thenReturn(Optional.of(existing));
        when(comunicadoRepository.existsBySlug("nuevo-titulo")).thenReturn(false);
        when(comunicadoRepository.save(any())).thenReturn(updated);

        var result = comunicadoService.updateComunicado("original", request);

        assertThat(result.getSlug()).isEqualTo("nuevo-titulo");
        verify(comunicadoRepository).save(any());
    }

    @Test
    void update_throwsNotFound_whenComunicadoNotExists() {
        var request = new ComunicadoRequest("Nuevo", "Resumen", "Contenido", null);
        when(comunicadoRepository.findBySlug("inventado")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> comunicadoService.updateComunicado("inventado", request));
    }

    @Test
    void update_throwsConflict_whenSlugExists() {
        var existing = Comunicado.builder().id(1L).titulo("Original").slug("original").build();
        var request = new ComunicadoRequest("Nuevo Titulo", "Resumen", "Contenido", null);
        when(comunicadoRepository.findBySlug("original")).thenReturn(Optional.of(existing));
        when(comunicadoRepository.existsBySlug("nuevo-titulo")).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> comunicadoService.updateComunicado("original", request));
    }

    @Test
    void delete_success() {
        var comunicado = Comunicado.builder().id(1L).titulo("Comunicado").slug("comunicado").build();
        when(comunicadoRepository.findBySlug("comunicado")).thenReturn(Optional.of(comunicado));

        comunicadoService.deleteComunicado("comunicado");

        verify(comunicadoRepository).delete(comunicado);
    }

    @Test
    void delete_throwsNotFound_whenComunicadoNotExists() {
        when(comunicadoRepository.findBySlug("inventado")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> comunicadoService.deleteComunicado("inventado"));
    }
}
