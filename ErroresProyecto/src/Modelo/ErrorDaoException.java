package Modelo;

/**
 * Excepción para errores en la capa de acceso a datos.
 * Permite propagar errores de BD a las capas superiores.
 */
public class ErrorDaoException extends RuntimeException {

    public ErrorDaoException(String mensaje) {
        super(mensaje);
    }

    public ErrorDaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
