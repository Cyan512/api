package com.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    
    private String nombre;

    @Column(unique = true)
    private String slug;

    private String imagen;

    private Boolean convocatoria;
    
    @Column(name = "objetivo_general")
    private String objetivoGeneral;

    @Column(name = "objetivos_especificos")
    private String objetivosEspecificos;

    @Column(name = "perfil_posgraduado")
    private String perfilPosgraduado;

    @ManyToOne
    @JoinColumn(name = "id_facultad")
    private Facultad idFacultad;

    @ManyToOne
    @JoinColumn(name = "id_tipo_programa")
    private TipoPrograma idTipoPrograma;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @Column(name = "lineas_investigacion")
    private String lineasInvestigacion;
}
