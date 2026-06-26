# Opciones Electivas

**Base:** `/api/v1/opciones-electivas`

---

## GET /api/v1/opciones-electivas

Lista opciones electivas. Opcionalmente filtrable por programa.

### Parametros (todos opcionales)

| Parametro | Tipo | Ejemplo | Descripcion |
|-----------|------|---------|-------------|
| `programaId` | `number` | `1` | Filtra por ID de programa |

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Opciones electivas obtenidas exitosamente",
  "data": [
    {
      "id": 1,
      "programa": {
        "id": 1,
        "nombre": "Ingenieria de Sistemas",
        "slug": "ingenieria-de-sistemas"
      },
      "curso": {
        "id": 5,
        "nombre": "Machine Learning",
        "creditos": 3,
        "categoria": "EE"
      }
    }
  ],
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/opciones-electivas/{id}

Obtiene una opcion electiva por su ID.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `404` | No existe una opcion con ese ID |

---

## POST /api/v1/opciones-electivas

Crea una nueva opcion electiva.

**Body:**
```json
{
  "programaId": 1,
  "cursoId": 5
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripcion |
|-------|------|-------------|-------------|
| `programaId` | `number` | Si | ID del programa |
| `cursoId` | `number` | Si | ID del curso electivo |

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `400` | Campos faltantes |
| `404` | `programaId` o `cursoId` no existe |

---

## DELETE /api/v1/opciones-electivas/{id}

Elimina una opcion electiva por su ID.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `404` | No existe una opcion con ese ID |
