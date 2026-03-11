package Servicio;

import Modelo.ErrorTicket;
import Dao.ErrorDAO;

import java.util.List;

/**
 * Clase de servicio que gestiona los errores.
 * Hace de intermediario entre la GUI y la base de datos.
 */
public class GestorErrores {

    private final ErrorDAO errorDAO;

    // Constructor
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
     * Se usa para llenar la tabla en la GUI.
     * @return Lista de errores
     */
    public List<ErrorTicket> obtenerTodosErrores() {
        return errorDAO.obtenerTodos();
    }

    /**
     * Actualiza un error existente en la base de datos.
     * Se usa para modificar la fase y agregar solución.
     * @param e Objeto ErrorTicket con los datos actualizados
     */
    public void actualizarError(ErrorTicket e) {
        errorDAO.actualizar(e);
    }

    /**
     * Elimina un error de la base de datos por su ID.
     */
    public void eliminarError(int id) {
        errorDAO.eliminar(id);
    }
}

