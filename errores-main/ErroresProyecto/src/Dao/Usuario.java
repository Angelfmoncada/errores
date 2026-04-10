package Dao;

import Conexion.ConexionBD;
import Modelo.ErrorDaoException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que se encarga de la validación de credenciales de usuario.
 * Retorna el rol del usuario al autenticarse exitosamente.
 */
public class Usuario {

    /**
     * Valida las credenciales del usuario.
     * @return el rol del usuario (ej: "admin", "visual") o null si las credenciales son inválidas.
     */
    public String validarLogin(String username, String password) {
        String hashedPassword = hashSHA256(password);
        String sql = "SELECT rol FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("rol");
            }
            return null;

        } catch (SQLException e) {
            throw new ErrorDaoException("Error en login: " + e.getMessage(), e);
        }
    }

    private String hashSHA256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
