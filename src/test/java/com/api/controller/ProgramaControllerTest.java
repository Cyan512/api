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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.api.config.GlobalExceptionHandler;
import com.api.dto.ProgramaRequest;
import com.api.model.Modalidad;
import com.api.model.Programa;
import com.api.service.ProgramaService;

@WebMvcTest(ProgramaController.class)
@Import(GlobalExceptionHandler.class)
class ProgramaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProgramaService programaService;

    @Test
    void listar_returns200_withEmptyList() throws Exception {
        when(programaService.getAllProgramas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/programas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_returns200_withData() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        when(programaService.getAllProgramas()).thenReturn(List.of(programa));

        mockMvc.perform(get("/api/v1/programas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombre").value("Ingeniería"));
    }

    @Test
    void crear_returns201() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        when(programaService.createPrograma(any())).thenReturn(programa);

        mockMvc.perform(post("/api/v1/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idTipoPrograma\":1,\"nombre\":\"Ingeniería\",\"idFacultad\":1,\"convocatoria\":true,\"modalidad\":\"PRESENCIAL\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Ingeniería"));
    }

    @Test
    void crear_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idTipoPrograma\":1,\"nombre\":\"\",\"idFacultad\":1,\"modalidad\":\"PRESENCIAL\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void crear_returns400_whenRequiredFieldsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void listarPorTipo_returns200_withData() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Maestría en Sistemas").build();
        when(programaService.getProgramasByTipoSlug("maestria"))
                .thenReturn(List.of(programa));

        mockMvc.perform(get("/api/v1/programas?tipoSlug=maestria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombre").value("Maestría en Sistemas"));
    }

    @Test
    void listarPorTipo_returns200_withEmptyList() throws Exception {
        when(programaService.getProgramasByTipoSlug("inventado"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/programas?tipoSlug=inventado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void obtenerPorSlug_returns200() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Ingeniería de Sistemas").slug("ingenieria-sistemas").build();
        when(programaService.getProgramaBySlug("ingenieria-sistemas")).thenReturn(programa);

        mockMvc.perform(get("/api/v1/programas/ingenieria-sistemas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Ingeniería de Sistemas"));
    }

    @Test
    void obtenerPorSlug_returns404() throws Exception {
        when(programaService.getProgramaBySlug("inventado"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Programa no encontrado"));

        mockMvc.perform(get("/api/v1/programas/inventado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
