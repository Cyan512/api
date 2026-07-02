# Autenticación

**Base:** `/api/v1/auth`

Todos los endpoints de esta sección son **públicos**.

---

## POST /api/v1/auth/register

🌐 **Público**

Registra un nuevo usuario.

**Body:**

```json
{
  "username": "jdoe",
  "email": "jdoe@ejemplo.com",
  "password": "secreto123",
  "roles": ["USER"]
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `username` | `string` | ✅ | Nombre de usuario (3 a 50 caracteres, único) |
| `email` | `string` | ✅ | Correo electrónico válido (máx. 120 caracteres, único) |
| `password` | `string` | ✅ | Contraseña (mín. 6 caracteres) |
| `roles` | `string[]` | ❌ | Roles a asignar. Si no se envía, se asigna `ROLE_USER` por defecto. |

**Respuesta (200 OK):**

```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "accessToken": "eyJhbGciOi...",
    "refreshToken": "dGhpcyBpcyBh...",
    "tokenType": "Bearer",
    "expiresIn": 1800000,
    "username": "jdoe",
    "roles": ["ROLE_USER"]
  },
  "timestamp": 1718400000000
}
```

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes o inválidos |
| `409` | `username` o `email` ya está en uso |

---

## POST /api/v1/auth/login

🌐 **Público**

Inicia sesión con credenciales existentes.

**Body:**

```json
{
  "username": "jdoe",
  "password": "secreto123"
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `username` | `string` | ✅ | Nombre de usuario |
| `password` | `string` | ✅ | Contraseña |

**Respuesta (200 OK):**

```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "accessToken": "eyJhbGciOi...",
    "refreshToken": "dGhpcyBpcyBh...",
    "tokenType": "Bearer",
    "expiresIn": 1800000,
    "username": "jdoe",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  },
  "timestamp": 1718400000000
}
```

> **Nota:** Cada login revoca todos los refresh tokens anteriores del usuario y genera uno nuevo.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | Campos obligatorios faltantes |
| `401` | Credenciales inválidas (username o password incorrectos) |

---

## POST /api/v1/auth/refresh

🌐 **Público**

Renueva un access token expirado usando el refresh token.

**Body:**

```json
{
  "refreshToken": "dGhpcyBpcyBh..."
}
```

**Campos:**

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `refreshToken` | `string` | ✅ | Refresh token vigente |

**Respuesta (200 OK):**

```json
{
  "success": true,
  "message": "Token renovado exitosamente",
  "data": {
    "accessToken": "eyJhbGciOi...",
    "refreshToken": "bmV3IHJlZnJl...",
    "tokenType": "Bearer",
    "expiresIn": 1800000,
    "username": "jdoe",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  },
  "timestamp": 1718400000000
}
```

> **Nota:** Se rota el refresh token: el anterior se revoca y se emite uno nuevo.

**Posibles errores:**

| Código | Causa |
|--------|-------|
| `400` | `refreshToken` faltante |
| `401` | Refresh token inválido, expirado o revocado |

---

## TokenResponse

Estructura devuelta por `register`, `login` y `refresh`:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `accessToken` | `string` | JWT para autorizar peticiones. Expira en 30 minutos. |
| `refreshToken` | `string` | Token para renovar el access token sin re-login. |
| `tokenType` | `string` | Siempre `"Bearer"`. |
| `expiresIn` | `number` | Tiempo de vida del access token en **milisegundos** (1800000 = 30 min). |
| `username` | `string` | Nombre del usuario autenticado. |
| `roles` | `string[]` | Lista de roles asignados (ej: `["ROLE_USER"]`, `["ROLE_ADMIN", "ROLE_USER"]`). |
