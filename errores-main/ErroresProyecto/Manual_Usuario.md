# Manual de Usuario - Gestor de Errores

## 1. Introduccion

El **Gestor de Errores** es una aplicacion de escritorio para registrar, consultar, actualizar y dar seguimiento a errores o bugs encontrados durante el desarrollo de software. Permite a los equipos de trabajo llevar un control organizado del ciclo de vida de cada error, desde su registro hasta su solucion.

---

## 2. Requisitos del Sistema

| Requisito | Detalle |
|-----------|---------|
| Java | JDK 25 o superior |
| Base de datos | MySQL 8.0 (via Docker o instalacion local) |
| Conector | mysql-connector-j-9.6.0.jar (incluido en `lib/`) |
| Sistema operativo | Windows 10/11, macOS o Linux |

---

## 3. Instalacion y Configuracion

### 3.1 Iniciar la base de datos con Docker

```bash
cd ErroresProyecto
docker-compose up -d
```

Esto levanta:
- **MySQL** en el puerto `3306` (usuario: `root`, sin contrasena)
- **phpMyAdmin** en `http://localhost:8080`

### 3.2 Ejecutar la aplicacion

```bash
java -cp "build/classes;lib/mysql-connector-j-9.6.0.jar" Vista.Login
```

> En Linux/macOS usar `:` en lugar de `;` como separador.

### 3.3 Configuracion de la base de datos

El archivo `src/Conexion/db.properties` contiene la configuracion de conexion:

```properties
db.url=jdbc:mysql://localhost:3306/errores
db.user=root
db.password=
```

Modifique estos valores si su servidor MySQL tiene una configuracion diferente.

---

## 4. Inicio de Sesion (Login)

Al iniciar la aplicacion se muestra la pantalla de login.

**Pasos:**
1. Ingrese su **nombre de usuario** en el campo "Usuario"
2. Ingrese su **contrasena** en el campo "Contrasena"
3. Haga clic en **"Ingresar"**

**Usuarios predeterminados:**

| Usuario | Contrasena | Rol |
|---------|-----------|-----|
| admin | admin123 | Administrador |
| kimberly | kimberly123 | Administrador |

> Si las credenciales son correctas, vera el mensaje "Bienvenido" y se abrira el menu principal.

---

## 5. Menu Principal

Desde el menu principal puede acceder a todas las funciones del sistema:

| Boton | Funcion |
|-------|---------|
| **Registrar Error** | Abre el formulario para registrar un nuevo error |
| **Ver Errores** | Muestra la tabla con todos los errores registrados |
| **Dashboard** | Muestra graficos y estadisticas de los errores |
| **Errores Resueltos** | Lista filtrable de errores ya solucionados |
| **Salir** | Cierra la aplicacion (pide confirmacion) |

---

## 6. Registrar un Nuevo Error

**Pasos:**
1. Desde el menu principal, haga clic en **"Registrar Error"**
2. Complete los campos obligatorios:
   - **Error (Titulo):** Nombre breve del error (maximo 100 caracteres)
   - **Descripcion:** Detalle completo del problema encontrado
   - **Severidad:** Seleccione el nivel (Baja, Media, Alta, Critica)
3. Campos opcionales:
   - **Pasos para Reproducir:** Describa paso a paso como reproducir el error
   - **Adjuntar Captura:** Haga clic en "Adjuntar Captura" para seleccionar una imagen (PNG, JPG o GIF, maximo 5 MB)
4. Haga clic en **"Guardar"**

> Los campos obligatorios se resaltaran en rojo si estan vacios al intentar guardar.

**Niveles de Severidad:**

| Nivel | Descripcion |
|-------|-------------|
| Baja | Error menor, no afecta funcionalidad principal |
| Media | Error que afecta funcionalidad secundaria |
| Alta | Error que afecta funcionalidad principal |
| Critica | Error que impide el uso del sistema |

---

## 7. Consultar y Editar Errores

**Pasos para consultar:**
1. Desde el menu principal, haga clic en **"Ver Errores"**
2. La tabla muestra todos los errores con: ID, Titulo, Descripcion, Severidad, Fase, Fecha, Solucion, Resuelto Por y Fecha de Solucion

**Buscar errores:**
1. Escriba un termino en el campo **"Buscar"** (busca por titulo)
2. Seleccione una **fase** del filtro o deje "Todas"
3. Haga clic en **"Buscar"**

**Editar un error:**
1. Haga clic en una fila de la tabla para seleccionarla
2. Cambie la **Fase** en el combo inferior
3. Si selecciona "Solucionado", aparecen los campos de solucion:
   - **Solucion:** Describa brevemente como se resolvio
   - **Descripcion de la Solucion:** Agregue detalles tecnicos de la solucion
