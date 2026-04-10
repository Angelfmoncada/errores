package Utilidades;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Utilidad para cargar y escalar imagenes en componentes JLabel.
 */
public class Imagen {

    private static final Logger logger = Logger.getLogger(Imagen.class.getName());

    public static void ponerImagenEnLabel(JLabel label, String ruta) {
        java.net.URL recurso = Imagen.class.getResource(ruta);
        if (recurso == null) {
            logger.log(Level.WARNING, "No se encontro la imagen: {0}", ruta);
            return;
        }

        ImageIcon originalIcon = new ImageIcon(recurso);

        int ancho = label.getWidth() > 0 ? label.getWidth() : label.getPreferredSize().width;
        int alto = label.getHeight() > 0 ? label.getHeight() : label.getPreferredSize().height;
        if (ancho <= 0 || alto <= 0) {
            label.setIcon(originalIcon);
            return;
        }

        Image escalada = originalIcon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        Icon iconoFinal = new ImageIcon(escalada);
        label.setIcon(iconoFinal);
    }
}
