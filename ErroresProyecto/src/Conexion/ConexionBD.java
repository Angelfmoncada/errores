package Conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase responsable de gestionar la conexion a la base de datos.
 * Lee la configuracion desde el archivo db.properties para evitar
 * credenciales hardcodeadas en el codigo fuente.
 */
public class ConexionBD {

    private static final Logger logger = Logger.getLogger(ConexionBD.class.getName());

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        Properties props = new Properties();
        try (InputStream input = ConexionBD.class.getResourceAsStream("/Conexion/db.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                logger.warning("No se encontro db.properties, usando valores por defecto");
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error al leer db.properties", e);
        }
        URL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/errores");
        USER = props.getProperty("db.user", "root");
        PASS = props.getProperty("db.password", "");
    }

    /**
     * Obtiene una conexion a la base de datos.
     * @return Connection activa
     * @throws SQLException si no se puede establecer la conexion
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
