package Servicio;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.SesionUsuario;
import Dao.ErrorDAO;

import java.sql.Timestamp;
import java.util.List;

/**
 * Clase de servicio que gestiona los errores.
 * Hace de intermediario entre la GUI y la base de datos.
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
     * Devuelve una lista con todos los errores registrados.
     */
    public List<ErrorTicket> obtenerTodosErrores() {
        return errorDAO.obtenerTodos();
    }

    /**
     * Actualiza un error existente en la base de datos.
     * Si la fase es SOLUCIONADO, registra automáticamente quién lo resolvió y cuándo.
     */
    public void actualizarError(ErrorTicket e) {
        // Auto-poblar datos de resolución
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
     * Elimina un error de la base de datos por su ID.
     */
    public void eliminarError(int id) {
        errorDAO.eliminar(id);
    }
}
