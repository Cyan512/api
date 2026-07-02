# Formato de respuesta unificado

Todas las respuestas siguen la misma estructura `ApiResponse<T>`.

## Autenticación

Las peticiones que requieren autenticación deben incluir el header:

```
Authorization: Bearer <accessToken>
```

El `accessToken` y `refreshToken` se obtienen de los endpoints en [Autenticación](endpoints/auth.md).

---

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

---

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

---

## Respuesta de error (401 Unauthorized)

```json
{
  "success": false,
  "message": "Credenciales inválidas",
  "data": null,
  "timestamp": 1718400000000
}
```

Posibles causas:
- Token ausente o malformado en el header `Authorization`
- Access token expirado
- Refresh token inválido, expirado o revocado
- Credenciales incorrectas en login

---

## Respuesta de error (403 Forbidden)

```json
{
  "success": false,
  "message": "Acceso denegado",
  "data": null,
  "timestamp": 1718400000000
}
```

El usuario está autenticado pero no tiene el rol requerido para ese endpoint (ej: acceder a `/api/v1/admin/**` sin rol `ADMIN`).

---

## Respuesta de error (404 Not Found)

```json
{
  "success": false,
  "message": "Programa no encontrado con slug: inventado",
  "data": null,
  "timestamp": 1718400000000
}
```

---

## Respuesta de error (409 Conflict)

```json
{
  "success": false,
  "message": "Ya existe un tipo de programa con el nombre: Pregrado",
  "data": null,
  "timestamp": 1718400000000
}
```

---

## Respuesta de error (500 Internal Server Error)

```json
{
  "success": false,
  "message": "Error interno del servidor",
  "data": null,
  "timestamp": 1718400000000
}
```

---

## Códigos HTTP utilizados

| Código | Descripción |
|--------|-------------|
| `200 OK` | GET exitoso |
| `201 Created` | POST exitoso (recurso creado) |
| `400 Bad Request` | Error de validación en el body |
| `401 Unauthorized` | Token ausente, inválido o credenciales incorrectas |
| `403 Forbidden` | Rol insuficiente para acceder al recurso |
| `404 Not Found` | Recurso no encontrado (FK inválido, slug/ID inexistente) |
| `409 Conflict` | Conflicto (slug/username/email duplicado) |
| `500 Internal Server Error` | Error inesperado del servidor |
