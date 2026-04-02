package Dao;

import Conexion.ConexionBD;
import Modelo.ErrorDaoException;
import Modelo.Fase;
import Modelo.Severidad;
import Modelo.ErrorTicket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que maneja la conexión a la base de datos para los errores.
 */
public class ErrorDAO {

    /**
     * Inserta un nuevo error en la base de datos.
     */
    public void insertar(ErrorTicket e) {
        String sql = "INSERT INTO errores (titulo, descripcion, severidad, fase, solucion, captura_error, pasos_reproducir) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getSeveridad().name());
            ps.setString(4, e.getFase().name());
            ps.setString(5, e.getSolucion());
            ps.setString(6, e.getCapturaError());
            ps.setString(7, e.getPasosReproducir());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al insertar: " + ex.getMessage(), ex);
        }
    }

    /**
     * Devuelve todos los errores registrados en la base de datos.
     */
    public List<ErrorTicket> obtenerTodos() {
        List<ErrorTicket> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, severidad, fase, fecha, solucion, " +
                     "resuelto_por, fecha_solucion, captura_error, pasos_reproducir, descripcion_solucion FROM errores";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
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
                lista.add(e);
            }

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al obtener errores: " + ex.getMessage(), ex);
        }

        return lista;
    }

    /**
     * Actualiza un error existente (fase y solución).
     */
    public void actualizar(ErrorTicket e) {
        String sql = "UPDATE errores SET fase = ?, solucion = ?, resuelto_por = ?, " +
                     "fecha_solucion = ?, descripcion_solucion = ? WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getFase().name());
            ps.setString(2, e.getSolucion());
            ps.setString(3, e.getResueltoPor());
            ps.setTimestamp(4, e.getFechaSolucion());
            ps.setString(5, e.getDescripcionSolucion());
            ps.setInt(6, e.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al actualizar: " + ex.getMessage(), ex);
        }
    }

    /**
     * Busca errores por titulo y/o fase.
     */
    public List<ErrorTicket> buscar(String titulo, String fase) {
        List<ErrorTicket> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT id, titulo, descripcion, severidad, fase, fecha, solucion, " +
            "resuelto_por, fecha_solucion, captura_error, pasos_reproducir, descripcion_solucion FROM errores WHERE 1=1");

        if (titulo != null && !titulo.isEmpty()) {
            sql.append(" AND titulo LIKE ?");
        }
        if (fase != null && !fase.isEmpty()) {
            sql.append(" AND fase = ?");
        }

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (titulo != null && !titulo.isEmpty()) {
                ps.setString(paramIndex++, "%" + titulo + "%");
            }
            if (fase != null && !fase.isEmpty()) {
                ps.setString(paramIndex, fase);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                lista.add(e);
            }

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al buscar: " + ex.getMessage(), ex);
        }

        return lista;
    }

    /**
     * Busca solo errores resueltos (SOLUCIONADO o CERRADO) con filtros opcionales.
     */
    public List<ErrorTicket> buscarResueltos(String titulo, String severidad,
                                              String resueltoPor, Timestamp desde, Timestamp hasta) {
        List<ErrorTicket> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT id, titulo, descripcion, severidad, fase, fecha, solucion, " +
            "resuelto_por, fecha_solucion, captura_error, pasos_reproducir, descripcion_solucion " +
            "FROM errores WHERE fase IN ('SOLUCIONADO', 'CERRADO')");

        if (titulo != null && !titulo.isEmpty()) {
            sql.append(" AND titulo LIKE ?");
        }
        if (severidad != null && !severidad.isEmpty()) {
            sql.append(" AND severidad = ?");
        }
        if (resueltoPor != null && !resueltoPor.isEmpty()) {
            sql.append(" AND resuelto_por = ?");
        }
        if (desde != null) {
            sql.append(" AND fecha_solucion >= ?");
        }
        if (hasta != null) {
            sql.append(" AND fecha_solucion <= ?");
        }
        sql.append(" ORDER BY fecha_solucion DESC");

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            if (titulo != null && !titulo.isEmpty()) {
                ps.setString(idx++, "%" + titulo + "%");
            }
            if (severidad != null && !severidad.isEmpty()) {
                ps.setString(idx++, severidad);
            }
            if (resueltoPor != null && !resueltoPor.isEmpty()) {
                ps.setString(idx++, resueltoPor);
            }
            if (desde != null) {
                ps.setTimestamp(idx++, desde);
            }
            if (hasta != null) {
                ps.setTimestamp(idx++, hasta);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                lista.add(e);
            }

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al buscar resueltos: " + ex.getMessage(), ex);
        }

        return lista;
    }

    /**
     * Obtiene la lista de usuarios que han resuelto errores.
     */
    public List<String> obtenerResolutores() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT resuelto_por FROM errores WHERE resuelto_por IS NOT NULL ORDER BY resuelto_por";

        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(rs.getString("resuelto_por"));
            }

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al obtener resolutores: " + ex.getMessage(), ex);
        }

        return lista;
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
            throw new ErrorDaoException("Error al eliminar: " + ex.getMessage(), ex);
        }
    }
}
