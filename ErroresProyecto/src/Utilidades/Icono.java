package Utilidades;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Utilidad para establecer el icono de la aplicacion en los JFrame.
 */
public class Icono {

    private static final Logger logger = Logger.getLogger(Icono.class.getName());

    public static void setLogotipo(JFrame frame) {
        try {
            Image icono = new ImageIcon(Icono.class.getResource("/Imagenes/Icono_Interfaz.png")).getImage();
            frame.setIconImage(icono);
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo cargar el icono: {0}", e.getMessage());
        }
    }
}
