package com.api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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

    private Integer semestre;

    @Column(nullable = false)
    private Boolean electivo = false;

    @Column(name = "costo_cuota", precision = 10, scale = 2)
    private BigDecimal costoCuota;

    @ManyToOne
    @JoinColumn(name = "id_programa")
    private Programa idPrograma;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso idCurso;

}
