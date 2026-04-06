package Modelo;

/**
 * Representacion de los niveles de severidad de un error.
 * Cada severidad tiene una etiqueta legible para la interfaz.
 */
public enum Severidad {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta"),
    CRITICA("Critica");

    private final String etiqueta;

    Severidad(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Retorna la etiqueta legible para la interfaz de usuario.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Obtiene la Severidad correspondiente al indice del ComboBox.
     */
    public static Severidad fromIndex(int index) {
        Severidad[] valores = values();
        if (index < 0 || index >= valores.length) {
            return BAJA;
        }
        return valores[index];
    }

    /**
     * Retorna un arreglo con las etiquetas para poblar ComboBox.
     */
    public static String[] getEtiquetas() {
        Severidad[] valores = values();
        String[] etiquetas = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            etiquetas[i] = valores[i].etiqueta;
        }
        return etiquetas;
    }
}
