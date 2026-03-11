/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de la conexión de la base de Datos
 * @author Mass
 */
public class ConexionBD {
    
      private static final String URL =
        "jdbc:mysql://localhost:3306/errores";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    
    }
    
}
