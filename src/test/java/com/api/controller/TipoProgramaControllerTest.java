package com.api.controller;

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
import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;
import com.api.service.TipoProgramaService;

@WebMvcTest(TipoProgramaController.class)
@Import(GlobalExceptionHandler.class)
class TipoProgramaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoProgramaService tipoProgramaService;

    @Test
    void listar_returns200_withEmptyList() throws Exception {
        when(tipoProgramaService.getAllTipoProgramas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/tipos-programa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tipos de programa obtenidos exitosamente"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_returns200_withData() throws Exception {
        var tipo = TipoPrograma.builder().id(1L).nombre("Pregrado").slug("pregrado").build();
        when(tipoProgramaService.getAllTipoProgramas()).thenReturn(List.of(tipo));

        mockMvc.perform(get("/api/v1/tipos-programa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nombre").value("Pregrado"));
    }

    @Test
    void crear_returns201() throws Exception {
        var tipo = TipoPrograma.builder().id(1L).nombre("Pregrado").slug("pregrado").build();
        when(tipoProgramaService.createTipoPrograma(new TipoProgramaRequest("Pregrado", null, null)))
                .thenReturn(tipo);

        mockMvc.perform(post("/api/v1/tipos-programa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Pregrado\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tipo de programa creado exitosamente"))
                .andExpect(jsonPath("$.data.nombre").value("Pregrado"));
    }

    @Test
    void crear_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/tipos-programa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void crear_returns400_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/tipos-programa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }
}
