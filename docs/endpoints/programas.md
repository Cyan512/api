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
      "modalidad": "PRESENCIAL"
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
    "modalidad": "PRESENCIAL"
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
  "idTipoPrograma": 1,
  "nombre": "Ingeniería de Sistemas",
  "idFacultad": 1,
  "convocatoria": true,
  "modalidad": "PRESENCIAL"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `idTipoPrograma` | `number` | ✅ | ID del tipo de programa |
| `nombre` | `string` | ✅ | Nombre del programa |
| `idFacultad` | `number` | ✅ | ID de la facultad |
| `convocatoria` | `boolean` | ❌ | Indica si tiene convocatoria abierta |
| `modalidad` | `string` | ✅ | `PRESENCIAL`, `SEMIPRESENCIAL` o `VIRTUAL` |

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
    "modalidad": "PRESENCIAL"
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
  "idTipoPrograma": 1,
  "nombre": "Ingeniería de Sistemas Actualizada",
  "idFacultad": 1,
  "convocatoria": false,
  "modalidad": "VIRTUAL"
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
    "modalidad": "VIRTUAL"
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
