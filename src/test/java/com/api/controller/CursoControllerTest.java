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
import com.api.model.Categoria;
import com.api.model.Curso;
import com.api.service.CursoService;

@WebMvcTest(CursoController.class)
@Import(GlobalExceptionHandler.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CursoService cursoService;

    @Test
    void listar_returns200_withEmptyList() throws Exception {
        when(cursoService.getAllCursos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void listar_returns200_withData() throws Exception {
        var curso = Curso.builder().id(1L).nombre("Matemáticas").creditos(4).categoria(Categoria.OE).build();
        when(cursoService.getAllCursos()).thenReturn(List.of(curso));

        mockMvc.perform(get("/api/v1/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].nombre").value("Matemáticas"));
    }

    @Test
    void obtenerPorId_returns200() throws Exception {
        var curso = Curso.builder().id(1L).nombre("Matemáticas").creditos(4).categoria(Categoria.OE).build();
        when(cursoService.getCursoById(1L)).thenReturn(curso);

        mockMvc.perform(get("/api/v1/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Matemáticas"));
    }

    @Test
    void obtenerPorId_returns404() throws Exception {
        when(cursoService.getCursoById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        mockMvc.perform(get("/api/v1/cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_returns201() throws Exception {
        var curso = Curso.builder().id(1L).nombre("Matemáticas").creditos(4).categoria(Categoria.OE).build();
        when(cursoService.createCurso(any())).thenReturn(curso);

        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Matemáticas\",\"creditos\":4,\"categoria\":\"OE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Matemáticas"));
    }

    @Test
    void crear_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"creditos\":4,\"categoria\":\"OE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_returns400_whenCreditosIsNull() throws Exception {
        mockMvc.perform(post("/api/v1/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Matemáticas\",\"categoria\":\"OE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void actualizar_returns200() throws Exception {
        var curso = Curso.builder().id(1L).nombre("Física").creditos(3).categoria(Categoria.EE).build();
        when(cursoService.updateCurso(eq(1L), any())).thenReturn(curso);

        mockMvc.perform(put("/api/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Física\",\"creditos\":3,\"categoria\":\"EE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Física"));
    }

    @Test
    void actualizar_returns400_whenNombreIsBlank() throws Exception {
        mockMvc.perform(put("/api/v1/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"creditos\":3,\"categoria\":\"EE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void eliminar_returns200() throws Exception {
        doNothing().when(cursoService).deleteCurso(1L);

        mockMvc.perform(delete("/api/v1/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void eliminar_returns404() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"))
                .when(cursoService).deleteCurso(99L);

        mockMvc.perform(delete("/api/v1/cursos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
