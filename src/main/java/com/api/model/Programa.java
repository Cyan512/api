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
@Table(name = "programa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Programa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tipo_programa")
    private TipoPrograma idTipoPrograma;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_facultad")
    private Facultad idFacultad;

    private Long slug;

    private Boolean convocatoria;

    @ManyToOne
    @JoinColumn(name = "id_modalidad")
    private Modalidad idModalidad;
}
