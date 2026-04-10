# Documentacion para Usuario Final

---

## 1. Descripcion del Sistema

El **Gestor de Errores** es una aplicacion de escritorio desarrollada en Java que permite a equipos de desarrollo de software registrar, dar seguimiento y resolver errores (bugs) encontrados durante el ciclo de vida de un proyecto.

### Funcionalidades principales:
- Registro de errores con titulo, descripcion, severidad y capturas de pantalla
- Seguimiento del ciclo de vida: Registrado -> Proceso -> Solucionado -> Cerrado
- Registro automatico de quien realizo cada cambio de fase
- Dashboard con graficos estadisticos (barras, pastel, lineas)
- Modulo de errores resueltos con filtros avanzados
- Exportacion de reportes a CSV
- Sistema de autenticacion con roles (admin, visual)

---

## 2. Requisitos Minimos

| Componente | Requisito |
|------------|-----------|
| **Sistema Operativo** | Windows 10/11, macOS 10.15+ o Linux |
| **Java** | JDK 25 o superior |
| **Base de Datos** | MySQL 8.0 (via Docker o instalacion local) |
| **Docker** | Docker Desktop (para levantar MySQL y phpMyAdmin) |
| **RAM** | 4 GB minimo |
| **Disco** | 500 MB libres |
| **Pantalla** | Resolucion minima 1024x768 |

### Software necesario:
1. **JDK 25:** Descargar desde https://www.oracle.com/java/technologies/downloads/
2. **Docker Desktop:** Descargar desde https://www.docker.com/products/docker-desktop/
3. **MySQL Connector J 9.6.0:** Ya incluido en la carpeta `lib/` del proyecto

---

## 3. Guia de Uso Paso a Paso

### Paso 1: Iniciar la Base de Datos

1. Abra **Docker Desktop** y espere a que este en estado "Running"
2. Abra una terminal (CMD o PowerShell)
3. Navegue a la carpeta del proyecto:
   ```
   cd C:\ruta\al\proyecto\ErroresProyecto
   ```
4. Ejecute el comando:
   ```
   docker-compose up -d
   ```
5. Espere a que aparezca el mensaje indicando que los contenedores estan activos
6. Verifique accediendo a phpMyAdmin en: **http://localhost:8080**

### Paso 2: Ejecutar la Aplicacion

1. En la misma terminal, ejecute:
   ```
   java -cp "build/classes;lib/mysql-connector-j-9.6.0.jar" Vista.Login
   ```
2. Aparecera la ventana de Login del sistema

### Paso 3: Iniciar Sesion

(Ver seccion 4 - Como iniciar sesion)

### Paso 4: Usar el Sistema

(Ver seccion 5 - Como usar las funciones principales)

---

## 4. Como Iniciar Sesion

### Pantalla de Login

Al iniciar la aplicacion se muestra la pantalla de autenticacion con:
- Campo de **Usuario**
- Campo de **Contrasena**
- Boton **Ingresar**
- Boton **Salir**

### Pasos:
1. Escriba su nombre de usuario en el campo "Usuario"
2. Escriba su contrasena en el campo "Contrasena"
3. Haga clic en el boton **"Ingresar"**
4. Si las credenciales son correctas, vera el mensaje "Bienvenido [usuario]"
5. Se abrira automaticamente el Menu Principal

### Usuarios predeterminados:

| Usuario | Contrasena | Rol | Permisos |
|---------|-----------|-----|----------|
| admin | admin123 | Administrador | Acceso completo |
| kimberly | kimberly123 | Administrador | Acceso completo |

### Si las credenciales son incorrectas:
- Aparece el mensaje "Usuario o contrasena incorrectos"
- Verifique que no tenga activado Bloq Mayus
- Intente nuevamente

---

## 5. Como Usar las Funciones Principales

### 5.1 Menu Principal

Desde aqui accede a todas las funciones. Los botones disponibles son:

| Boton | Funcion |
|-------|---------|
| **Registrar Error** | Abre formulario para reportar un nuevo error |
| **Ver Errores** | Muestra la tabla con todos los errores |
| **Dashboard** | Graficos y estadisticas |
| **Errores Resueltos** | Lista de errores ya solucionados |
| **Salir** | Cierra la aplicacion |

---

### 5.2 Registrar un Nuevo Error

**Ruta:** Menu Principal -> Registrar Error

**Campos del formulario:**

| Campo | Obligatorio | Descripcion |
|-------|-------------|-------------|
| Error (Titulo) | Si | Nombre breve del error (maximo 100 caracteres) |
| Descripcion | Si | Detalle completo del problema |
| Severidad | Si | Nivel: Baja, Media, Alta o Critica |
| Pasos para Reproducir | No | Instrucciones paso a paso |
| Captura de Pantalla | No | Imagen PNG, JPG o GIF (max 5 MB) |