4. Haga clic en **"Guardar"**

> El sistema registra automaticamente quien resolvio el error y la fecha.

**Ver captura de pantalla:**
1. Seleccione un error en la tabla
2. Haga clic en **"Ver Captura"**
3. Se abrira una ventana con la imagen adjunta

---

## 8. Exportar Errores a CSV

Esta funcion permite generar un reporte descargable en formato CSV.

**Pasos:**
1. En la vista de errores, haga clic en **"Exportar CSV"**
2. Seleccione la ubicacion y nombre del archivo
3. Haga clic en **"Guardar"**

El archivo CSV puede abrirse con Excel, Google Sheets o cualquier editor de texto.

---

## 9. Dashboard (Estadisticas)

El Dashboard muestra graficos en tiempo real sobre los errores del sistema:

| Grafico | Descripcion |
|---------|-------------|
| **Barras** | Errores por severidad (Baja, Media, Alta, Critica) |
| **Pastel** | Distribucion por fase (Registrado, Proceso, Solucionado, Cerrado) |
| **Lineas** | Tendencia de errores por mes |
| **Resumen** | Total de errores, resueltos, promedio de resolucion y porcentaje |

Haga clic en **"Actualizar"** para recargar los datos.

---

## 10. Errores Resueltos

Vista especializada para consultar solo errores con fase "Solucionado" o "Cerrado".

**Filtros disponibles:**
- **Titulo:** Busca por nombre del error
- **Severidad:** Filtra por nivel de severidad
- **Resuelto por:** Filtra por el usuario que lo resolvio
- **Desde / Hasta:** Rango de fechas de solucion (formato: AAAA-MM-DD)

Al seleccionar un error resuelto, se muestra el detalle de la solucion en el panel inferior.

---

## 11. Fases del Ciclo de Vida de un Error

Cada error pasa por las siguientes fases:

```
REGISTRADO  -->  PROCESO  -->  SOLUCIONADO  -->  CERRADO
```

| Fase | Significado |
|------|-------------|
| **Registrado** | Error recien reportado, pendiente de revision |
| **Proceso** | Se esta trabajando en la solucion del error |
| **Solucionado** | El error fue corregido (requiere descripcion de solucion) |
| **Cerrado** | La solucion fue verificada y el error se da por terminado |

---

## 12. Eliminar un Error

**Pasos:**
1. En la tabla de errores, seleccione la fila del error
2. Haga clic en **"Eliminar"**
3. Confirme la eliminacion en el dialogo

> **Advertencia:** Esta accion es irreversible. El error se elimina permanentemente de la base de datos.

---

## 13. Preguntas Frecuentes

**P: No puedo conectarme a la base de datos**
R: Verifique que Docker este corriendo (`docker ps`) y que el contenedor `errores_mysql` este activo. Revise la configuracion en `db.properties`.

**P: Olvide mi contrasena**
R: Acceda a phpMyAdmin (`http://localhost:8080`), vaya a la tabla `usuarios` y actualice el campo `password` con el hash SHA-256 de su nueva contrasena.

**P: La imagen no se muestra**
R: Verifique que el archivo existe en la carpeta `capturas/` y que el formato es PNG, JPG o GIF.

**P: Como compilar el proyecto**
R: Ejecute desde la carpeta del proyecto:
```bash
find src -name "*.java" | xargs javac -cp "lib/mysql-connector-j-9.6.0.jar" -d build/classes
```

---

## 14. Estructura del Proyecto

```
ErroresProyecto/
  src/
    Conexion/          -- Conexion a la base de datos
    Dao/               -- Acceso a datos (ErrorDAO, EstadisticasDAO, Usuario)
    Modelo/            -- Clases del dominio (ErrorTicket, Fase, Severidad)
    Servicio/          -- Logica de negocio (GestorErrores)
    Vista/             -- Interfaces graficas (Login, Principal, Tabla, etc.)
    Utilidades/        -- Herramientas auxiliares (Tema, ExportadorCSV, etc.)
    Graficos/          -- Paneles de graficos (Barras, Pastel, Lineas)
    Imagenes/          -- Iconos e imagenes de la aplicacion
    Script_BD.sql      -- Script para crear la base de datos
  lib/
    mysql-connector-j-9.6.0.jar
  docker-compose.yml   -- Configuracion de Docker (MySQL + phpMyAdmin)
  build/classes/       -- Archivos compilados (.class)
```
