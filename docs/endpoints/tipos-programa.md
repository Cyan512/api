# Tipos de Programa

**Base:** `/api/v1/tipos-programa`

---

## GET /api/v1/tipos-programa

Lista todos los tipos de programa.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Tipos de programa obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Pregrado",
      "imagenCard": "pregrado-card.jpg",
      "imagenBg": "pregrado-bg.jpg",
      "slug": "pregrado"
    },
    {
      "id": 2,
      "nombre": "Maestría",
      "imagenCard": null,
      "imagenBg": null,
      "slug": "maestria"
    }
  ],
  "timestamp": 1718400000000
}
```

**Respuesta vacía:**
```json
{
  "success": true,
  "message": "Tipos de programa obtenidos exitosamente",
  "data": [],
  "timestamp": 1718400000000
}
```

---

## POST /api/v1/tipos-programa

Crea un nuevo tipo de programa.

**Body:**
```json
{
  "nombre": "Diplomado",
  "imagenCard": "diplomado-card.jpg",
  "imagenBg": "diplomado-bg.jpg"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | `string` | ✅ | Nombre del tipo de programa |
| `imagenCard` | `string` | ❌ | URL de imagen para tarjeta |
| `imagenBg` | `string` | ❌ | URL de imagen de fondo |

**Nota:** El campo `slug` se genera automáticamente desde el `nombre`.

**Ejemplo de respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Tipo de programa creado exitosamente",
  "data": {
    "id": 3,
    "nombre": "Diplomado",
    "imagenCard": "diplomado-card.jpg",
    "imagenBg": "diplomado-bg.jpg",
    "slug": "diplomado"
  },
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/tipos-programa/{slug}

Obtiene un tipo de programa específico por su slug.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Tipo de programa encontrado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Pregrado",
    "imagenCard": "pregrado-card.jpg",
    "imagenBg": "pregrado-bg.jpg",
    "slug": "pregrado"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un tipo de programa con ese slug |

---

## PUT /api/v1/tipos-programa/{slug}

Actualiza un tipo de programa existente.

**Body:**
```json
{
  "nombre": "Diplomado",
  "imagenCard": "diplomado-card.jpg",
  "imagenBg": "diplomado-bg.jpg"
}
```

**Nota:** El campo `slug` se regenera automáticamente desde el `nombre`.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Tipo de programa actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Diplomado",
    "imagenCard": "diplomado-card.jpg",
    "imagenBg": "diplomado-bg.jpg",
    "slug": "diplomado"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | `nombre` vacío o no enviado |
| `404` | No existe un tipo de programa con ese slug |
| `409` | El slug generado ya existe (mismo nombre) |

---

## DELETE /api/v1/tipos-programa/{slug}

Elimina un tipo de programa por su slug.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Tipo de programa eliminado exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un tipo de programa con ese slug |