**Pasos:**
1. Escriba el titulo del error (el contador muestra X/100 caracteres)
2. Seleccione la severidad en el combo
3. Escriba la descripcion detallada
4. (Opcional) Escriba los pasos para reproducir el error
5. (Opcional) Haga clic en "Adjuntar Captura" para seleccionar una imagen
6. Haga clic en **"Guardar"**
7. Aparece mensaje de confirmacion: "Error registrado correctamente"

**Nota:** El sistema registra automaticamente su nombre de usuario como "Registrado Por".

**Niveles de Severidad:**
- **Baja:** Error menor que no afecta la funcionalidad principal
- **Media:** Error que afecta funcionalidad secundaria
- **Alta:** Error que afecta funcionalidad principal del sistema
- **Critica:** Error que impide el uso del sistema por completo

---

### 5.3 Ver y Gestionar Errores

**Ruta:** Menu Principal -> Ver Errores

**Funciones de la tabla:**

| Accion | Como hacerlo |
|--------|-------------|
| Ver todos los errores | Se cargan automaticamente al abrir |
| Buscar por titulo | Escribir en campo "Buscar" y clic en "Buscar" |
| Filtrar por fase | Seleccionar fase en el combo y clic en "Buscar" |
| Ordenar por columna | Clic en el encabezado de la columna |
| Ver detalle completo | **Doble clic** en la fila del error |
| Ver captura adjunta | Seleccionar fila y clic en "Ver Captura" |
| Exportar a CSV | Clic en "Exportar CSV" y elegir ubicacion |

**Columnas de la tabla:**
- ID, Titulo, Severidad, Fase, Fecha
- Registrado Por, En Proceso Por, Resuelto Por, Fecha Solucion

**Colores de las filas:**
- Verde claro = Severidad Baja
- Amarillo claro = Severidad Media
- Naranja claro = Severidad Alta
- Rojo claro = Severidad Critica

**Barra de estado (parte inferior):**
- Izquierda: Nombre del usuario logueado
- Derecha: Total de errores mostrados

---

### 5.4 Cambiar la Fase de un Error

**Pasos:**
1. Seleccione un error haciendo clic en la fila
2. En el combo "Fase", seleccione la nueva fase
3. Si selecciona "Solucionado":
   - Aparecen los campos "Solucion" y "Descripcion de la Solucion"
   - Escriba como se resolvio el error
4. Haga clic en **"Guardar"**

**Registro automatico de responsables:**
- Al cambiar a **Proceso**: Se registra su usuario como "En Proceso Por"
- Al cambiar a **Solucionado**: Se registra su usuario como "Resuelto Por" y la fecha
- Al revertir a **Registrado**: Se limpian los campos de proceso y resolucion

---

### 5.5 Eliminar un Error

1. Seleccione el error en la tabla
2. Haga clic en **"Eliminar"**
3. Confirme en el dialogo "Esta seguro de eliminar este error?"
4. El error se elimina permanentemente

**Advertencia:** Esta accion no se puede deshacer.

---

### 5.6 Dashboard (Estadisticas)

**Ruta:** Menu Principal -> Dashboard

Muestra 4 paneles con informacion en tiempo real:

| Panel | Tipo de Grafico | Informacion |
|-------|----------------|-------------|
| Superior izquierdo | Barras | Errores por severidad |
| Superior derecho | Pastel (pie) | Distribucion por fase |
| Inferior izquierdo | Lineas | Tendencia mensual de errores |
| Inferior derecho | Resumen | Total, resueltos, promedio, porcentaje |

Haga clic en **"Actualizar"** para recargar los graficos con datos recientes.

---

### 5.7 Errores Resueltos

**Ruta:** Menu Principal -> Errores Resueltos

Vista especializada que muestra solo errores con fase "Solucionado" o "Cerrado".

**Filtros disponibles:**
- Titulo (busqueda por texto)
- Severidad (combo)
- Resuelto por (combo con usuarios que han resuelto errores)
- Rango de fechas (Desde / Hasta en formato AAAA-MM-DD)

Al seleccionar un error, el panel inferior muestra:
- Solucion aplicada
- Descripcion detallada de la solucion
- Pasos para reproducir (si fueron documentados)

---

### 5.8 Exportar Errores a CSV

1. En la vista de errores, haga clic en **"Exportar CSV"**
2. Seleccione la carpeta donde guardar el archivo
3. Escriba el nombre del archivo (se sugiere "reporte_errores.csv")
4. Haga clic en **"Guardar"**
5. Aparece mensaje de confirmacion con la ruta del archivo

