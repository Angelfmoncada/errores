package Modelo;

/**
 * Singleton thread-safe que almacena la sesion del usuario autenticado.
 * Permite acceder al username y rol desde cualquier parte de la aplicacion.
 */
public class SesionUsuario {

    private static volatile SesionUsuario instancia;

    private String username;
    private String rol;

    private SesionUsuario() {
    }

    /**
     * Retorna la instancia unica de SesionUsuario (double-checked locking).
     */
    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            synchronized (SesionUsuario.class) {
                if (instancia == null) {
                    instancia = new SesionUsuario();
                }
            }
        }
        return instancia;
    }

    /**
     * Inicia la sesion con el usuario y rol proporcionados.
     */
    public void iniciarSesion(String username, String rol) {
        this.username = username;
        this.rol = rol;
    }

    /**
     * Cierra la sesion actual limpiando los datos del usuario.
     */
    public void cerrarSesion() {
        this.username = null;
        this.rol = null;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    /**
     * Verifica si hay un usuario autenticado.
     */
    public boolean estaAutenticado() {
        return username != null;
    }
}
