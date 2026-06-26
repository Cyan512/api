# Detalle de Malla

**Base:** `/api/v1/detalles-malla`

---

## GET /api/v1/detalles-malla

Lista los detalles de malla. Opcionalmente filtrable por programa y/o semestre.

### Parametros (todos opcionales)

| Parametro | Tipo | Ejemplo | Descripcion |
|-----------|------|---------|-------------|
| `programaId` | `number` | `1` | Filtra por ID de programa |
| `numSemestre` | `number` | `2` | Filtra por numero de semestre (requiere `programaId`) |

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Detalles de malla obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "programa": {
        "id": 1,
        "nombre": "Ingenieria de Sistemas",
        "slug": "ingenieria-de-sistemas"
      },
      "numSemestre": 1,
      "orden": 1,
      "esEspacioElectivo": false,
      "curso": {
        "id": 1,
        "nombre": "Matematicas Basicas",
        "creditos": 4,
        "categoria": "OE"
      },
      "costoSoles": 500.00
    }
  ],
  "timestamp": 1718400000000
}
```

> *Nota:* Cuando `esEspacioElectivo` es `true`, `curso` sera `null`.

---

## GET /api/v1/detalles-malla/{id}

Obtiene un detalle de malla por su ID.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `404` | No existe un detalle con ese ID |

---

## POST /api/v1/detalles-malla

Crea un nuevo detalle de malla.

**Body (curso obligatorio):**
```json
{
  "programaId": 1,
  "numSemestre": 1,
  "orden": 1,
  "esEspacioElectivo": false,
  "cursoId": 1,
  "costoSoles": 500.00
}
```

**Body (espacio electivo, sin curso fijo):**
```json
{
  "programaId": 1,
  "numSemestre": 3,
  "orden": 1,
  "esEspacioElectivo": true,
  "costoSoles": 600.00
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripcion |
|-------|------|-------------|-------------|
| `programaId` | `number` | Si | ID del programa |
| `numSemestre` | `number` | Si | Numero de semestre (>=1) |
| `orden` | `number` | Si | Orden dentro del semestre |
| `esEspacioElectivo` | `boolean` | Si | `true` si es electivo, `false` si es obligatorio |
| `cursoId` | `number` | Condicional | Obligatorio si `esEspacioElectivo` es `false` |
| `costoSoles` | `number` | Si | Costo en soles |

**Reglas de validacion:**
- Si `esEspacioElectivo = false` → `cursoId` obligatorio.
- Si `esEspacioElectivo = true` → `cursoId` **no** debe enviarse.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `400` | Campos faltantes o regla electivo/curso rota |
| `404` | `programaId` o `cursoId` no existe |

---

## DELETE /api/v1/detalles-malla/{id}

Elimina un detalle de malla por su ID.

**Posibles errores:**

| Codigo | Causa |
|--------|-------|
| `404` | No existe un detalle con ese ID |
