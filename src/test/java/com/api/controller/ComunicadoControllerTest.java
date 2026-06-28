package com.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.api.model.Comunicado;
import com.api.service.ComunicadoService;

@WebMvcTest(ComunicadoController.class)
@Import(GlobalExceptionHandler.class)
class ComunicadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComunicadoService comunicadoService;

    @Test
    void listar_returns200_withEmptyList() throws Exception {
        when(comunicadoService.getAllComunicados()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/comunicados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Comunicados obtenidos exitosamente"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_returns200_withData() throws Exception {
        var comunicado = Comunicado.builder().id(1L).titulo("Comunicado 1").slug("comunicado-1").build();
        when(comunicadoService.getAllComunicados()).thenReturn(List.of(comunicado));

        mockMvc.perform(get("/api/v1/comunicados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].titulo").value("Comunicado 1"));
    }

    @Test
    void obtenerPorSlug_returns200() throws Exception {
        var comunicado = Comunicado.builder().id(1L).titulo("Nuevo Comunicado").slug("nuevo-comunicado").build();
        when(comunicadoService.getComunicadoBySlug("nuevo-comunicado")).thenReturn(comunicado);

        mockMvc.perform(get("/api/v1/comunicados/nuevo-comunicado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.titulo").value("Nuevo Comunicado"));
    }

    @Test
    void obtenerPorSlug_returns404() throws Exception {
        when(comunicadoService.getComunicadoBySlug("inventado"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunicado no encontrado"));

        mockMvc.perform(get("/api/v1/comunicados/inventado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_returns201() throws Exception {
        var comunicado = Comunicado.builder().id(1L).titulo("Nuevo Comunicado").slug("nuevo-comunicado").build();
        when(comunicadoService.createComunicado(any())).thenReturn(comunicado);

        mockMvc.perform(post("/api/v1/comunicados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Nuevo Comunicado\",\"resumen\":\"Resumen del comunicado\",\"contenido\":\"Contenido completo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Comunicado creado exitosamente"))
                .andExpect(jsonPath("$.data.titulo").value("Nuevo Comunicado"));
    }

    @Test
    void crear_returns400_whenTituloIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/comunicados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"\",\"resumen\":\"Resumen\",\"contenido\":\"Contenido\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void crear_returns400_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/v1/comunicados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void actualizar_returns200() throws Exception {
        var comunicado = Comunicado.builder().id(1L).titulo("Comunicado Actualizado").slug("comunicado-actualizado").build();
        when(comunicadoService.updateComunicado(eq("nuevo-comunicado"), any())).thenReturn(comunicado);

        mockMvc.perform(put("/api/v1/comunicados/nuevo-comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Comunicado Actualizado\",\"resumen\":\"Resumen actualizado\",\"contenido\":\"Contenido actualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.titulo").value("Comunicado Actualizado"));
    }

    @Test
    void actualizar_returns400_whenTituloIsBlank() throws Exception {
        mockMvc.perform(put("/api/v1/comunicados/nuevo-comunicado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"\",\"resumen\":\"Resumen\",\"contenido\":\"Contenido\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void eliminar_returns200() throws Exception {
        doNothing().when(comunicadoService).deleteComunicado("nuevo-comunicado");

        mockMvc.perform(delete("/api/v1/comunicados/nuevo-comunicado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void eliminar_returns404() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Comunicado no encontrado"))
                .when(comunicadoService).deleteComunicado("inventado");

        mockMvc.perform(delete("/api/v1/comunicados/inventado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
