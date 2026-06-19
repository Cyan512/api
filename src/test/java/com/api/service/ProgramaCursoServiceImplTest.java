package com.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.api.dto.ProgramaCursoRequest;
import com.api.model.Curso;
import com.api.model.Programa;
import com.api.model.ProgramaCurso;
import com.api.repository.CursoRepository;
import com.api.repository.ProgramaCursoRepository;
import com.api.repository.ProgramaRepository;
import com.api.service.impl.ProgramaCursoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProgramaCursoServiceImplTest {

    @Mock
    private ProgramaCursoRepository programaCursoRepository;

    @Mock
    private ProgramaRepository programaRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private ProgramaCursoServiceImpl programaCursoService;

    @Test
    void getAll_returnsList() {
        var pc = ProgramaCurso.builder().id(1L).semestre(1).electivo(false).build();
        when(programaCursoRepository.findAll()).thenReturn(List.of(pc));

        var result = programaCursoService.getAllProgramaCursos();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_returnsEmptyList() {
        when(programaCursoRepository.findAll()).thenReturn(List.of());

        var result = programaCursoService.getAllProgramaCursos();

        assertThat(result).isEmpty();
    }

    @Test
    void create_success() {
        var programa = Programa.builder().id(1L).build();
        var curso = Curso.builder().id(1L).build();
        var request = new ProgramaCursoRequest(1, true, BigDecimal.valueOf(500), 1L, 1L);
        var saved = ProgramaCurso.builder().id(1L).idPrograma(programa).idCurso(curso).semestre(1).electivo(true).build();

        when(programaRepository.findById(1L)).thenReturn(Optional.of(programa));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(programaCursoRepository.save(any())).thenReturn(saved);

        var result = programaCursoService.createProgramaCurso(request);

        assertThat(result.getSemestre()).isEqualTo(1);
        assertThat(result.getElectivo()).isTrue();
        verify(programaCursoRepository).save(any());
    }

    @Test
    void create_throwsNotFound_whenProgramaNotExists() {
        var request = new ProgramaCursoRequest(null, false, BigDecimal.valueOf(500), 99L, 1L);
        when(programaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaCursoService.createProgramaCurso(request));
    }

    @Test
    void create_throwsNotFound_whenCursoNotExists() {
        var programa = Programa.builder().id(1L).build();
        var request = new ProgramaCursoRequest(1, false, BigDecimal.valueOf(500), 1L, 99L);
        when(programaRepository.findById(1L)).thenReturn(Optional.of(programa));
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> programaCursoService.createProgramaCurso(request));
    }
}
