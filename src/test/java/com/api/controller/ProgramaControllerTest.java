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
import com.api.config.TestSecurityConfig;
import com.api.enums.Modalidad;
import com.api.model.Programa;
import com.api.service.ProgramaService;

@WebMvcTest(ProgramaController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
class ProgramaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProgramaService programaService;

    @Test
    void listar_sinFiltros_returns200() throws Exception {
        when(programaService.getProgramas(null, null, null, null, null)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/programas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_conTodosLosFiltros_returns200() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Maestría en Sistemas").build();
        when(programaService.getProgramas("maestria", "sistemas", Modalidad.VIRTUAL, 1L, true))
                .thenReturn(List.of(programa));

        mockMvc.perform(get("/api/v1/programas?tipoSlug=maestria&q=sistemas&modalidad=VIRTUAL&idFacultad=1&convocatoria=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombre").value("Maestría en Sistemas"));
    }

    @Test
    void crear_returns201() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Ingeniería").slug("ingenieria").build();
        when(programaService.createPrograma(any())).thenReturn(programa);

        mockMvc.perform(post("/api/v1/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ingeniería\",\"enConvocatoria\":true,\"imageUrl\":\"https://img.jpg\",\"objetivoGeneral\":\"Formar profesionales\",\"objetivosEspecificos\":[\"Desarrollar competencias\"],\"perfilPosgraduado\":\"Perfil profesional\",\"facultadId\":1,\"tipoProgramaId\":1,\"modalidad\":\"PRESENCIAL\",\"lineasInvestigacion\":[\"Ingeniería de software\"],\"costoMatricula\":1000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Ingeniería"));
    }

    @Test
    void crear_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/programas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"facultadId\":1,\"tipoProgramaId\":1,\"modalidad\":\"PRESENCIAL\",\"imageUrl\":\"https://img.jpg\",\"objetivoGeneral\":\"Obj\",\"objetivosEspecificos\":[\"Esp\"],\"perfilPosgraduado\":\"Perfil\",\"lineasInvestigacion\":[\"Lineas\"],\"costoMatricula\":1000}"))
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

    @Test
    void actualizar_returns200() throws Exception {
        var programa = Programa.builder().id(1L).nombre("Ingeniería Actualizada").slug("ingenieria-actualizada").build();
        when(programaService.updatePrograma(eq("ingenieria-sistemas"), any())).thenReturn(programa);

        mockMvc.perform(put("/api/v1/programas/ingenieria-sistemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ingeniería Actualizada\",\"enConvocatoria\":true,\"imageUrl\":\"https://img.jpg\",\"objetivoGeneral\":\"Formar profesionales\",\"objetivosEspecificos\":[\"Desarrollar competencias\"],\"perfilPosgraduado\":\"Perfil profesional\",\"facultadId\":1,\"tipoProgramaId\":1,\"modalidad\":\"VIRTUAL\",\"lineasInvestigacion\":[\"Ingeniería de software\"],\"costoMatricula\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Ingeniería Actualizada"));
    }

    @Test
    void actualizar_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(put("/api/v1/programas/ingenieria-sistemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"facultadId\":1,\"tipoProgramaId\":1,\"modalidad\":\"VIRTUAL\",\"imageUrl\":\"https://img.jpg\",\"objetivoGeneral\":\"Obj\",\"objetivosEspecificos\":[\"Esp\"],\"perfilPosgraduado\":\"Perfil\",\"lineasInvestigacion\":[\"Lineas\"],\"costoMatricula\":1000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void eliminar_returns200() throws Exception {
        doNothing().when(programaService).deletePrograma("ingenieria-sistemas");

        mockMvc.perform(delete("/api/v1/programas/ingenieria-sistemas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void eliminar_returns404() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Programa no encontrado"))
                .when(programaService).deletePrograma("inventado");

        mockMvc.perform(delete("/api/v1/programas/inventado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
