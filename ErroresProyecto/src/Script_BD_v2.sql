-- ============================================================
-- Script de migración v2 - Ejecutar sobre BD existente
-- Añade columnas para: resolución, capturas, documentación
-- ============================================================

USE errores;

-- Fase 2: Tracking de resolución
ALTER TABLE errores ADD COLUMN resuelto_por VARCHAR(50) DEFAULT NULL;
ALTER TABLE errores ADD COLUMN fecha_solucion DATETIME DEFAULT NULL;

-- Fase 3: Capturas de pantalla
ALTER TABLE errores ADD COLUMN captura_error VARCHAR(500) DEFAULT NULL;

-- Fase 4: Documentación estructurada
ALTER TABLE errores ADD COLUMN pasos_reproducir TEXT DEFAULT NULL;
ALTER TABLE errores ADD COLUMN descripcion_solucion TEXT DEFAULT NULL;
