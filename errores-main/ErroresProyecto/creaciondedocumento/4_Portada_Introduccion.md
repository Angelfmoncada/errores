# Proyecto de Software - Tercer Parcial

## Evaluacion, Mejora y Pruebas del Sistema

### Gestor de Errores

---

**Asignatura:** Proyecto de Software

**Parcial:** Tercero

**Sistema:** Gestor de Errores - Sistema de seguimiento y gestion de bugs

---
---

# Introduccion

## Objetivo del Entregable

El presente entregable documenta el proceso de refactorizacion, mejora funcional y elaboracion de documentacion para usuario final del sistema **Gestor de Errores**.

Los objetivos principales son:

1. **Refactorizacion del codigo:** Mejorar la calidad interna del codigo aplicando principios SOLID, eliminando duplicacion y reduciendo el acoplamiento entre componentes.

2. **Mejoras funcionales:** Agregar nuevas caracteristicas que optimicen la experiencia del usuario, mejoren el rendimiento y amplien las capacidades del sistema.

3. **Documentacion:** Desarrollar documentacion clara y accesible para usuarios finales que facilite el uso correcto del sistema.

---

## Alcance del Trabajo Realizado

### Refactorizacion (35%)
- Eliminacion de codigo duplicado en ErrorDAO (4 bloques reducidos a 1 metodo)
- Creacion de metodos utilitarios en enums (`fromIndex()`, `getEtiquetas()`)
- Externalizacion de credenciales de base de datos a archivo de propiedades
- Correccion de singleton thread-safe en SesionUsuario
- Centralizacion del tema visual en clase Tema.java
- Reemplazo de System.err por Logger estandar de Java
- Creacion de metodo generico `ejecutarConsulta()` con interfaz funcional

### Mejoras Funcionales (30%)
- Tracking automatico de responsables por fase (registrado_por, proceso_por, resuelto_por)
- Colores por severidad en la tabla de errores
- Doble clic para ver detalle completo del error
- Ordenamiento de tabla por columna
- Barra de estado con usuario y total de errores
- Exportacion de errores a CSV
- Contador de caracteres en tiempo real en campo titulo
- Validacion visual de formularios con bordes de color

### Documentacion (20%)
- Manual de usuario con 14 secciones
- Documentacion de nuevas caracteristicas
- Evidencia e impacto de cada mejora
- Documentacion de refactorizacion con codigo antes vs despues
- Guia de uso paso a paso

### Organizacion (15%)
- Carpeta `creaciondedocumento/` con documentos organizados por tema
- Estructura logica y facil de revisar
- Nombres descriptivos en cada archivo

---

## Estructura del Entregable

```
creaciondedocumento/
    1_Nuevas_Caracteristicas.md      -> Lista de todas las mejoras implementadas
    2_Evidencia_Impacto.md           -> Evidencia y explicacion del impacto
    3_Refactorizacion_Codigo.md      -> Analisis antes vs despues del codigo
    4_Portada_Introduccion.md        -> Portada, introduccion y alcance
    5_Documentacion_Usuario.md       -> Manual completo para usuario final
```
