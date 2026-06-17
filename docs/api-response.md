# Formato de respuesta unificado

Todas las respuestas siguen la misma estructura `ApiResponse<T>`.

## Respuesta exitosa (GET / POST)

```json
{
  "success": true,
  "message": "Programas obtenidos exitosamente",
  "data": [ ... ],
  "timestamp": 1718400000000
}
```

- `success` (`boolean`): `true` si la operación fue exitosa
- `message` (`string`): mensaje descriptivo del resultado
- `data` (`T`): el payload de la respuesta (puede ser un objeto, arreglo o `null`)
- `timestamp` (`number`): fecha/hora actual en milisegundos Unix

## Respuesta de error (400 Bad Request — validación)

```json
{
  "success": false,
  "message": "Error de validación",
  "data": {
    "nombre": "El nombre es obligatorio",
    "modalidad": "La modalidad es obligatoria"
  },
  "timestamp": 1718400000000
}
```

- `data` contiene un objeto con los campos que fallaron y su mensaje de error

## Respuesta de error (404 Not Found)

```json
{
  "success": false,
  "message": "Programa no encontrado con slug: inventado",
  "data": null,
  "timestamp": 1718400000000
}
```

## Respuesta de error (409 Conflict)

```json
{
  "success": false,
  "message": "Ya existe un tipo de programa con el nombre: Pregrado",
  "data": null,
  "timestamp": 1718400000000
}
```

## Respuesta de error (500 Internal Server Error)

```json
{
  "success": false,
  "message": "Error interno del servidor",
  "data": null,
  "timestamp": 1718400000000
}
```

## Códigos HTTP utilizados

| Código | Descripción |
|--------|-------------|
| `200 OK` | GET exitoso |
| `201 Created` | POST exitoso (recurso creado) |
| `400 Bad Request` | Error de validación en el body |
| `404 Not Found` | Recurso no encontrado (FK inválido, slug inexistente) |
| `409 Conflict` | Conflicto (slug duplicado) |
| `500 Internal Server Error` | Error inesperado del servidor |
