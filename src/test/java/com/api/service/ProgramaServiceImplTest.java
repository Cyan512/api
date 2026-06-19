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
import org.springframework.data.jpa.domain.Specification;
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
    void getProgramas_withoutFilters_callsFindAll() {
        var programa = Programa.builder().id(1L).nombre("Ingeniería").build();
        when(programaRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(programa));

        var result = programaService.getProgramas(null, null, null, null, null);

        assertThat(result).hasSize(1);
    }

    @Test
    void getProgramas_withAllFilters_callsFindAll() {
        when(tipoProgramaRepository.existsBySlug("maestria")).thenReturn(true);
        var programa = Programa.builder().id(1L).nombre("Maestría en Sistemas").build();
        when(programaRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(programa));

        var result = programaService.getProgramas("maestria", "sistemas", Modalidad.VIRTUAL, 1L, true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Maestría en Sistemas");
    }

    @Test
    void getProgramas_throws404_whenTipoSlugNotExists() {
        when(tipoProgramaRepository.existsBySlug("inventado")).thenReturn(false);

        assertThrows(ResponseStatusException.class,
                () -> programaService.getProgramas("inventado", null, null, null, null));
    }

    @Test
    void create_success() {
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 1L, 1L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
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
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 1L, 99L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(tipoProgramaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.createPrograma(request));
    }

    @Test
    void create_throwsNotFound_whenFacultadNotExists() {
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 99L, 1L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.createPrograma(request));
    }

    @Test
    void create_throwsConflict_whenSlugExists() {
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 1L, 1L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(1L)).thenReturn(Optional.of(facultad));
        when(programaRepository.existsBySlug("ingenieria")).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> programaService.createPrograma(request));
    }

    @Test
    void getBySlug_returnsPrograma() {
        var programa = Programa.builder().id(1L).nombre("Ingeniería de Sistemas").slug("ingenieria-sistemas").build();
        when(programaRepository.findBySlug("ingenieria-sistemas")).thenReturn(Optional.of(programa));

        var result = programaService.getProgramaBySlug("ingenieria-sistemas");

        assertThat(result.getNombre()).isEqualTo("Ingeniería de Sistemas");
    }

    @Test
    void getBySlug_throwsNotFound_whenSlugNotExists() {
        when(programaRepository.findBySlug("inventado")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.getProgramaBySlug("inventado"));
    }

    @Test
    void update_success() {
        var existing = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest("Ingeniería", false, null, "Nuevo Objetivo General",
                "Nuevos Objetivos Específicos", "Nuevo Perfil Posgraduado", 1L, 1L, Modalidad.VIRTUAL,
                "Nuevas Líneas de Investigación");
        var updated = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();

        when(programaRepository.findBySlug("ingenieria")).thenReturn(Optional.of(existing));
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(1L)).thenReturn(Optional.of(facultad));
        when(programaRepository.save(any())).thenReturn(updated);

        var result = programaService.updatePrograma("ingenieria", request);

        assertThat(result.getSlug()).isEqualTo("ingenieria");
        verify(programaRepository).save(any());
    }

    @Test
    void update_success_slugChanges() {
        var existing = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest("Maestría en Sistemas", false, null, "Nuevo Objetivo General",
                "Nuevos Objetivos Específicos", "Nuevo Perfil Posgraduado", 1L, 1L, Modalidad.VIRTUAL,
                "Nuevas Líneas de Investigación");
        var updated = Programa.builder().id(1L).nombre("Maestría en Sistemas").slug("maestria-en-sistemas").build();

        when(programaRepository.findBySlug("ingenieria")).thenReturn(Optional.of(existing));
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(1L)).thenReturn(Optional.of(facultad));
        when(programaRepository.existsBySlug("maestria-en-sistemas")).thenReturn(false);
        when(programaRepository.save(any())).thenReturn(updated);

        var result = programaService.updatePrograma("ingenieria", request);

        assertThat(result.getSlug()).isEqualTo("maestria-en-sistemas");
        verify(programaRepository).save(any());
    }

    @Test
    void update_throwsNotFound_whenProgramaNotExists() {
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 1L, 1L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(programaRepository.findBySlug("inventado")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.updatePrograma("inventado", request));
    }

    @Test
    void update_throwsNotFound_whenTipoProgramaNotExists() {
        var existing = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 1L, 99L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(programaRepository.findBySlug("ingenieria")).thenReturn(Optional.of(existing));
        when(tipoProgramaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.updatePrograma("ingenieria", request));
    }

    @Test
    void update_throwsNotFound_whenFacultadNotExists() {
        var existing = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var request = new ProgramaRequest("Ingeniería", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 99L, 1L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(programaRepository.findBySlug("ingenieria")).thenReturn(Optional.of(existing));
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.updatePrograma("ingenieria", request));
    }

    @Test
    void update_throwsConflict_whenSlugExists() {
        var existing = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        var tipoPrograma = TipoPrograma.builder().id(1L).build();
        var facultad = Facultad.builder().id(1L).build();
        var request = new ProgramaRequest("Maestría en Sistemas", true, null, "Objetivo General",
                "Objetivos Específicos", "Perfil Posgraduado", 1L, 1L, Modalidad.PRESENCIAL,
                "Líneas de Investigación");
        when(programaRepository.findBySlug("ingenieria")).thenReturn(Optional.of(existing));
        when(tipoProgramaRepository.findById(1L)).thenReturn(Optional.of(tipoPrograma));
        when(facultadRepository.findById(1L)).thenReturn(Optional.of(facultad));
        when(programaRepository.existsBySlug("maestria-en-sistemas")).thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> programaService.updatePrograma("ingenieria", request));
    }

    @Test
    void delete_success() {
        var programa = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        when(programaRepository.findBySlug("ingenieria")).thenReturn(Optional.of(programa));

        programaService.deletePrograma("ingenieria");

        verify(programaRepository).delete(programa);
    }

    @Test
    void delete_throwsNotFound_whenProgramaNotExists() {
        when(programaRepository.findBySlug("inventado")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaService.deletePrograma("inventado"));
    }
}
