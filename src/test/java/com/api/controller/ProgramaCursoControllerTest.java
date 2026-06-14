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
        var pc = ProgramaCurso.builder().id(1L).semestres("I").build();
        when(programaCursoService.getAllProgramaCursos()).thenReturn(List.of(pc));

        mockMvc.perform(get("/api/v1/programas-cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].semestres").value("I"));
    }

    @Test
    void crear_returns201() throws Exception {
        var pc = ProgramaCurso.builder().id(1L).semestres("I").build();
        when(programaCursoService.createProgramaCurso(any())).thenReturn(pc);

        mockMvc.perform(post("/api/v1/programas-cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idPrograma\":1,\"idCurso\":1,\"semestres\":\"I\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.semestres").value("I"));
    }

    @Test
    void crear_returns400_whenSemestresIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/programas-cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idPrograma\":1,\"idCurso\":1,\"semestres\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error de validación"));
    }

    @Test
    void crear_returns400_whenRequiredFieldsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/programas-cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
