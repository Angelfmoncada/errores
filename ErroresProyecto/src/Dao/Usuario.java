/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Clase que se encarga de la validación de credenciales de usuario
 * 
 */
public class Usuario {
    
    public boolean validarLogin(String username, String password) {
        //Inyección SQL
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        //Si intenta la conexión con la base de datos
        try (Connection con = ConexionBD.conectar();
                //Preparación de la Consulta SQL
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            //Ejecuión de la consulta
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true si existe el usuario

        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
            return false;
        }
    }
    
}
