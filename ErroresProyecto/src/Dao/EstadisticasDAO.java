package Dao;

import Conexion.ConexionBD;
import Modelo.ErrorDaoException;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO para consultas de estadísticas y agregaciones.
 * Utilizado por el Dashboard para generar gráficos.
 */
public class EstadisticasDAO {

    public Map<String, Integer> contarPorSeveridad() {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT severidad, COUNT(*) AS total FROM errores GROUP BY severidad ORDER BY severidad";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                datos.put(rs.getString("severidad"), rs.getInt("total"));
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al contar por severidad: " + ex.getMessage(), ex);
        }
        return datos;
    }

    public Map<String, Integer> contarPorFase() {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT fase, COUNT(*) AS total FROM errores GROUP BY fase ORDER BY fase";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                datos.put(rs.getString("fase"), rs.getInt("total"));
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al contar por fase: " + ex.getMessage(), ex);
        }
        return datos;
    }

    public Map<String, Integer> contarPorMes() {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(fecha, '%Y-%m') AS mes, COUNT(*) AS total " +
                     "FROM errores GROUP BY mes ORDER BY mes";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                datos.put(rs.getString("mes"), rs.getInt("total"));
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al contar por mes: " + ex.getMessage(), ex);
        }
        return datos;
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) AS total FROM errores";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al contar total: " + ex.getMessage(), ex);
        }
        return 0;
    }

    public int contarResueltos() {
        String sql = "SELECT COUNT(*) AS total FROM errores WHERE fase IN ('SOLUCIONADO', 'CERRADO')";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al contar resueltos: " + ex.getMessage(), ex);
        }
        return 0;
    }

    public double tiempoPromedioResolucion() {
        String sql = "SELECT AVG(DATEDIFF(fecha_solucion, fecha)) AS promedio " +
                     "FROM errores WHERE fecha_solucion IS NOT NULL AND fecha IS NOT NULL";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("promedio");
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al calcular promedio: " + ex.getMessage(), ex);
        }
        return 0;
    }

    public Map<String, Integer> contarPorResolutor() {
        Map<String, Integer> datos = new LinkedHashMap<>();
        String sql = "SELECT resuelto_por, COUNT(*) AS total FROM errores " +
                     "WHERE resuelto_por IS NOT NULL GROUP BY resuelto_por ORDER BY total DESC";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                datos.put(rs.getString("resuelto_por"), rs.getInt("total"));
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al contar por resolutor: " + ex.getMessage(), ex);
        }
        return datos;
    }
}
