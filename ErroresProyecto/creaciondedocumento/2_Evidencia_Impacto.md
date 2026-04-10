# Evidencia y Explicacion del Impacto de Cada Mejora

---

## 1. Tracking de Responsables por Fase

### Evidencia
- Tabla muestra columnas: "Registrado Por", "En Proceso Por", "Resuelto Por"
- Al registrar un error, el campo "Registrado Por" se llena automaticamente con el usuario logueado
- Al cambiar a fase "Proceso", se registra quien lo tomo
- Al cambiar a fase "Solucionado", se registra quien lo resolvio y la fecha

### Impacto
- **Trazabilidad completa:** Se sabe exactamente quien participo en cada etapa del error
- **Rendicion de cuentas:** Cada miembro del equipo tiene registro de su trabajo
- **Auditoria:** Se puede revisar el historial de responsables para cualquier error

---

## 2. Colores por Severidad en la Tabla

### Evidencia
- Filas con severidad "Critica" se muestran en rojo claro
- Filas con severidad "Alta" se muestran en naranja claro
- Filas con severidad "Media" se muestran en amarillo claro
- Filas con severidad "Baja" se muestran en verde claro

### Impacto
- **Identificacion rapida:** El usuario detecta errores criticos de un vistazo sin leer texto
- **Priorizacion visual:** Facilita la toma de decisiones sobre que error atender primero
- **Reduccion de errores humanos:** Menor probabilidad de ignorar errores criticos

---

## 3. Detalle Completo con Doble Clic

### Evidencia
- Al hacer doble clic en cualquier fila, se abre un dialogo con toda la informacion
- Muestra: datos basicos, responsables, descripcion, pasos para reproducir, solucion

### Impacto
- **Acceso rapido a informacion:** No necesita navegar a otra pantalla
- **Vista consolidada:** Toda la informacion del error en un solo lugar
- **Mejor experiencia de usuario:** Interaccion intuitiva (doble clic es un patron conocido)

---

## 4. Ordenamiento de Tabla por Columna

### Evidencia
- Clic en header "Severidad" ordena de Baja a Critica (o viceversa)
- Clic en header "Fecha" ordena del mas reciente al mas antiguo
- Funciona con todas las columnas

### Impacto
- **Flexibilidad:** El usuario organiza la informacion como la necesita
- **Eficiencia:** Encontrar errores especificos sin necesidad de buscar manualmente
- **Analisis:** Permite agrupar visualmente errores por cualquier criterio

---

## 5. Barra de Estado

### Evidencia
- Parte inferior de la ventana muestra "Usuario: admin" y "Total: 5 errores"
- Se actualiza al buscar, cargar o eliminar errores

### Impacto
- **Contexto constante:** El usuario siempre sabe con que cuenta esta logueado
- **Retroalimentacion:** Sabe cuantos errores hay sin contar filas
- **Profesionalismo:** Mejora la percepcion de calidad del sistema

---

## 6. Exportacion a CSV

### Evidencia
- Boton "Exportar CSV" en la barra de herramientas
- Genera archivo .csv con todos los campos del error
- Compatible con Excel y Google Sheets

### Impacto
- **Reportes:** Permite generar reportes para gerencia o clientes
- **Integracion:** Los datos pueden importarse en otras herramientas
- **Respaldo:** Sirve como copia de seguridad de los datos en formato abierto

---

## 7. Contador de Caracteres en Tiempo Real

### Evidencia
- Debajo del campo titulo se muestra "X/100"
- Cambia de color: verde (0-80), naranja (81-100), rojo (101+)

### Impacto
- **Prevencion de errores:** El usuario sabe antes de guardar si excede el limite
- **Retroalimentacion inmediata:** No necesita esperar un mensaje de error
- **Usabilidad:** Patron comun en redes sociales que los usuarios ya conocen

---

## 8. Validacion Visual de Formularios

### Evidencia
- Campos vacios se marcan con borde rojo al intentar guardar
- Campos validos se marcan con borde verde
- Tooltip explica el problema al pasar el mouse

### Impacto
- **Reduccion de errores:** El usuario identifica visualmente que campos corregir
- **Eficiencia:** No necesita leer un mensaje para saber que campo falta
- **Accesibilidad:** La retroalimentacion visual es mas intuitiva que solo texto

---

## 9. Tema Visual Centralizado

### Evidencia
- Todas las ventanas usan los mismos colores, fuentes y estilos
- Definidos en Tema.java como constantes estaticas

### Impacto
- **Consistencia visual:** El sistema se ve profesional y uniforme
- **Mantenibilidad:** Cambiar un color o fuente se hace en un solo lugar
- **Escalabilidad:** Nuevas pantallas heredan automaticamente el tema

---

## 10. Configuracion Externalizada

### Evidencia
- Archivo db.properties con URL, usuario y password de la BD
- ConexionBD.java lee el archivo en vez de tener valores hardcodeados

### Impacto
- **Seguridad:** Las credenciales no estan en el codigo fuente
- **Flexibilidad:** Se puede cambiar la BD sin recompilar
- **Buenas practicas:** Sigue el principio de separacion de configuracion y codigo

---

## 11. Refactorizacion SOLID

### Evidencia
- ErrorDAO: 4 bloques de mapeo duplicados reducidos a 1 metodo `mapearFila()`
- Enums: 5 bloques if-else reemplazados por `Fase.fromIndex()` y `Severidad.fromIndex()`
- SesionUsuario: Singleton ahora es thread-safe

### Impacto
- **Mantenibilidad:** Menos codigo = menos lugares donde introducir bugs
- **Legibilidad:** Codigo mas facil de entender para nuevos desarrolladores
- **Robustez:** El singleton thread-safe previene errores en escenarios concurrentes

---

## 12. Logger Centralizado

### Evidencia
- System.err.println reemplazado por java.util.logging.Logger
- Mensajes de error con niveles (WARNING, SEVERE, INFO)

### Impacto
- **Diagnostico:** Los logs tienen nivel de severidad y origen (clase)
- **Profesionalismo:** Sigue el estandar de la industria Java
- **Configurabilidad:** Se puede redirigir a archivo sin cambiar codigo
