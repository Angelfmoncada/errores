# Refactorizacion del Codigo

---

## 1. Problemas Detectados (Antes)

Antes de la refactorizacion, el sistema presentaba los siguientes problemas estructurales:

| Problema | Ubicacion | Gravedad |
|----------|-----------|----------|
| Codigo duplicado (mapeo de ErrorTicket) | ErrorDAO.java | Alta |
| Bloques if-else repetidos para enums | FrmRegistrarError, frmTableErrores | Alta |
| Credenciales hardcodeadas en codigo | ConexionBD.java | Critica |
| Singleton no thread-safe | SesionUsuario.java | Media |
| System.err.println en vez de Logger | Icono.java, Imagen.java | Baja |
| Logica de negocio en las Vistas | frmTableErrores.java | Alta |
| Busqueda ineficiente (iterar lista completa) | frmTableErrores.java | Media |
| Sin validacion visual en formularios | FrmRegistrarError.java | Media |
| Estilos hardcodeados en cada ventana | Todas las Vistas | Media |

---

## 2. Codigo Duplicado

### Antes: Mapeo de ErrorTicket duplicado 4 veces

El mismo bloque de 12 lineas se repetia en los metodos `obtenerTodos()`, `buscar()`, `buscarResueltos()` y cada vez que se necesitaba leer un error del ResultSet:

```java
// ANTES - Este bloque aparecia 4 VECES en ErrorDAO.java
ErrorTicket e = new ErrorTicket(
    rs.getString("titulo"),
    rs.getString("descripcion"),
    Severidad.valueOf(rs.getString("severidad")),
    Fase.valueOf(rs.getString("fase"))
);
e.setId(rs.getInt("id"));
e.setFecha(rs.getTimestamp("fecha"));
e.setSolucion(rs.getString("solucion"));
e.setResueltoPor(rs.getString("resuelto_por"));
e.setFechaSolucion(rs.getTimestamp("fecha_solucion"));
e.setCapturaError(rs.getString("captura_error"));
e.setPasosReproducir(rs.getString("pasos_reproducir"));
e.setDescripcionSolucion(rs.getString("descripcion_solucion"));
```

### Despues: Metodo unico centralizado

```java
// DESPUES - Un solo metodo reutilizable
private ErrorTicket mapearFila(ResultSet rs) throws SQLException {
    ErrorTicket e = new ErrorTicket(
        rs.getString("titulo"),
        rs.getString("descripcion"),
        Severidad.valueOf(rs.getString("severidad")),
        Fase.valueOf(rs.getString("fase"))
    );
    e.setId(rs.getInt("id"));
    e.setFecha(rs.getTimestamp("fecha"));
    e.setSolucion(rs.getString("solucion"));
    e.setRegistradoPor(rs.getString("registrado_por"));
    e.setProcesoPor(rs.getString("proceso_por"));
    e.setResueltoPor(rs.getString("resuelto_por"));
    e.setFechaSolucion(rs.getTimestamp("fecha_solucion"));
    e.setCapturaError(rs.getString("captura_error"));
    e.setPasosReproducir(rs.getString("pasos_reproducir"));
    e.setDescripcionSolucion(rs.getString("descripcion_solucion"));
    return e;
}
```

**Resultado:** 48 lineas duplicadas reducidas a 1 metodo de 16 lineas.

---

## 3. Separacion en Modulos/Clases

### Nuevas clases creadas para separar responsabilidades:

| Clase | Paquete | Responsabilidad |
|-------|---------|-----------------|
| `Tema.java` | Utilidades | Centraliza colores, fuentes y estilos visuales |
| `ExportadorCSV.java` | Utilidades | Logica de exportacion a formato CSV |
| `ValidadorCampos.java` | Utilidades | Validacion visual de campos de formulario |
| `db.properties` | Conexion | Configuracion externalizada de la base de datos |

### Arquitectura por capas (ya existente, ahora mejorada):

```
Vista/              -> Interfaz grafica (solo presentacion)
    |
Servicio/           -> Logica de negocio (GestorErrores)
    |
Dao/                -> Acceso a datos (ErrorDAO, EstadisticasDAO, Usuario)
    |
Conexion/           -> Conexion a BD (ConexionBD + db.properties)
    |
Modelo/             -> Clases del dominio (ErrorTicket, Fase, Severidad)
    |
Utilidades/         -> Clases auxiliares (Tema, ExportadorCSV, ValidadorCampos)
```

---

## 4. Mejora en Nombres de Variables y Funciones

### Antes vs Despues:

| Antes | Despues | Razon |
|-------|---------|-------|
| Bloques if-else sin nombre | `Fase.fromIndex(int)` | Metodo descriptivo y reutilizable |
| `new GestorErrores()` en cada metodo | `private final GestorErrores gestor` | Instancia unica, no se recrea |
| SQL hardcodeado repetido | `COLUMNAS_SELECT` constante | Un solo lugar para mantener |
| `System.err.println(...)` | `logger.log(Level.WARNING, ...)` | Logger estandar con niveles |
| Color `new Color(0,0,102)` repetido | `Tema.PRIMARIO` | Constante con nombre significativo |
| `new Font("Trebuchet MS", 1, 14)` | `Tema.FUENTE_SUBTITULO` | Fuente centralizada |

