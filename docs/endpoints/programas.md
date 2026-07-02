# Programas

**Base:** `/api/v1/programas`

---

## GET /api/v1/programas

🌐 **Público**

Lista programas con filtros opcionales.

### Parámetros (todos opcionales)

| Parámetro | Tipo | Ejemplo | Descripción |
|-----------|------|---------|-------------|
| `tipoSlug` | `string` | `maestria` | Filtra por tipo de programa (slug) |
| `q` | `string` | `sistemas` | Búsqueda textual por nombre |
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

> **Nota:** `objetivosEspecificos` y `lineasInvestigacion` son arrays de strings (`text[]` en BD).

---

## GET /api/v1/programas/{slug}

🌐 **Público**

Obtiene un programa específico por su slug.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un programa con ese slug |

---

## POST /api/v1/programas

🔒 **Requiere rol: ADMIN o USER**

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

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | `string` | Sí | Nombre del programa |
| `enConvocatoria` | `boolean` | No | Indica si tiene convocatoria abierta (default `false`) |
| `imageUrl` | `string` | Sí | URL de la imagen del programa |
| `objetivoGeneral` | `string` | Sí | Objetivo general |
| `objetivosEspecificos` | `string[]` | Sí | Array de objetivos específicos |
| `perfilPosgraduado` | `string` | Sí | Perfil del posgraduado |
| `facultadId` | `number` | Sí | ID de la facultad |
| `tipoProgramaId` | `number` | Sí | ID del tipo de programa |
| `modalidad` | `string` | Sí | `PRESENCIAL`, `SEMIPRESENCIAL` o `VIRTUAL` |
| `lineasInvestigacion` | `string[]` | Sí | Array de líneas de investigación |
| `costoMatricula` | `number` | Sí | Costo de matrícula |

**Nota:** El campo `slug` se genera automáticamente desde el `nombre`.

---

## PUT /api/v1/programas/{slug}

🔒 **Requiere rol: ADMIN o USER**

Actualiza un programa existente. Body igual que POST.

**Nota:** El campo `slug` se regenera automáticamente desde el `nombre`.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o inválidos |
| `404` | Programa, `tipoProgramaId` o `facultadId` no existe |
| `409` | Ya existe un programa con el mismo nombre (slug duplicado) |

---

## DELETE /api/v1/programas/{slug}

🔒 **Requiere rol: ADMIN o USER**

Elimina un programa por su slug.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un programa con ese slug |
