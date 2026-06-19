# Comunicados

**Base:** `/api/v1/comunicados`

---

## GET /api/v1/comunicados

Lista todos los comunicados.

### Ejemplos de uso

```
GET /api/v1/comunicados
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Comunicados obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "titulo": "Nuevo comunicado",
      "slug": "nuevo-comunicado",
      "resumen": "Resumen del comunicado",
      "contenido": "Contenido completo del comunicado",
      "imagen": "https://ejemplo.com/imagen.jpg",
      "fechaPublicacion": "2026-06-19T10:00:00"
    }
  ],
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/comunicados/{slug}

Obtiene un comunicado específico por su slug.

**Parámetros:**

| Parámetro | Tipo | Ejemplo | Descripción |
|-----------|------|---------|-------------|
| `slug` | `string` | `nuevo-comunicado` | Slug único del comunicado |

**Respuesta:**
```json
{
  "success": true,
  "message": "Comunicado encontrado exitosamente",
  "data": {
    "id": 1,
    "titulo": "Nuevo comunicado",
    "slug": "nuevo-comunicado",
    "resumen": "Resumen del comunicado",
    "contenido": "Contenido completo del comunicado",
    "imagen": "https://ejemplo.com/imagen.jpg",
    "fechaPublicacion": "2026-06-19T10:00:00"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un comunicado con ese slug |

---

## POST /api/v1/comunicados

Crea un nuevo comunicado.

**Body:**
```json
{
  "titulo": "Nuevo comunicado",
  "resumen": "Resumen del comunicado",
  "contenido": "Contenido completo del comunicado",
  "imagen": "https://ejemplo.com/imagen.jpg"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `titulo` | `string` | ✅ | Título del comunicado |
| `resumen` | `string` | ✅ | Resumen del comunicado |
| `contenido` | `string` | ✅ | Contenido completo del comunicado |
| `imagen` | `string` | ❌ | URL de la imagen del comunicado |

**Nota:** El campo `slug` se genera automáticamente desde el `titulo` y `fechaPublicacion` se asigna automáticamente con la fecha actual.

**Respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Comunicado creado exitosamente",
  "data": {
    "id": 1,
    "titulo": "Nuevo comunicado",
    "slug": "nuevo-comunicado",
    "resumen": "Resumen del comunicado",
    "contenido": "Contenido completo del comunicado",
    "imagen": "https://ejemplo.com/imagen.jpg",
    "fechaPublicacion": "2026-06-19T10:00:00"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o inválidos |
| `409` | Ya existe un comunicado con el mismo título (slug duplicado) |

---

## PUT /api/v1/comunicados/{slug}

Actualiza un comunicado existente.

**Body:**
```json
{
  "titulo": "Comunicado actualizado",
  "resumen": "Resumen actualizado",
  "contenido": "Contenido actualizado",
  "imagen": "https://ejemplo.com/nueva-imagen.jpg"
}
```

**Nota:** El campo `slug` se regenera automáticamente desde el `titulo`. La `fechaPublicacion` se mantiene de la creación original.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Comunicado actualizado exitosamente",
  "data": {
    "id": 1,
    "titulo": "Comunicado actualizado",
    "slug": "comunicado-actualizado",
    "resumen": "Resumen actualizado",
    "contenido": "Contenido actualizado",
    "imagen": "https://ejemplo.com/nueva-imagen.jpg",
    "fechaPublicacion": "2026-06-19T10:00:00"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o inválidos |
| `404` | No existe un comunicado con ese slug |
| `409` | Ya existe un comunicado con el mismo título (slug duplicado) |

---

## DELETE /api/v1/comunicados/{slug}

Elimina un comunicado por su slug.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Comunicado eliminado exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un comunicado con ese slug |