El archivo CSV se puede abrir con:
- Microsoft Excel
- Google Sheets
- LibreOffice Calc
- Cualquier editor de texto

---

## 6. Ejemplos de Uso

### Ejemplo 1: Registrar un error critico

1. Login como "admin" / "admin123"
2. Clic en "Registrar Error"
3. Titulo: "Login no funciona con caracteres especiales"
4. Severidad: **Critica**
5. Descripcion: "Al ingresar una contrasena con el caracter @ el sistema rechaza las credenciales aunque sean correctas"
6. Pasos para Reproducir:
   ```
   1. Abrir la pantalla de login
   2. Ingresar usuario: test@user
   3. Ingresar contrasena: pass@123
   4. Clic en Ingresar
   5. Resultado: "Usuario o contrasena incorrectos"
   ```
7. Adjuntar captura de pantalla del error
8. Clic en "Guardar"

### Ejemplo 2: Resolver un error

1. Ir a "Ver Errores"
2. Seleccionar el error critico del login
3. Cambiar fase a "Proceso" -> Guardar (se registra quien lo tomo)
4. Cambiar fase a "Solucionado"
5. Solucion: "Escapar caracteres especiales en la consulta SQL"
6. Descripcion de Solucion: "Se modifico el metodo validarLogin para usar PreparedStatement con parametros, evitando problemas con caracteres especiales en usuario y contrasena"
7. Clic en "Guardar"

### Ejemplo 3: Generar reporte

1. Ir a "Ver Errores"
2. Clic en "Exportar CSV"
3. Guardar como "reporte_abril_2026.csv"
4. Abrir el archivo en Excel para analisis

---

## 7. Problemas Comunes y Soluciones

### P: "No puedo conectarme a la base de datos"
**Solucion:**
1. Verifique que Docker Desktop este corriendo (icono de ballena en barra de tareas)
2. Ejecute `docker ps` en terminal para ver si el contenedor esta activo
3. Si no esta activo, ejecute: `docker-compose up -d`
4. Espere 15 segundos y vuelva a intentar
5. Si persiste, revise el archivo `db.properties` en la carpeta Conexion

---

### P: "Olvide mi contrasena"
**Solucion:**
1. Acceda a phpMyAdmin: http://localhost:8080
2. Seleccione la base de datos "errores"
3. Abra la tabla "usuarios"
4. Busque su usuario y actualice el campo "password"
5. Use un generador SHA-256 en linea para crear el hash de su nueva contrasena

---

### P: "La imagen no se muestra al ver captura"
**Solucion:**
1. Verifique que la carpeta `capturas/` exista junto al proyecto
2. Verifique que el archivo de imagen exista dentro de esa carpeta
3. Formatos soportados: PNG, JPG, GIF
4. Tamano maximo: 5 MB

---

### P: "La tabla esta vacia al abrir Ver Errores"
**Solucion:**
1. Verifique la conexion a la base de datos (Docker corriendo)
2. Registre al menos un error desde "Registrar Error"
3. Si aparece un mensaje de error, revise los logs en la terminal

---

### P: "El boton Guardar no hace nada al editar"
**Solucion:**
1. Asegurese de haber seleccionado una fila de la tabla primero (clic en la fila)
2. Debe seleccionar una fase en el combo antes de guardar
3. Si cambia a "Solucionado", debe escribir la solucion

---

### P: "El contador de caracteres esta en rojo"
**Solucion:**
- El titulo no puede exceder 100 caracteres
- Reduzca el texto del titulo para que el contador vuelva a verde
- Use la descripcion para detalles adicionales

---

### P: "No puedo exportar a CSV"
**Solucion:**
1. Verifique que haya al menos un error registrado
2. Asegurese de tener permisos de escritura en la carpeta destino
3. Si el archivo ya existe, el sistema lo sobreescribe

---

### P: "Los graficos del Dashboard estan vacios"
**Solucion:**
1. Debe haber al menos un error registrado para que aparezcan datos
2. Haga clic en "Actualizar" para recargar los graficos
3. Verifique la conexion a la base de datos

---

### P: "Como compilo el proyecto?"
**Solucion:**
Ejecute desde la carpeta del proyecto:
```
find src -name "*.java" | xargs javac -cp "lib/mysql-connector-j-9.6.0.jar" -d build/classes
```
Luego copie los recursos:
```
cp src/Conexion/db.properties build/classes/Conexion/
cp src/Imagenes/*.png build/classes/Imagenes/
```
