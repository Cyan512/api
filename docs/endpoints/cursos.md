# Cursos

**Base:** `/api/v1/cursos`

---

## GET /api/v1/cursos

Lista todos los cursos.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Cursos obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Matemáticas Básicas",
      "creditos": 4,
      "categoria": "OE"
    },
    {
      "id": 2,
      "nombre": "Programación I",
      "creditos": 4,
      "categoria": "EE"
    }
  ],
  "timestamp": 1718400000000
}
```

---

## POST /api/v1/cursos

Crea un nuevo curso.

**Body:**
```json
{
  "nombre": "Base de Datos",
  "creditos": 3,
  "categoria": "EE"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `nombre` | `string` | ✅ | Nombre del curso |
| `creditos` | `number` | ✅ | Número de créditos (mínimo 1) |
| `categoria` | `string` | ✅ | `OE` (obligatorio específico) o `EE` (electivo específico) |

**Ejemplo de respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Curso creado exitosamente",
  "data": {
    "id": 3,
    "nombre": "Base de Datos",
    "creditos": 3,
    "categoria": "EE"
  },
  "timestamp": 1718400000000
}
```

---

## GET /api/v1/cursos/{id}

Obtiene un curso específico por su ID.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Curso encontrado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Matemáticas Básicas",
    "creditos": 4,
    "categoria": "OE"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un curso con ese ID |

---

## PUT /api/v1/cursos/{id}

Actualiza un curso existente.

**Body:**
```json
{
  "nombre": "Matemáticas Avanzadas",
  "creditos": 5,
  "categoria": "OE"
}
```

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Curso actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Matemáticas Avanzadas",
    "creditos": 5,
    "categoria": "OE"
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o inválidos |
| `404` | No existe un curso con ese ID |

---

## DELETE /api/v1/cursos/{id}

Elimina un curso por su ID.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Curso eliminado exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un curso con ese ID |
