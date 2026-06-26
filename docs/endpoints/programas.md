# Programas

**Base:** `/api/v1/programas`

---

## GET /api/v1/programas

Lista programas con filtros opcionales.

### Parametros (todos opcionales)

| Parametro | Tipo | Ejemplo | Descripcion |
|-----------|------|---------|-------------|
| `tipoSlug` | `string` | `maestria` | Filtra por tipo de programa (slug) |
| `q` | `string` | `sistemas` | Busqueda textual por nombre |
| `modalidad` | `string` | `VIRTUAL` | Filtra por modalidad (`PRESENCIAL`, `SEMIPRESENCIAL`, `VIRTUAL`) |
| `idFacultad` | `number` | `1` | Filtra por ID de facultad |
| `convocatoria` | `boolean` | `true` | Filtra por convocatoria abierta/cerrada |

**Respuesta:**
```json
{
  "success": true,
  "message": "Programas obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "tipoPrograma": {
        "id": 1,
        "nombre": "Pregrado",
        "cardImageUrl": "pregrado-card.jpg",
        "heroBgUrl": "pregrado-bg.jpg",
        "slug": "pregrado"
      },
      "nombre": "Ingenieria de Sistemas",
      "facultad": {
        "id": 1,
        "nombre": "Ingenieria"
      },
      "slug": "ingenieria-de-sistemas",
      "enConvocatoria": true,
      "modalidad": "PRESENCIAL",
      "imageUrl": "https://ejemplo.com/imagen.jpg",
      "objetivoGeneral": "Formar profesionales en ingenieria de sistemas",
      "objetivosEspecificos": [
        "Desarrollar competencias en desarrollo de software",
        "Fomentar la investigacion aplicada"
      ],
      "perfilPosgraduado": "Profesional capacitado para liderar proyectos tecnologicos",
      "lineasInvestigacion": [
        "Ingenieria de software",
        "Inteligencia artificial"
      ],
      "costoMatricula": 500.00
    }
  ],
  "timestamp": 1718400000000
}
```

> **Nota:** `objetivosEspecificos` y `lineasInvestigacion` ahora son arrays de strings (`text[]` en BD).

---

## GET /api/v1/programas/{slug}

Obtiene un programa especifico por su slug.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `404` | No existe un programa con ese slug |

---

## POST /api/v1/programas

Crea un nuevo programa.

**Body:**
```json
{
  "nombre": "Ingenieria de Sistemas",
  "enConvocatoria": true,
  "imageUrl": "https://ejemplo.com/imagen.jpg",
  "objetivoGeneral": "Formar profesionales en ingenieria de sistemas",
  "objetivosEspecificos": [
    "Desarrollar competencias en desarrollo de software",
    "Fomentar la investigacion aplicada"
  ],
  "perfilPosgraduado": "Profesional capacitado para liderar proyectos tecnologicos",
  "facultadId": 1,
  "tipoProgramaId": 1,
  "modalidad": "PRESENCIAL",
  "lineasInvestigacion": [
    "Ingenieria de software",
    "Inteligencia artificial"
  ],
  "costoMatricula": 500.00
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripcion |
|-------|------|-------------|-------------|
| `nombre` | `string` | Si | Nombre del programa |
| `enConvocatoria` | `boolean` | No | Indica si tiene convocatoria abierta (default `false`) |
| `imageUrl` | `string` | Si | URL de la imagen del programa |
| `objetivoGeneral` | `string` | Si | Objetivo general |
| `objetivosEspecificos` | `string[]` | Si | Array de objetivos especificos |
| `perfilPosgraduado` | `string` | Si | Perfil del posgraduado |
| `facultadId` | `number` | Si | ID de la facultad |
| `tipoProgramaId` | `number` | Si | ID del tipo de programa |
| `modalidad` | `string` | Si | `PRESENCIAL`, `SEMIPRESENCIAL` o `VIRTUAL` |
| `lineasInvestigacion` | `string[]` | Si | Array de lineas de investigacion |
| `costoMatricula` | `number` | Si | Costo de matricula |

**Nota:** El campo `slug` se genera automaticamente desde el `nombre`.

---

## PUT /api/v1/programas/{slug}

Actualiza un programa existente. Body igual que POST.

**Nota:** El campo `slug` se regenera automaticamente desde el `nombre`.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o invalidos |
| `404` | Programa, `tipoProgramaId` o `facultadId` no existe |
| `409` | Ya existe un programa con el mismo nombre (slug duplicado) |

---

## DELETE /api/v1/programas/{slug}

Elimina un programa por su slug.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `404` | No existe un programa con ese slug |
