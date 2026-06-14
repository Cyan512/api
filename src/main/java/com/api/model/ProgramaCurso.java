package com.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "programa_curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramaCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa idPrograma;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso idCurso;

    private String semestres;
}
