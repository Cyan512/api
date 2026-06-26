package com.api.model;

import java.math.BigDecimal;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.api.enums.Modalidad;

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
@Table(name = "programas")
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

    @Column(name = "image_url", nullable = false, columnDefinition = "text")
    private String imageUrl;

    @Column(name = "en_convocatoria", nullable = false)
    @Builder.Default
    private Boolean enConvocatoria = false;

    @Column(name = "objetivo_general", nullable = false, columnDefinition = "text")
    private String objetivoGeneral;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "objetivos_especificos", columnDefinition = "text[]")
    private String[] objetivosEspecificos;

    @Column(name = "perfil_posgraduado", nullable = false, columnDefinition = "text")
    private String perfilPosgraduado;

    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    @ManyToOne
    @JoinColumn(name = "tipo_programa_id")
    private TipoPrograma tipoPrograma;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "lineas_investigacion", columnDefinition = "text[]")
    private String[] lineasInvestigacion;

    @Column(name = "costo_matricula", precision = 10, scale = 2)
    private BigDecimal costoMatricula;
}