---

## 5. Fragmentos de Codigo Antes vs Despues

### 5.1 Conversion de indice a enum

**ANTES (repetido 5 veces en diferentes Vistas):**
```java
Fase faseSeleccionada = Fase.REGISTRADO;
int index = cboFase.getSelectedIndex();
if (index == 0) {
    faseSeleccionada = Fase.REGISTRADO;
} else if (index == 1) {
    faseSeleccionada = Fase.PROCESO;
} else if (index == 2) {
    faseSeleccionada = Fase.SOLUCIONADO;
} else if (index == 3) {
    faseSeleccionada = Fase.CERRADO;
}
```

**DESPUES (una sola linea):**
```java
Fase faseSeleccionada = Fase.fromIndex(cboFase.getSelectedIndex());
```

**Implementacion del metodo en el enum:**
```java
public static Fase fromIndex(int index) {
    Fase[] valores = values();
    if (index < 0 || index >= valores.length) {
        return REGISTRADO;
    }
    return valores[index];
}
```

---

### 5.2 Conexion a base de datos

**ANTES (credenciales hardcodeadas):**
```java
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/errores";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```

**DESPUES (configuracion externalizada):**
```java
public class ConexionBD {
    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        Properties props = new Properties();
        try (InputStream input = ConexionBD.class.getResourceAsStream("/Conexion/db.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error al leer db.properties", e);
        }
        URL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/errores");
        USER = props.getProperty("db.user", "root");
        PASS = props.getProperty("db.password", "");
    }
}
```

---

### 5.3 Singleton thread-safe

**ANTES (no thread-safe):**
```java
public static SesionUsuario getInstancia() {
    if (instancia == null) {
        instancia = new SesionUsuario();
    }
    return instancia;
}
```

**DESPUES (double-checked locking con volatile):**
```java
private static volatile SesionUsuario instancia;

public static SesionUsuario getInstancia() {
    if (instancia == null) {
        synchronized (SesionUsuario.class) {
            if (instancia == null) {
                instancia = new SesionUsuario();
            }
        }
    }
    return instancia;
}
```

---

### 5.4 Busqueda ineficiente

**ANTES (iterar toda la lista para encontrar un error):**
```java
// En frmTableErrores.java - se obtenia TODA la lista y se iteraba
List<ErrorTicket> errores = new GestorErrores().obtenerTodosErrores();
for (ErrorTicket err : errores) {
    if (err.getId() == id) {
        txtDescSolucion.setText(err.getDescripcionSolucion());
        break;
    }
}
```

**DESPUES (consulta directa por ID):**
```java
ErrorTicket err = gestor.buscarPorId(id);
if (err != null) {
    txtDescSolucion.setText(err.getDescripcionSolucion());
}
```

---

### 5.5 Consultas duplicadas en DAO

**ANTES (cada metodo construia su propia consulta y loop):**
```java
public List<ErrorTicket> obtenerTodos() {
    List<ErrorTicket> lista = new ArrayList<>();
    String sql = "SELECT id, titulo, descripcion, ... FROM errores";
    try (Connection con = ...; Statement st = ...; ResultSet rs = ...) {
        while (rs.next()) {
            // 12 lineas de mapeo duplicadas aqui
            lista.add(e);
        }
    }
    return lista;
}
```

**DESPUES (metodo generico con interfaz funcional):**
```java
private List<ErrorTicket> ejecutarConsulta(String sql, ParametrosSetter setter) {
    List<ErrorTicket> lista = new ArrayList<>();
    try (Connection con = ConexionBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        if (setter != null) setter.setParametros(ps);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearFila(rs));
            }
        }
    }
    return lista;
}

// Uso simplificado:
public List<ErrorTicket> obtenerTodos() {
    return ejecutarConsulta("SELECT " + COLUMNAS_SELECT + " FROM errores", null);
}
```

---

## 6. Explicacion Breve de Cada Mejora

| # | Mejora | Principio SOLID | Explicacion |
|---|--------|-----------------|-------------|
| 1 | `mapearFila()` centralizado | DRY (Don't Repeat Yourself) | Elimina duplicacion, un solo punto de cambio |
| 2 | `Fase.fromIndex()` / `Severidad.fromIndex()` | Single Responsibility | El enum sabe como convertirse, no las vistas |
| 3 | `db.properties` externo | Open/Closed | Configuracion abierta a extension, cerrada a modificacion |
| 4 | Singleton volatile | Thread Safety | Previene bugs en entornos multi-hilo |
| 5 | `buscarPorId()` en DAO | Liskov Substitution | La capa de datos provee la operacion correcta |
| 6 | `Tema.java` centralizado | Single Responsibility | Una clase, una responsabilidad: el tema visual |
| 7 | `ExportadorCSV.java` | Single Responsibility | Logica de exportacion separada de la vista |
| 8 | `ValidadorCampos.java` | Single Responsibility | Validacion separada de la logica de formulario |
| 9 | `ejecutarConsulta()` generico | Interface Segregation | Interfaz funcional minima para setear parametros |
| 10 | Logger en vez de System.err | Dependency Inversion | Depende de abstraccion (Logger), no de implementacion (stderr) |
