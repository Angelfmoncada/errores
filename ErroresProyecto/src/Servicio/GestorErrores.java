package Servicio;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.SesionUsuario;
import Dao.ErrorDAO;

import java.sql.Timestamp;
import java.util.List;

/**
 * Clase de servicio que gestiona los errores.
 * Hace de intermediario entre la GUI y la capa de datos,
 * aplicando logica de negocio antes de las operaciones.
 */
public class GestorErrores {

    private final ErrorDAO errorDAO;

    public GestorErrores() {
        this.errorDAO = new ErrorDAO();
    }

    /**
     * Registra un nuevo error en la base de datos.
     */
    public void registrarError(ErrorTicket e) {
        errorDAO.insertar(e);
    }

    /**
     * Busca un error por su ID.
     * @return el ErrorTicket encontrado o null si no existe
     */
    public ErrorTicket buscarPorId(int id) {
        return errorDAO.buscarPorId(id);
    }

    /**
     * Devuelve una lista con todos los errores registrados.
     */
    public List<ErrorTicket> obtenerTodosErrores() {
        return errorDAO.obtenerTodos();
    }

    /**
     * Actualiza un error existente en la base de datos.
     * Si la fase es SOLUCIONADO, registra automaticamente quien lo resolvio y cuando.
     * Si se revierte a una fase anterior, limpia los datos de resolucion.
     */
    public void actualizarError(ErrorTicket e) {
        if (e.getFase() == Fase.SOLUCIONADO && e.getResueltoPor() == null) {
            e.setResueltoPor(SesionUsuario.getInstancia().getUsername());
            e.setFechaSolucion(new Timestamp(System.currentTimeMillis()));
        } else if (e.getFase() != Fase.SOLUCIONADO && e.getFase() != Fase.CERRADO) {
            e.setResueltoPor(null);
            e.setFechaSolucion(null);
        }
        errorDAO.actualizar(e);
    }

    /**
     * Busca errores por titulo y/o fase.
     */
    public List<ErrorTicket> buscarErrores(String titulo, String fase) {
        return errorDAO.buscar(titulo, fase);
    }

    /**
     * Busca errores resueltos con filtros avanzados.
     */
    public List<ErrorTicket> buscarResueltos(String titulo, String severidad,
                                              String resueltoPor, Timestamp desde, Timestamp hasta) {
        return errorDAO.buscarResueltos(titulo, severidad, resueltoPor, desde, hasta);
    }

    /**
     * Elimina un error de la base de datos por su ID.
     */
    public void eliminarError(int id) {
        errorDAO.eliminar(id);
    }
}
