# Detalle de Malla

**Base:** `/api/v1/detalles-malla`

---

## GET /api/v1/detalles-malla

🌐 **Público**

Lista los detalles de malla. Opcionalmente filtrable por programa y/o semestre.

### Parámetros (todos opcionales)

| Parámetro | Tipo | Ejemplo | Descripción |
|-----------|------|---------|-------------|
| `programaId` | `number` | `1` | Filtra por ID de programa |
| `numSemestre` | `number` | `2` | Filtra por número de semestre (requiere `programaId`) |

**Ejemplo de respuesta:**

```json
{
  "success": true,
  "message": "Detalles de malla obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "numSemestre": 1,
      "costoSoles": 500.00,
      "curso": {
        "id": 1,
        "nombre": "Matemáticas Básicas",
        "creditos": 4,
        "categoria": "OE"
      },
      "programa": {
        "id": 1,
        "nombre": "Ingeniería de Sistemas",
        "slug": "ingenieria-de-sistemas"
      }
    }
  ],
  "timestamp": 1718400000000
}
```

> *Nota:* Cuando el detalle corresponde a un espacio electivo, `curso` será `null`.

---

## GET /api/v1/detalles-malla/{id}

🌐 **Público**

Obtiene un detalle de malla por su ID.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un detalle con ese ID |

---

## POST /api/v1/detalles-malla

🔒 **Requiere rol: ADMIN o USER**

Crea un nuevo detalle de malla.

**Body (curso obligatorio):**

```json
{
  "programaId": 1,
  "numSemestre": 1,
  "cursoId": 1,
  "costoSoles": 500.00
}
```

**Body (espacio electivo, sin curso fijo):**

```json
{
  "programaId": 1,
  "numSemestre": 3,
  "costoSoles": 600.00
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `programaId` | `number` | Sí | ID del programa |
| `numSemestre` | `number` | Sí | Número de semestre (≥ 1) |
| `cursoId` | `number` | No | ID del curso. `null` si es espacio electivo. |
| `costoSoles` | `number` | Sí | Costo en soles |

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos faltantes |
| `404` | `programaId` o `cursoId` no existe |

---

## DELETE /api/v1/detalles-malla/{id}

🔒 **Requiere rol: ADMIN o USER**

Elimina un detalle de malla por su ID.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un detalle con ese ID |
