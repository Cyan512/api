package com.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.api.model.ProgramaCurso;
import com.api.service.ProgramaCursoService;

@WebMvcTest(ProgramaCursoController.class)
@Import(GlobalExceptionHandler.class)
class ProgramaCursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProgramaCursoService programaCursoService;

    @Test
    void listar_returns200_withEmptyList() throws Exception {
        when(programaCursoService.getAllProgramaCursos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/programas-cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_returns200_withData() throws Exception {
        var pc = ProgramaCurso.builder().id(1L).semestre(1).electivo(false).build();
        when(programaCursoService.getAllProgramaCursos()).thenReturn(List.of(pc));

        mockMvc.perform(get("/api/v1/programas-cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].semestre").value(1))
                .andExpect(jsonPath("$.data[0].electivo").value(false));
    }

    @Test
    void listar_porProgramaId_returns200() throws Exception {
        var pc = ProgramaCurso.builder().id(1L).semestre(1).electivo(false).build();
        when(programaCursoService.getProgramaCursosByProgramaId(1L)).thenReturn(List.of(pc));

        mockMvc.perform(get("/api/v1/programas-cursos?programaId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].semestre").value(1))
                .andExpect(jsonPath("$.data[0].electivo").value(false));
    }

    @Test
    void listar_porCursoId_returns200() throws Exception {
        var pc = ProgramaCurso.builder().id(1L).semestre(1).electivo(false).build();
        when(programaCursoService.getProgramaCursosByCursoId(1L)).thenReturn(List.of(pc));

        mockMvc.perform(get("/api/v1/programas-cursos?cursoId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].semestre").value(1))
                .andExpect(jsonPath("$.data[0].electivo").value(false));
    }

    @Test
    void obtenerPorId_returns200() throws Exception {
        var pc = ProgramaCurso.builder().id(1L).semestre(1).electivo(false).build();
        when(programaCursoService.getProgramaCursoById(1L)).thenReturn(pc);

        mockMvc.perform(get("/api/v1/programas-cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.semestre").value(1))
                .andExpect(jsonPath("$.data.electivo").value(false));
    }

    @Test
    void obtenerPorId_returns404() throws Exception {
        when(programaCursoService.getProgramaCursoById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Asociación no encontrada"));

        mockMvc.perform(get("/api/v1/programas-cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_returns201() throws Exception {
        var pc = ProgramaCurso.builder().id(1L).semestre(1).electivo(false).build();
        when(programaCursoService.createProgramaCurso(any())).thenReturn(pc);

        mockMvc.perform(post("/api/v1/programas-cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idPrograma\":1,\"idCurso\":1,\"semestre\":1,\"electivo\":false,\"costoCuota\":500}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.semestre").value(1))
                .andExpect(jsonPath("$.data.electivo").value(false));
    }

    @Test
    void crear_returns400_whenRequiredFieldsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/programas-cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void eliminar_returns200() throws Exception {
        doNothing().when(programaCursoService).deleteProgramaCurso(1L);

        mockMvc.perform(delete("/api/v1/programas-cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void eliminar_returns404() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Asociación no encontrada"))
                .when(programaCursoService).deleteProgramaCurso(99L);

        mockMvc.perform(delete("/api/v1/programas-cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
