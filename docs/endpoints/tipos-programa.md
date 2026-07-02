# Tipos de Programa

**Base:** `/api/v1/tipos-programa`

---

## GET /api/v1/tipos-programa

🌐 **Público**

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
      "cardImageUrl": "pregrado-card.jpg",
      "heroBgUrl": "pregrado-bg.jpg",
      "slug": "pregrado"
    },
    {
      "id": 2,
      "nombre": "Maestria",
      "cardImageUrl": "maestria-card.jpg",
      "heroBgUrl": "maestria-bg.jpg",
      "slug": "maestria"
    }
  ],
  "timestamp": 1718400000000
}
```

---

## POST /api/v1/tipos-programa

🔒 **Requiere rol: ADMIN o USER**

Crea un nuevo tipo de programa.

**Body:**

```json
{
  "nombre": "Diplomado",
  "cardImageUrl": "diplomado-card.jpg",
  "heroBgUrl": "diplomado-bg.jpg"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | `string` | Sí | Nombre del tipo de programa |
| `cardImageUrl` | `string` | Sí | URL de imagen para tarjeta |
| `heroBgUrl` | `string` | Sí | URL de imagen de fondo |

**Nota:** El campo `slug` se genera automáticamente desde el `nombre`.

**Respuesta (201 Created):**

```json
{
  "success": true,
  "message": "Tipo de programa creado exitosamente",
  "data": {
    "id": 3,
    "nombre": "Diplomado",
    "cardImageUrl": "diplomado-card.jpg",
    "heroBgUrl": "diplomado-bg.jpg",
    "slug": "diplomado"
  },
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/tipos-programa/{slug}

🌐 **Público**

Obtiene un tipo de programa específico por su slug.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un tipo de programa con ese slug |

---

## PUT /api/v1/tipos-programa/{slug}

🔒 **Requiere rol: ADMIN o USER**

Actualiza un tipo de programa existente.

**Body:** Igual que POST.

**Nota:** El campo `slug` se regenera automáticamente desde el `nombre`.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | `nombre`, `cardImageUrl` o `heroBgUrl` vacíos |
| `404` | No existe un tipo de programa con ese slug |
| `409` | El slug generado ya existe (mismo nombre) |

---

## DELETE /api/v1/tipos-programa/{slug}

🔒 **Requiere rol: ADMIN o USER**

Elimina un tipo de programa por su slug.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un tipo de programa con ese slug |
