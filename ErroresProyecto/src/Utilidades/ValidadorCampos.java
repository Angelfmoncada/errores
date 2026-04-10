package Utilidades;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Utilidad para validacion visual de campos de formulario.
 * Resalta campos vacios o invalidos con bordes de color.
 */
public class ValidadorCampos {

    private static final Border BORDE_ERROR = BorderFactory.createLineBorder(Tema.BORDE_ERROR, 2);
    private static final Border BORDE_OK = BorderFactory.createLineBorder(Tema.BORDE_OK, 1);
    private static final Border BORDE_NORMAL = BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180), 1);

    /**
     * Valida que un JTextField no este vacio.
     * @return true si el campo tiene contenido
     */
    public static boolean validarNoVacio(JTextField campo) {
        if (campo.getText().trim().isEmpty()) {
            campo.setBorder(BORDE_ERROR);
            campo.setToolTipText("Este campo es obligatorio");
            return false;
        }
        campo.setBorder(BORDE_OK);
        campo.setToolTipText(null);
        return true;
    }

    /**
     * Valida que un JTextArea no este vacio.
     * @return true si el area tiene contenido
     */
    public static boolean validarNoVacio(JTextArea campo) {
        if (campo.getText().trim().isEmpty()) {
            campo.setBorder(BORDE_ERROR);
            campo.setToolTipText("Este campo es obligatorio");
            return false;
        }
        campo.setBorder(BORDE_OK);
        campo.setToolTipText(null);
        return true;
    }

    /**
     * Valida la longitud maxima de un campo de texto.
     * @return true si el campo no excede el maximo
     */
    public static boolean validarLongitud(JTextField campo, int max) {
        if (campo.getText().trim().length() > max) {
            campo.setBorder(BORDE_ERROR);
            campo.setToolTipText("Maximo " + max + " caracteres");
            return false;
        }
        return true;
    }

    /**
     * Restablece el borde de un componente al estado normal.
     */
    public static void resetearBorde(JComponent componente) {
        componente.setBorder(BORDE_NORMAL);
        componente.setToolTipText(null);
    }
}
