package Modelo;

/**
 * Singleton que almacena la sesión del usuario autenticado.
 * Permite acceder al username y rol desde cualquier parte de la app.
 */
public class SesionUsuario {

    private static SesionUsuario instancia;

    private String username;
    private String rol;

    private SesionUsuario() {
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void iniciarSesion(String username, String rol) {
        this.username = username;
        this.rol = rol;
    }

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

    public boolean estaAutenticado() {
        return username != null;
    }
}
