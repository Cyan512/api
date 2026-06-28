package com.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;
import com.api.repository.TipoProgramaRepository;
import com.api.service.impl.TipoProgramaServiceImpl;

@ExtendWith(MockitoExtension.class)
class TipoProgramaServiceImplTest {

    @Mock
    private TipoProgramaRepository tipoProgramaRepository;

    @InjectMocks
    private TipoProgramaServiceImpl tipoProgramaService;

    @Test
    void getAll_returnsList() {
        var tipo1 = TipoPrograma.builder().id(1L).nombre("Pregrado").slug("pregrado").build();
        var tipo2 = TipoPrograma.builder().id(2L).nombre("Posgrado").slug("posgrado").build();
        when(tipoProgramaRepository.findAll()).thenReturn(List.of(tipo1, tipo2));

        var result = tipoProgramaService.getAllTipoProgramas();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNombre()).isEqualTo("Pregrado");
    }

    @Test
    void getAll_returnsEmptyList() {
        when(tipoProgramaRepository.findAll()).thenReturn(List.of());

        var result = tipoProgramaService.getAllTipoProgramas();

        assertThat(result).isEmpty();
    }

    @Test
    void create_success() {
        var request = new TipoProgramaRequest("Pregrado", null, null);
        var saved = TipoPrograma.builder().id(1L).nombre("Pregrado").slug("pregrado").build();
        when(tipoProgramaRepository.existsBySlug("pregrado")).thenReturn(false);
        when(tipoProgramaRepository.save(any())).thenReturn(saved);

        var result = tipoProgramaService.createTipoPrograma(request);

        assertThat(result.getSlug()).isEqualTo("pregrado");
        assertThat(result.getNombre()).isEqualTo("Pregrado");
        verify(tipoProgramaRepository).save(any());
    }

    @Test
    void create_throwsConflict_whenSlugExists() {
        var request = new TipoProgramaRequest("Pregrado", null, null);
        when(tipoProgramaRepository.existsBySlug("pregrado")).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> tipoProgramaService.createTipoPrograma(request));
    }
}
