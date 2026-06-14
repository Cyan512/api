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

import com.api.dto.FacultadRequest;
import com.api.model.Facultad;
import com.api.repository.FacultadRepository;
import com.api.service.impl.FacultadServiceImpl;

@ExtendWith(MockitoExtension.class)
class FacultadServiceImplTest {

    @Mock
    private FacultadRepository facultadRepository;

    @InjectMocks
    private FacultadServiceImpl facultadService;

    @Test
    void getAll_returnsList() {
        var facultad = Facultad.builder().id(1L).nombre("Ingeniería").build();
        when(facultadRepository.findAll()).thenReturn(List.of(facultad));

        var result = facultadService.getAllFacultades();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_returnsEmptyList() {
        when(facultadRepository.findAll()).thenReturn(List.of());

        var result = facultadService.getAllFacultades();

        assertThat(result).isEmpty();
    }

    @Test
    void create_success() {
        var request = new FacultadRequest("Ingeniería");
        var saved = Facultad.builder().id(1L).nombre("Ingeniería").build();
        when(facultadRepository.save(any())).thenReturn(saved);

        var result = facultadService.createFacultad(request);

        assertThat(result.getNombre()).isEqualTo("Ingeniería");
        verify(facultadRepository).save(any());
    }
}
