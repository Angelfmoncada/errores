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
 * DAO (Data Access Object) para operaciones CRUD de errores.
 * Centraliza todo el acceso a la tabla 'errores' de la base de datos.
 */
public class ErrorDAO {

    private static final String COLUMNAS_SELECT =
        "id, titulo, descripcion, severidad, fase, fecha, solucion, " +
        "registrado_por, proceso_por, resuelto_por, fecha_solucion, captura_error, pasos_reproducir, descripcion_solucion";

    /**
     * Mapea una fila del ResultSet a un objeto ErrorTicket.
     * Metodo centralizado para evitar duplicacion de codigo.
     */
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

    /**
     * Ejecuta un SELECT y mapea los resultados a una lista de ErrorTicket.
     */
    private List<ErrorTicket> ejecutarConsulta(String sql, ParametrosSetter setter) {
        List<ErrorTicket> lista = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (setter != null) {
                setter.setParametros(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFila(rs));
                }
            }
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error en consulta: " + ex.getMessage(), ex);
        }
        return lista;
    }

    /**
     * Interfaz funcional para asignar parametros a un PreparedStatement.
     */
    @FunctionalInterface
    private interface ParametrosSetter {
        void setParametros(PreparedStatement ps) throws SQLException;
    }

    /**
     * Inserta un nuevo error en la base de datos.
     */
    public void insertar(ErrorTicket e) {
        String sql = "INSERT INTO errores (titulo, descripcion, severidad, fase, solucion, captura_error, pasos_reproducir, registrado_por) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getSeveridad().name());
            ps.setString(4, e.getFase().name());
            ps.setString(5, e.getSolucion());
            ps.setString(6, e.getCapturaError());
            ps.setString(7, e.getPasosReproducir());
            ps.setString(8, e.getRegistradoPor());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al insertar: " + ex.getMessage(), ex);
        }
    }

    /**
     * Busca un error por su ID.
     * @return el ErrorTicket encontrado o null si no existe
     */
    public ErrorTicket buscarPorId(int id) {
        String sql = "SELECT " + COLUMNAS_SELECT + " FROM errores WHERE id = ?";
        List<ErrorTicket> resultado = ejecutarConsulta(sql, ps -> ps.setInt(1, id));
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /**
     * Devuelve todos los errores registrados en la base de datos.
     */
    public List<ErrorTicket> obtenerTodos() {
        String sql = "SELECT " + COLUMNAS_SELECT + " FROM errores ORDER BY fecha DESC";
        return ejecutarConsulta(sql, null);
    }

    /**
     * Actualiza un error existente (fase y solucion).
     */
    public void actualizar(ErrorTicket e) {
        String sql = "UPDATE errores SET fase = ?, solucion = ?, proceso_por = ?, resuelto_por = ?, " +
                     "fecha_solucion = ?, descripcion_solucion = ? WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getFase().name());
            ps.setString(2, e.getSolucion());
            ps.setString(3, e.getProcesoPor());
            ps.setString(4, e.getResueltoPor());
            ps.setTimestamp(5, e.getFechaSolucion());
            ps.setString(6, e.getDescripcionSolucion());
            ps.setInt(7, e.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al actualizar: " + ex.getMessage(), ex);
        }
    }

    /**
     * Busca errores por titulo y/o fase.
     */
    public List<ErrorTicket> buscar(String titulo, String fase) {
        StringBuilder sql = new StringBuilder("SELECT " + COLUMNAS_SELECT + " FROM errores WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (titulo != null && !titulo.isEmpty()) {
            sql.append(" AND titulo LIKE ?");
            params.add("%" + titulo + "%");
        }
        if (fase != null && !fase.isEmpty()) {
            sql.append(" AND fase = ?");
            params.add(fase);
        }
        sql.append(" ORDER BY fecha DESC");

        return ejecutarConsulta(sql.toString(), ps -> {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        });
    }

    /**
     * Busca solo errores resueltos (SOLUCIONADO o CERRADO) con filtros opcionales.
     */
    public List<ErrorTicket> buscarResueltos(String titulo, String severidad,
                                              String resueltoPor, Timestamp desde, Timestamp hasta) {
        StringBuilder sql = new StringBuilder(
            "SELECT " + COLUMNAS_SELECT + " FROM errores WHERE fase IN ('SOLUCIONADO', 'CERRADO')");
        List<Object> params = new ArrayList<>();

        if (titulo != null && !titulo.isEmpty()) {
            sql.append(" AND titulo LIKE ?");
            params.add("%" + titulo + "%");
        }
        if (severidad != null && !severidad.isEmpty()) {
            sql.append(" AND severidad = ?");
            params.add(severidad);
        }
        if (resueltoPor != null && !resueltoPor.isEmpty()) {
            sql.append(" AND resuelto_por = ?");
            params.add(resueltoPor);
        }
        if (desde != null) {
            sql.append(" AND fecha_solucion >= ?");
            params.add(desde);
        }
        if (hasta != null) {
            sql.append(" AND fecha_solucion <= ?");
            params.add(hasta);
        }
        sql.append(" ORDER BY fecha_solucion DESC");

        return ejecutarConsulta(sql.toString(), ps -> {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        });
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
     * Actualiza la captura de pantalla de un error existente.
     */
    public void actualizarCaptura(int id, String rutaCaptura) {
        String sql = "UPDATE errores SET captura_error = ? WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rutaCaptura);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ErrorDaoException("Error al actualizar captura: " + ex.getMessage(), ex);
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
            throw new ErrorDaoException("Error al eliminar: " + ex.getMessage(), ex);
        }
    }
}
