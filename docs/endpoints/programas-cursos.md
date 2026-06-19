# Programa-Curso (Asociación)

**Base:** `/api/v1/programas-cursos`

---

## GET /api/v1/programas-cursos

Lista todas las asociaciones programa-curso. Opcionalmente puede filtrarse por programa o curso.

### Parámetros (todos opcionales)

| Parámetro | Tipo | Ejemplo | Descripción |
|-----------|------|---------|-------------|
| `programaId` | `number` | `1` | Filtra por ID de programa |
| `cursoId` | `number` | `1` | Filtra por ID de curso |

**Nota:** No se pueden usar `programaId` y `cursoId` simultáneamente.

### Ejemplos de uso

```
GET /api/v1/programas-cursos
GET /api/v1/programas-cursos?programaId=1
GET /api/v1/programas-cursos?cursoId=1
```

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Programas-curso obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "idPrograma": {
        "id": 1,
        "nombre": "Ingeniería de Sistemas",
        "slug": "ingenieria-de-sistemas"
      },
      "idCurso": {
        "id": 1,
        "nombre": "Matemáticas Básicas",
        "creditos": 4,
        "categoria": "OE"
      },
      "semestre": 1,
      "electivo": false
    }
  ],
  "timestamp": 1718400000000
}
```

---

## POST /api/v1/programas-cursos

Crea una nueva asociación programa-curso.

**Body:**
```json
{
  "idPrograma": 1,
  "idCurso": 1,
  "costoCuota": 500,
  "semestre": 1,
  "electivo": false
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `idPrograma` | `number` | ✅ | ID del programa |
| `idCurso` | `number` | ✅ | ID del curso |
| `costoCuota` | `number` | ✅ | Costo de la cuota del curso |
| `semestre` | `number` | ❌ | Semestre en que se dicta (omitir si es electivo) |
| `electivo` | `boolean` | ❌ | `true` si es electivo, `false` si es obligatorio (por defecto `false`) |

**Respuesta (201 Created):**
```json
{
  "success": true,
  "message": "Programa-curso creado exitosamente",
  "data": {
    "id": 1,
    "idPrograma": { ... },
    "idCurso": { ... },
    "semestre": 1,
    "electivo": false
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes |
| `404` | `idPrograma` o `idCurso` no existe |

---

## GET /api/v1/programas-cursos/{id}

Obtiene una asociación programa-curso específica por su ID.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Asociación programa-curso encontrada exitosamente",
  "data": {
    "id": 1,
    "idPrograma": {
      "id": 1,
      "nombre": "Ingeniería de Sistemas",
      "slug": "ingenieria-de-sistemas"
    },
    "idCurso": {
      "id": 1,
      "nombre": "Matemáticas Básicas",
      "creditos": 4,
      "categoria": "OE"
    },
    "semestre": 1,
    "electivo": false
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe una asociación con ese ID |

---

## DELETE /api/v1/programas-cursos/{id}

Elimina una asociación programa-curso por su ID.

**Ejemplo de respuesta:**
```json
{
  "success": true,
  "message": "Asociación programa-curso eliminada exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe una asociación con ese ID |
