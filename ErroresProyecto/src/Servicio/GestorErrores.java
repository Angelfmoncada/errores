package Servicio;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.SesionUsuario;
import Dao.ErrorDAO;

import java.sql.Timestamp;
import java.util.List;

/**
 * Clase de servicio que gestiona los errores.
 * Registra automaticamente quien realizo cada cambio de fase:
 * - registrado_por: usuario que creo el error
 * - proceso_por: usuario que movio el error a PROCESO
 * - resuelto_por: usuario que movio el error a SOLUCIONADO
 */
public class GestorErrores {

    private final ErrorDAO errorDAO;

    public GestorErrores() {
        this.errorDAO = new ErrorDAO();
    }

    private String getUsuarioActual() {
        return SesionUsuario.getInstancia().getUsername();
    }

    /**
     * Registra un nuevo error. Asigna automaticamente el usuario actual como registrado_por.
     */
    public void registrarError(ErrorTicket e) {
        e.setRegistradoPor(getUsuarioActual());
        errorDAO.insertar(e);
    }

    public ErrorTicket buscarPorId(int id) {
        return errorDAO.buscarPorId(id);
    }

    public List<ErrorTicket> obtenerTodosErrores() {
        return errorDAO.obtenerTodos();
    }

    /**
     * Actualiza un error. Asigna automaticamente el responsable segun la fase:
     * - PROCESO: asigna proceso_por al usuario actual
     * - SOLUCIONADO: asigna resuelto_por y fecha_solucion
     * - REGISTRADO: limpia proceso_por, resuelto_por y fecha_solucion
     */
    public void actualizarError(ErrorTicket e) {
        String usuario = getUsuarioActual();

        // Obtener el estado previo para preservar datos existentes
        ErrorTicket previo = errorDAO.buscarPorId(e.getId());

        switch (e.getFase()) {
            case PROCESO:
                // Preservar quien lo registro
                if (previo != null) {
                    e.setRegistradoPor(previo.getRegistradoPor());
                }
                e.setProcesoPor(usuario);
                e.setResueltoPor(null);
                e.setFechaSolucion(null);
                break;

            case SOLUCIONADO:
                if (previo != null) {
                    e.setRegistradoPor(previo.getRegistradoPor());
                    e.setProcesoPor(previo.getProcesoPor());
                }
                if (e.getResueltoPor() == null) {
                    e.setResueltoPor(usuario);
                    e.setFechaSolucion(new Timestamp(System.currentTimeMillis()));
                }
                break;

            case CERRADO:
                if (previo != null) {
                    e.setRegistradoPor(previo.getRegistradoPor());
                    e.setProcesoPor(previo.getProcesoPor());
                    if (e.getResueltoPor() == null) {
                        e.setResueltoPor(previo.getResueltoPor());
                        e.setFechaSolucion(previo.getFechaSolucion());
                    }
                }
                break;

            default: // REGISTRADO
                if (previo != null) {
                    e.setRegistradoPor(previo.getRegistradoPor());
                }
                e.setProcesoPor(null);
                e.setResueltoPor(null);
                e.setFechaSolucion(null);
                break;
        }

        errorDAO.actualizar(e);
    }

    public List<ErrorTicket> buscarErrores(String titulo, String fase) {
        return errorDAO.buscar(titulo, fase);
    }

    public List<ErrorTicket> buscarResueltos(String titulo, String severidad,
                                              String resueltoPor, Timestamp desde, Timestamp hasta) {
        return errorDAO.buscarResueltos(titulo, severidad, resueltoPor, desde, hasta);
    }

    public void eliminarError(int id) {
        errorDAO.eliminar(id);
    }
}
