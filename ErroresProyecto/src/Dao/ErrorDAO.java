package Dao;

import Conexion.ConexionBD;
import Modelo.Fase;
import Modelo.Severidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.ErrorTicket;

/**
 * Clase que maneja la conexión a la base de datos para los errores.
 */
public class ErrorDAO {

    /**
     * Inserta un nuevo error en la base de datos.
     */
    public void insertar(ErrorTicket e) {
        //Inyección/Consulta SQL
        String sql = "INSERT INTO errores (titulo, descripcion, severidad, fase,solucion) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            //Asignación de parametros desde la consulta                
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getSeveridad().name());
            ps.setString(4, e.getFase().name());
            ps.setString(5, e.getSolucion());
            //Ejecuta la consulta
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error al insertar: " + ex.getMessage());
        }
    }

    /**
     * Devuelve todos los errores registrados en la base de datos.
     */
    public List<ErrorTicket> obtenerTodos() {
        List<ErrorTicket> lista = new ArrayList<>();
       String sql = "SELECT id, titulo, descripcion, severidad, fase, fecha, solucion FROM errores";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                //Instancia de los datos
                ErrorTicket e = new ErrorTicket(
    rs.getString("titulo"),
    rs.getString("descripcion"),
    Severidad.valueOf(rs.getString("severidad")),
    Fase.valueOf(rs.getString("fase"))
);
        //Asignar atributos que no están en el constructor        
        e.setId(rs.getInt("id"));
        e.setFecha(rs.getTimestamp("fecha"));
        e.setSolucion(rs.getString("solucion"));

                //Agregar los datos a la lista
                lista.add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Error al obtener errores: " + ex.getMessage());
        }

        return lista;
    }

    /**
     * Actualiza un error existente (fase y solución).
     */
    public void actualizar(ErrorTicket e) {
        String sql = "UPDATE errores SET fase = ?, solucion = ? WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            //Asignación de valores de la consulta
            ps.setString(1, e.getFase().name());
            ps.setString(2, e.getSolucion());
            ps.setInt(3, e.getId());
            //Ejecución de consulta
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error al actualizar: " + ex.getMessage());
        }
    }

    /**
     * Elimina un error de la base de datos por su ID.
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM errores WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error al eliminar: " + ex.getMessage());
        }
    }
}

