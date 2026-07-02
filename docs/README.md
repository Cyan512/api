# API Documentación

API REST del sistema de gestión de programas académicos.

**Base URL:** `https://api.criseral.com/api/v1`

---

## Índice

1. [Formato de respuesta](api-response.md)
2. [Entidades y relaciones](entities.md)
3. [Enumeraciones](enums.md)
4. Endpoints:
   - [Autenticación](endpoints/auth.md)
   - [Admin / Usuarios](endpoints/admin.md)
   - [Tipos de Programa](endpoints/tipos-programa.md)
   - [Facultades](endpoints/facultades.md)
   - [Cursos](endpoints/cursos.md)
   - [Programas](endpoints/programas.md)
   - [Detalles de Malla](endpoints/detalles-malla.md)
   - [Comunicados](endpoints/comunicados.md)

---

## Autenticación

La API usa **JWT** (JSON Web Tokens) con par access/refresh.

1. Registrarse o loguearse en `/api/v1/auth` para obtener `accessToken` + `refreshToken`.
2. Incluir el header `Authorization: Bearer <accessToken>` en peticiones que requieran autenticación.
3. Cuando el access token expire (30 min), usar `/api/v1/auth/refresh` con el `refreshToken` para obtener uno nuevo.

---

## Roles y permisos

| Grupo de endpoints | Método HTTP | Acceso |
|---|---|---|
| `/api/v1/auth/**` | `POST` | Público |
| `/api/v1/**` | `GET` | Público |
| `/api/v1/**` | `POST`, `PUT`, `DELETE` | `ADMIN` o `USER` |
| `/api/v1/admin/**` | Todos | `ADMIN` |
| `/swagger-ui/**`, `/v3/api-docs/**` | `GET` | Público |
