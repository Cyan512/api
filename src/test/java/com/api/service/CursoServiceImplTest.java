package com.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.dto.CursoRequest;
import com.api.enums.Categoria;
import com.api.model.Curso;
import com.api.repository.CursoRepository;
import com.api.service.impl.CursoServiceImpl;

@ExtendWith(MockitoExtension.class)
class CursoServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoServiceImpl cursoService;

    @Test
    void getAll_returnsList() {
        var curso = Curso.builder().id(1L).nombre("Matemáticas").creditos(4).categoria(Categoria.OE).build();
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        var result = cursoService.getAllCursos();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_returnsEmptyList() {
        when(cursoRepository.findAll()).thenReturn(List.of());

        var result = cursoService.getAllCursos();

        assertThat(result).isEmpty();
    }

    @Test
    void create_success() {
        var request = new CursoRequest("Matemáticas", 4, Categoria.OE);
        var saved = Curso.builder().id(1L).nombre("Matemáticas").creditos(4).categoria(Categoria.OE).build();
        when(cursoRepository.save(any())).thenReturn(saved);

        var result = cursoService.createCurso(request);

        assertThat(result.getNombre()).isEqualTo("Matemáticas");
        verify(cursoRepository).save(any());
    }
}
