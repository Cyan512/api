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

import com.api.dto.ProgramaRequest;
import com.api.model.Facultad;
import com.api.model.Modalidad;
import com.api.model.Programa;
import com.api.model.TipoPrograma;
import com.api.repository.FacultadRepository;
import com.api.repository.ProgramaRepository;
import com.api.repository.TipoProgramaRepository;
import com.api.service.impl.ProgramaServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProgramaServiceImplTest {

    @Mock
    private ProgramaRepository programaRepository;

    @Mock
    private TipoProgramaRepository tipoProgramaRepository;

    @Mock
    private FacultadRepository facultadRepository;

    @InjectMocks
    private ProgramaServiceImpl programaService;

    @Test
    void getAll_returnsList() {
        var programa = Programa.builder().id(1L).nombre("Ingeniería").build();
        when(programaRepository.findAll()).thenReturn(List.of(programa));

        var result = programaService.getAllProgramas();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_returnsEmptyList() {
        when(programaRepository.findAll()).thenReturn(List.of());

        var result = programaService.getAllProgramas();

        assertThat(result).isEmpty();
    }

    @Test
    void create_success() {
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest(1L, "Ingeniería", 1L, true, Modalidad.PRESENCIAL);
        var saved = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();

        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(1L)).thenReturn(Optional.of(facultad));
        when(programaRepository.existsBySlug("ingenieria")).thenReturn(false);
        when(programaRepository.save(any())).thenReturn(saved);

        var result = programaService.createPrograma(request);

        assertThat(result.getSlug()).isEqualTo("ingenieria");
        verify(programaRepository).save(any());
    }

    @Test
    void create_throwsNotFound_whenTipoProgramaNotExists() {
        var request = new ProgramaRequest(99L, "Ingeniería", 1L, true, Modalidad.PRESENCIAL);
        when(tipoProgramaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.createPrograma(request));
    }

    @Test
    void create_throwsNotFound_whenFacultadNotExists() {
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var request = new ProgramaRequest(1L, "Ingeniería", 99L, true, Modalidad.PRESENCIAL);
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.createPrograma(request));
    }

    @Test
    void create_throwsConflict_whenSlugExists() {
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest(1L, "Ingeniería", 1L, true, Modalidad.PRESENCIAL);
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(1L)).thenReturn(Optional.of(facultad));
        when(programaRepository.existsBySlug("ingenieria")).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> programaService.createPrograma(request));
    }

    @Test
    void getByTipoSlug_returnsList() {
        when(tipoProgramaRepository.existsBySlug("maestria")).thenReturn(true);
        var programa = Programa.builder().id(1L).nombre("Maestría en Sistemas").build();
        when(programaRepository.findByIdTipoProgramaSlug("maestria"))
                .thenReturn(List.of(programa));

        var result = programaService.getProgramasByTipoSlug("maestria");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Maestría en Sistemas");
    }

    @Test
    void getByTipoSlug_throwsNotFound_whenSlugNotExists() {
        when(tipoProgramaRepository.existsBySlug("inventado")).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> programaService.getProgramasByTipoSlug("inventado"));
    }
}
