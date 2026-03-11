/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Mass
 */
public class Imagen {
    
    public static void ponerImagenEnLabel(JLabel label, String ruta) {
        // 1. Cargamos la imagen original
        ImageIcon originalIcon = new ImageIcon(Imagen.class.getResource(ruta));
        
        // 2. Escalamos la imagen al ancho y alto del JLabel
        // Image.SCALE_SMOOTH es clave para que no se vea pixelada
        Image escalada = originalIcon.getImage().getScaledInstance(
                label.getWidth(), 
                label.getHeight(), 
                Image.SCALE_SMOOTH
        );
        
        // 3. Creamos el nuevo icono y lo asignamos
        Icon iconoFinal = new ImageIcon(escalada);
        label.setIcon(iconoFinal);
    }
    
    
}
