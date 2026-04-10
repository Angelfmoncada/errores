package Modelo;

/**
 * Representacion de las fases del ciclo de vida de un error.
 * Cada fase tiene una etiqueta legible para mostrar en la interfaz.
 */
public enum Fase {
    REGISTRADO("Registrado"),
    PROCESO("Proceso"),
    SOLUCIONADO("Solucionado"),
    CERRADO("Cerrado");

    private final String etiqueta;

    Fase(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna la etiqueta legible para la interfaz de usuario.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Obtiene la Fase correspondiente al indice del ComboBox.
     * Elimina la duplicacion de if-else en las vistas.
     */
    public static Fase fromIndex(int index) {
        Fase[] valores = values();
        if (index < 0 || index >= valores.length) {
            return REGISTRADO;
        }
        return valores[index];
    }

    /**
     * Retorna un arreglo con las etiquetas para poblar ComboBox.
     */
    public static String[] getEtiquetas() {
        Fase[] valores = values();
        String[] etiquetas = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            etiquetas[i] = valores[i].etiqueta;
        }
        return etiquetas;
    }
}
