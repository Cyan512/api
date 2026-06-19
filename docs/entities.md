# Entidades y relaciones

## Diagrama de relaciones

```
TipoPrograma (1) ────< (*) Programa
Facultad     (1) ────< (*) Programa
Programa     (1) ────< (*) ProgramaCurso
Curso        (1) ────< (*) ProgramaCurso
```

---

## TipoPrograma

**Tabla:** `tipo_programa`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `nombre` | `String` | Nombre del tipo de programa |
| `imagenCard` | `String` | URL de imagen para vista de tarjeta |
| `imagenBg` | `String` | URL de imagen de fondo |
| `slug` | `String` | Identificador único para URL (se genera automáticamente) |

**Slug:** Se genera automáticamente desde el `nombre` al crear (ej: "Maestría" → "maestria"). Es **único**.

---

## Facultad

**Tabla:** `facultad`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `nombre` | `String` | Nombre de la facultad |

---

## Curso

**Tabla:** `curso`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `nombre` | `String` | Nombre del curso |
| `creditos` | `Integer` | Número de créditos |
| `categoria` | `Categoria` (enum) | `OE` (obligatorio específico) o `EE` (electivo específico) |

---

## Programa

**Tabla:** `programa`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `idTipoPrograma` | `TipoPrograma` (FK → `tipo_programa.id`) | Tipo de programa al que pertenece |
| `nombre` | `String` | Nombre del programa |
| `idFacultad` | `Facultad` (FK → `facultad.id`) | Facultad a la que pertenece |
| `slug` | `String` (unique) | Identificador único para URL (se genera automáticamente) |
| `convocatoria` | `Boolean` | Indica si el programa tiene convocatoria abierta |
| `modalidad` | `Modalidad` (enum) | `PRESENCIAL`, `SEMIPRESENCIAL` o `VIRTUAL` |
| `imagen` | `String` | URL de la imagen del programa |
| `objetivoGeneral` | `Text` | Objetivo general del programa |
| `objetivosEspecificos` | `Text` | Objetivos específicos del programa |
| `perfilPosgraduado` | `Text` | Perfil del posgraduado |
| `lineasInvestigacion` | `Text` | Líneas de investigación |

**Slug:** Se genera automáticamente desde el `nombre` al crear. Es **único**.

---

## Comunicado

**Tabla:** `comunicado`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `titulo` | `String` | Título del comunicado |
| `slug` | `String` (unique) | Identificador único para URL (se genera automáticamente) |
| `resumen` | `Text` | Resumen del comunicado |
| `contenido` | `Text` | Contenido completo del comunicado |
| `imagen` | `String` | URL de la imagen del comunicado |
| `fechaPublicacion` | `LocalDateTime` | Fecha y hora de publicación |

**Slug:** Se genera automáticamente desde el `titulo` al crear. Es **único**.

---

## ProgramaCurso

**Tabla:** `programa_curso`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `idPrograma` | `Programa` (FK → `programa.id`) | Programa asociado |
| `idCurso` | `Curso` (FK → `curso.id`) | Curso asociado |
| `semestre` | `Integer` | Semestre en que se dicta (`null` si es electivo) |
| `electivo` | `Boolean` | `true` si es electivo, `false` si es obligatorio |
| `costoCuota` | `BigDecimal` | Costo de la cuota del curso |
