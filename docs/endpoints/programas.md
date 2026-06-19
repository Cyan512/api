# Programas

**Base:** `/api/v1/programas`

---

## GET /api/v1/programas

Lista programas con filtros opcionales.

### Parámetros (todos opcionales)

| Parámetro | Tipo | Ejemplo | Descripción |
|-----------|------|---------|-------------|
| `tipoSlug` | `string` | `maestria` | Filtra por tipo de programa (slug) |
| `q` | `string` | `sistemas` | Búsqueda textual por nombre |
| `modalidad` | `string` | `VIRTUAL` | Filtra por modalidad (`PRESENCIAL`, `SEMIPRESENCIAL`, `VIRTUAL`) |
| `idFacultad` | `number` | `1` | Filtra por ID de facultad |
| `convocatoria` | `boolean` | `true` | Filtra por convocatoria abierta/cerrada |

**Nota:** El orden de los parámetros no importa.

### Ejemplos de uso

```
GET /api/v1/programas
GET /api/v1/programas?tipoSlug=maestria
GET /api/v1/programas?tipoSlug=maestria&q=sistemas
GET /api/v1/programas?modalidad=VIRTUAL
GET /api/v1/programas?convocatoria=true
GET /api/v1/programas?tipoSlug=maestria&modalidad=VIRTUAL&convocatoria=true
GET /api/v1/programas?tipoSlug=pregrado&q=inge&idFacultad=1
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Programas obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "idTipoPrograma": {
        "id": 1,
        "nombre": "Pregrado",
        "imagenCard": null,
        "imagenBg": null,
        "slug": "pregrado"
      },
      "nombre": "Ingeniería de Sistemas",
      "idFacultad": {
        "id": 1,
        "nombre": "Ingeniería"
      },
      "slug": "ingenieria-de-sistemas",
      "convocatoria": true,
      "modalidad": "PRESENCIAL",
      "imagen": "https://ejemplo.com/imagen.jpg",
      "objetivoGeneral": "Formar profesionales en ingeniería de sistemas",
      "objetivosEspecificos": "Desarrollar competencias en desarrollo de software",
      "perfilPosgraduado": "Profesional capacitado para liderar proyectos tecnológicos",
      "lineasInvestigacion": "Ingeniería de software, Inteligencia artificial"
    }
  ],
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/programas/{slug}

Obtiene un programa específico por su slug.

**Parámetros:**

| Parámetro | Tipo | Ejemplo | Descripción |
|-----------|------|---------|-------------|
| `slug` | `string` | `ingenieria-de-sistemas` | Slug único del programa |

**Respuesta:**
```json
{
  "success": true,
  "message": "Programa encontrado exitosamente",
  "data": {
    "id": 1,
    "idTipoPrograma": {
      "id": 1,
      "nombre": "Pregrado",
      "imagenCard": null,
      "imagenBg": null,
      "slug": "pregrado"
    },
    "nombre": "Ingeniería de Sistemas",
    "idFacultad": {
      "id": 1,
      "nombre": "Ingeniería"
    },
    "slug": "ingenieria-de-sistemas",
    "convocatoria": true,
    "modalidad": "PRESENCIAL",
    "imagen": "https://ejemplo.com/imagen.jpg",
    "objetivoGeneral": "Formar profesionales en ingeniería de sistemas",
    "objetivosEspecificos": "Desarrollar competencias en desarrollo de software",
    "perfilPosgraduado": "Profesional capacitado para liderar proyectos tecnológicos",
    "lineasInvestigacion": "Ingeniería de software, Inteligencia artificial"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un programa con ese slug |

---

## POST /api/v1/programas

Crea un nuevo programa.

**Body:**
```json
{
  "nombre": "Ingeniería de Sistemas",
  "convocatoria": true,
  "imagen": "https://ejemplo.com/imagen.jpg",
  "objetivoGeneral": "Formar profesionales en ingeniería de sistemas",
  "objetivosEspecificos": "Desarrollar competencias en desarrollo de software",
  "perfilPosgraduado": "Profesional capacitado para liderar proyectos tecnológicos",
  "idFacultad": 1,
  "idTipoPrograma": 1,
  "modalidad": "PRESENCIAL",
  "lineasInvestigacion": "Ingeniería de software, Inteligencia artificial"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | `string` | ✅ | Nombre del programa |
| `convocatoria` | `boolean` | ❌ | Indica si tiene convocatoria abierta |
| `imagen` | `string` | ❌ | URL de la imagen del programa |
| `objetivoGeneral` | `string` | ✅ | Objetivo general del programa |
| `objetivosEspecificos` | `string` | ✅ | Objetivos específicos del programa |
| `perfilPosgraduado` | `string` | ✅ | Perfil del posgraduado |
| `idFacultad` | `number` | ✅ | ID de la facultad |
| `idTipoPrograma` | `number` | ✅ | ID del tipo de programa |
| `modalidad` | `string` | ✅ | `PRESENCIAL`, `SEMIPRESENCIAL` o `VIRTUAL` |
| `lineasInvestigacion` | `string` | ✅ | Líneas de investigación |

**Nota:** El campo `slug` se genera automáticamente desde el `nombre`.

**Respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Programa creado exitosamente",
  "data": {
    "id": 1,
    "idTipoPrograma": {
      "id": 1,
      "nombre": "Pregrado",
      "imagenCard": null,
      "imagenBg": null,
      "slug": "pregrado"
    },
    "nombre": "Ingeniería de Sistemas",
    "idFacultad": {
      "id": 1,
      "nombre": "Ingeniería"
    },
    "slug": "ingenieria-de-sistemas",
    "convocatoria": true,
    "modalidad": "PRESENCIAL",
    "imagen": "https://ejemplo.com/imagen.jpg",
    "objetivoGeneral": "Formar profesionales en ingeniería de sistemas",
    "objetivosEspecificos": "Desarrollar competencias en desarrollo de software",
    "perfilPosgraduado": "Profesional capacitado para liderar proyectos tecnológicos",
    "lineasInvestigacion": "Ingeniería de software, Inteligencia artificial"
  },
  "timestamp": 1718400000000
}
```

---

## PUT /api/v1/programas/{slug}

Actualiza un programa existente.

**Body:**
```json
{
  "nombre": "Ingeniería de Sistemas Actualizada",
  "convocatoria": false,
  "imagen": "https://ejemplo.com/nueva-imagen.jpg",
  "objetivoGeneral": "Formar profesionales actualizados en ingeniería",
  "objetivosEspecificos": "Desarrollar competencias avanzadas",
  "perfilPosgraduado": "Profesional con perfil actualizado",
  "idFacultad": 1,
  "idTipoPrograma": 1,
  "modalidad": "VIRTUAL",
  "lineasInvestigacion": "Nuevas líneas de investigación"
}
```

**Nota:** El campo `slug` se regenera automáticamente desde el `nombre`.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Programa actualizado exitosamente",
  "data": {
    "id": 1,
    "idTipoPrograma": {
      "id": 1,
      "nombre": "Pregrado",
      "slug": "pregrado"
    },
    "nombre": "Ingeniería de Sistemas Actualizada",
    "idFacultad": {
      "id": 1,
      "nombre": "Ingeniería"
    },
    "slug": "ingenieria-de-sistemas-actualizada",
    "convocatoria": false,
    "modalidad": "VIRTUAL",
    "imagen": "https://ejemplo.com/nueva-imagen.jpg",
    "objetivoGeneral": "Formar profesionales actualizados en ingeniería",
    "objetivosEspecificos": "Desarrollar competencias avanzadas",
    "perfilPosgraduado": "Profesional con perfil actualizado",
    "lineasInvestigacion": "Nuevas líneas de investigación"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o inválidos |
| `404` | Programa, `idTipoPrograma` o `idFacultad` no existe |
| `409` | Ya existe un programa con el mismo nombre (slug duplicado) |

---

## DELETE /api/v1/programas/{slug}

Elimina un programa por su slug.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Programa eliminado exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un programa con ese slug |
