-- V2: Refactor estructura BD para alinearla con el nuevo modelo de negocio
-- Ejecutar con transaccion; si algo falla hara rollback

CREATE SCHEMA IF NOT EXISTS "public";

-- ============================================================
-- 1. Renombrar tablas de singular a plural (idempotente)
-- ============================================================
DO $$ BEGIN
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_schema='public' AND table_name='tipo_programa') THEN
        ALTER TABLE public.tipo_programa RENAME TO tipos_programa;
    END IF;
END $$;

DO $$ BEGIN
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_schema='public' AND table_name='programa') THEN
        ALTER TABLE public.programa RENAME TO programas;
    END IF;
END $$;

DO $$ BEGIN
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_schema='public' AND table_name='curso') THEN
        ALTER TABLE public.curso RENAME TO cursos;
    END IF;
END $$;

DO $$ BEGIN
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_schema='public' AND table_name='facultad') THEN
        ALTER TABLE public.facultad RENAME TO facultades;
    END IF;
END $$;

-- ============================================================
-- 2. Renombrar columnas en tipos_programa
-- ============================================================
ALTER TABLE public.tipos_programa
    RENAME COLUMN imagen_card TO card_image_url;

ALTER TABLE public.tipos_programa
    RENAME COLUMN imagen_bg TO hero_bg_url;

-- Backfill NOT NULL: rellenar nulos con placeholder
UPDATE public.tipos_programa
   SET card_image_url = ''
 WHERE card_image_url IS NULL;

UPDATE public.tipos_programa
   SET hero_bg_url = ''
 WHERE hero_bg_url IS NULL;

ALTER TABLE public.tipos_programa
    ALTER COLUMN card_image_url SET NOT NULL;

ALTER TABLE public.tipos_programa
    ALTER COLUMN hero_bg_url SET NOT NULL;

-- ============================================================
-- 3. Renombrar columnas en programas
-- ============================================================
ALTER TABLE public.programas
    RENAME COLUMN imagen TO image_url;

ALTER TABLE public.programas
    RENAME COLUMN convocatoria TO en_convocatoria;

ALTER TABLE public.programas
    RENAME COLUMN id_facultad TO facultad_id;

ALTER TABLE public.programas
    RENAME COLUMN id_tipo_programa TO tipo_programa_id;

-- ============================================================
-- 4. Convertir TEXT -> text[] en programas
--    Asume que los valores estan separados por salto de linea
--    Ajusta el delimitador si usas otro (|, ;, etc.)
-- ============================================================

-- 4a. Crear columna temporal text[] y migrar
ALTER TABLE public.programas
    ADD COLUMN objetivos_especificos_arr text[];

UPDATE public.programas
   SET objetivos_especificos_arr =
       string_to_array(objetivos_especificos, E'\n');

-- 4b. Reemplazar la columna original
ALTER TABLE public.programas
    DROP COLUMN objetivos_especificos;

ALTER TABLE public.programas
    RENAME COLUMN objetivos_especificos_arr TO objetivos_especificos;

-- 4c. Lo mismo para lineas_investigacion
ALTER TABLE public.programas
    ADD COLUMN lineas_investigacion_arr text[];

UPDATE public.programas
   SET lineas_investigacion_arr =
       string_to_array(lineas_investigacion, E'\n');

ALTER TABLE public.programas
    DROP COLUMN lineas_investigacion;

ALTER TABLE public.programas
    RENAME COLUMN lineas_investigacion_arr TO lineas_investigacion;

-- ============================================================
-- 5. Crear tabla detalle_malla (reemplaza programa_curso)
-- ============================================================
CREATE TABLE public.detalle_malla (
    id                serial    NOT NULL,
    programa_id       int       NOT NULL,
    num_semestre      int       NOT NULL,
    orden             int       NOT NULL,
    es_espacio_electivo boolean NOT NULL DEFAULT FALSE,
    curso_id          int,
    costo_soles       numeric(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CHECK (num_semestre > 0),
    CHECK ((es_espacio_electivo = FALSE AND curso_id IS NOT NULL) OR
           (es_espacio_electivo = TRUE  AND curso_id IS NULL))
);

ALTER TABLE public.detalle_malla
    ADD CONSTRAINT fk_detalle_malla_programa_id_programas_id
    FOREIGN KEY (programa_id) REFERENCES public.programas(id);

ALTER TABLE public.detalle_malla
    ADD CONSTRAINT fk_detalle_malla_curso_id_cursos_id
    FOREIGN KEY (curso_id) REFERENCES public.cursos(id);

-- ============================================================
-- 6. Crear tabla opciones_electivas
-- ============================================================
CREATE TABLE public.opciones_electivas (
    id          serial NOT NULL,
    programa_id int    NOT NULL,
    curso_id    int    NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.opciones_electivas
    ADD CONSTRAINT fk_opciones_electivas_programa_id_programas_id
    FOREIGN KEY (programa_id) REFERENCES public.programas(id);

ALTER TABLE public.opciones_electivas
    ADD CONSTRAINT fk_opciones_electivas_curso_id_cursos_id
    FOREIGN KEY (curso_id) REFERENCES public.cursos(id);

-- ============================================================
-- 7. Migrar datos de programa_curso -> detalle_malla
--    (si la tabla programa_curso existe)
-- ============================================================
DO $$
BEGIN
    IF EXISTS (
        SELECT FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name   = 'programa_curso'
    ) THEN
        INSERT INTO public.detalle_malla
            (programa_id, num_semestre, orden, es_espacio_electivo, curso_id, costo_soles)
        SELECT
            pc.id_programa,
            COALESCE(pc.semestre, 1),
            ROW_NUMBER() OVER (
                PARTITION BY pc.id_programa, COALESCE(pc.semestre, 1)
                ORDER BY pc.id
            ),
            COALESCE(pc.electivo, FALSE),
            pc.id_curso,
            pc.costo_cuota
        FROM public.programa_curso pc;

        -- 7b. Eliminar tabla vieja
        DROP TABLE public.programa_curso;
    END IF;
END $$;
