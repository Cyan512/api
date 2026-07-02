# Entidades y relaciones

## Diagrama de relaciones

```
TipoPrograma (1) ────< (*) Programa
Facultad     (1) ────< (*) Programa
Programa     (1) ────< (*) DetalleMalla
Curso        (1) ────< (*) DetalleMalla
User         (1) ────< (*) RefreshToken
User         (*) >───< (*) Role   (via user_roles)
```

**Nota:** La tabla `opciones_electivas` existe en BD (`programa_id` → FK programas, `curso_id` → FK cursos) pero su modelo Java y controller están pendientes de implementar.

---

## TipoPrograma

**Tabla:** `tipos_programa`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `nombre` | `String` | Nombre del tipo de programa |
| `cardImageUrl` | `String` | URL de imagen para vista de tarjeta |
| `heroBgUrl` | `String` | URL de imagen de fondo |
| `slug` | `String` | Identificador único para URL (se genera automáticamente) |

**Slug:** Se genera automáticamente desde el `nombre` al crear (ej: "Maestría" → "maestria"). Es **único**.

---

## Facultad

**Tabla:** `facultades`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `nombre` | `String` | Nombre de la facultad |

---

## Curso

**Tabla:** `cursos`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `nombre` | `String` | Nombre del curso |
| `creditos` | `Integer` | Número de créditos |
| `categoria` | `Categoria` (enum) | `OE` (obligatorio específico) o `EE` (electivo específico) |

---

## Programa

**Tabla:** `programas`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `tipoPrograma` | `TipoPrograma` (FK → `tipos_programa.id`) | Tipo de programa al que pertenece |
| `nombre` | `String` | Nombre del programa |
| `facultad` | `Facultad` (FK → `facultades.id`) | Facultad a la que pertenece |
| `slug` | `String` (unique) | Identificador único para URL (se genera automáticamente) |
| `enConvocatoria` | `Boolean` | Indica si el programa tiene convocatoria abierta |
| `modalidad` | `Modalidad` (enum) | `PRESENCIAL`, `SEMIPRESENCIAL` o `VIRTUAL` |
| `imageUrl` | `String` | URL de la imagen del programa |
| `objetivoGeneral` | `Text` | Objetivo general del programa |
| `objetivosEspecificos` | `Text[]` | Objetivos específicos del programa |
| `perfilPosgraduado` | `Text` | Perfil del posgraduado |
| `lineasInvestigacion` | `Text[]` | Líneas de investigación |
| `costoMatricula` | `BigDecimal` | Costo de matrícula |

**Slug:** Se genera automáticamente desde el `nombre` al crear. Es **único**.

---

## Comunicado

**Tabla:** `comunicado`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `titulo` | `String` | Título del comunicado |
| `slug` | `String` (unique) | Identificador único para URL (se genera automáticamente) |
| `resumen` | `Text` | Resumen del comunicado |
| `contenido` | `Text` | Contenido completo del comunicado |
| `imagen` | `String` | URL de la imagen del comunicado |
| `fechaPublicacion` | `LocalDateTime` | Fecha y hora de publicación |

**Slug:** Se genera automáticamente desde el `titulo` al crear. Es **único**.

---

## DetalleMalla

**Tabla:** `detalle_malla`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `numSemestre` | `Integer` | Número de semestre (≥ 1) |
| `costoSoles` | `BigDecimal` | Costo en soles |
| `curso` | `Curso` (FK → `cursos.id`, nullable) | Curso asociado (`null` si es espacio electivo) |
| `programa` | `Programa` (FK → `programas.id`) | Programa al que pertenece |

> **Nota:** La BD (`detalle_malla`) y el DTO (`DetalleMallaRequest`) incluyen los campos `orden` y `es_espacio_electivo`, pero el modelo Java `DetalleMalla` aún no los mapea. En una próxima actualización del modelo estos campos serán persistidos.

---

## User

**Tabla:** `users`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `username` | `String` (unique, 50) | Nombre de usuario |
| `email` | `String` (unique, 120) | Correo electrónico |
| `passwordHash` | `String` | Hash BCrypt de la contraseña (nunca se expone en respuestas) |
| `enabled` | `Boolean` | `true` si el usuario está activo (default `true`) |
| `createdAt` | `LocalDateTime` | Fecha de creación |
| `roles` | `Set<Role>` (M:N via `user_roles`) | Roles asignados |

---

## Role

**Tabla:** `roles`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Integer` | PK, auto-incrementable |
| `name` | `String` (unique, 20) | Nombre del rol (`ROLE_ADMIN`, `ROLE_USER`) |

---

## RefreshToken

**Tabla:** `refresh_tokens`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `Long` | PK, auto-incrementable |
| `userId` | `Long` (FK → `users.id`) | Usuario propietario del token |
| `tokenHash` | `String` (unique) | Hash SHA-256 del refresh token (el token real nunca se persiste) |
| `expiresAt` | `LocalDateTime` | Fecha de expiración |
| `revoked` | `Boolean` | `true` si fue revocado (default `false`) |
| `createdAt` | `LocalDateTime` | Fecha de creación |
