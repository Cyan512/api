# Facultades

**Base:** `/api/v1/facultades`

---

## GET /api/v1/facultades

🌐 **Público**

Lista todas las facultades.

**Ejemplo de respuesta:**

```json
{
  "success": true,
  "message": "Facultades obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Ingeniería"
    },
    {
      "id": 2,
      "nombre": "Ciencias de la Salud"
    }
  ],
  "timestamp": 1718400000000
}
```

---

## POST /api/v1/facultades

🔒 **Requiere rol: ADMIN o USER**

Crea una nueva facultad.

**Body:**

```json
{
  "nombre": "Arquitectura"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | `string` | ✅ | Nombre de la facultad |

**Ejemplo de respuesta (201 Created):**

```json
{
  "success": true,
  "message": "Facultad creada exitosamente",
  "data": {
    "id": 3,
    "nombre": "Arquitectura"
  },
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/facultades/{id}

🌐 **Público**

Obtiene una facultad específica por su ID.

**Ejemplo de respuesta:**

```json
{
  "success": true,
  "message": "Facultad encontrada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Ingeniería"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe una facultad con ese ID |

---

## PUT /api/v1/facultades/{id}

🔒 **Requiere rol: ADMIN o USER**

Actualiza una facultad existente.

**Body:**

```json
{
  "nombre": "Arquitectura y Urbanismo"
}
```

**Ejemplo de respuesta:**

```json
{
  "success": true,
  "message": "Facultad actualizada exitosamente",
  "data": {
    "id": 1,
    "nombre": "Arquitectura y Urbanismo"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | `nombre` vacío o no enviado |
| `404` | No existe una facultad con ese ID |

---

## DELETE /api/v1/facultades/{id}

🔒 **Requiere rol: ADMIN o USER**

Elimina una facultad por su ID.

**Ejemplo de respuesta:**

```json
{
  "success": true,
  "message": "Facultad eliminada exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe una facultad con ese ID |
