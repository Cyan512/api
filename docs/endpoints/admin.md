# Admin / Usuarios

**Base:** `/api/v1/admin/users`

Todos los endpoints de esta sección requieren rol **`ADMIN`**.

---

## GET /api/v1/admin/users

🔒 **Requiere rol: ADMIN**

Lista todos los usuarios registrados.

**Respuesta:**

```json
{
  "success": true,
  "message": "Usuarios obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@ejemplo.com",
      "enabled": true,
      "createdAt": "2026-06-19T10:00:00",
      "roles": [
        { "id": 1, "name": "ROLE_ADMIN" },
        { "id": 2, "name": "ROLE_USER" }
      ]
    },
    {
      "id": 2,
      "username": "jdoe",
      "email": "jdoe@ejemplo.com",
      "enabled": true,
      "createdAt": "2026-06-20T15:30:00",
      "roles": [
        { "id": 2, "name": "ROLE_USER" }
      ]
    }
  ],
  "timestamp": 1718400000000
}
```

> **Nota:** El campo `passwordHash` nunca se expone en las respuestas.

---

## GET /api/v1/admin/users/{id}

🔒 **Requiere rol: ADMIN**

Obtiene un usuario específico por su ID.

**Respuesta:**

```json
{
  "success": true,
  "message": "Usuario encontrado exitosamente",
  "data": {
    "id": 2,
    "username": "jdoe",
    "email": "jdoe@ejemplo.com",
    "enabled": true,
    "createdAt": "2026-06-20T15:30:00",
    "roles": [
      { "id": 2, "name": "ROLE_USER" }
    ]
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un usuario con ese ID |

---

## PUT /api/v1/admin/users/{id}/roles

🔒 **Requiere rol: ADMIN**

Actualiza los roles de un usuario.

**Body:**

```json
{
  "roles": ["ADMIN", "USER"]
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `roles` | `string[]` | ✅ | Lista de roles (al menos uno). Se normalizan automáticamente con prefijo `ROLE_`. |

**Respuesta:**

```json
{
  "success": true,
  "message": "Roles actualizados exitosamente",
  "data": {
    "id": 2,
    "username": "jdoe",
    "email": "jdoe@ejemplo.com",
    "enabled": true,
    "createdAt": "2026-06-20T15:30:00",
    "roles": [
      { "id": 1, "name": "ROLE_ADMIN" },
      { "id": 2, "name": "ROLE_USER" }
    ]
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | `roles` vacío o no enviado |
| `404` | No existe un usuario con ese ID |

---

## DELETE /api/v1/admin/users/{id}

🔒 **Requiere rol: ADMIN**

Elimina un usuario por su ID.

**Respuesta:**

```json
{
  "success": true,
  "message": "Usuario eliminado exitosamente",
  "data": null,
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `404` | No existe un usuario con ese ID |
