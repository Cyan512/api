package com.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.config.GlobalExceptionHandler;
import com.api.model.Facultad;
import com.api.service.FacultadService;

@WebMvcTest(FacultadController.class)
@Import(GlobalExceptionHandler.class)
class FacultadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultadService facultadService;

    @Test
    void listar_returns200_withEmptyList() throws Exception {
        when(facultadService.getAllFacultades()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/facultades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_returns200_withData() throws Exception {
        var facultad = Facultad.builder().id(1L).nombre("Ingeniería").build();
        when(facultadService.getAllFacultades()).thenReturn(List.of(facultad));

        mockMvc.perform(get("/api/v1/facultades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombre").value("Ingeniería"));
    }

    @Test
    void crear_returns201() throws Exception {
        var facultad = Facultad.builder().id(1L).nombre("Ingeniería").build();
        when(facultadService.createFacultad(any())).thenReturn(facultad);

        mockMvc.perform(post("/api/v1/facultades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ingeniería\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Ingeniería"));
    }

    @Test
    void crear_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/facultades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void crear_returns400_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/facultades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
