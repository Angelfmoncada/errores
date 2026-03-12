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
        java.net.URL recurso = Imagen.class.getResource(ruta);
        if (recurso == null) {
            System.err.println("No se encontró la imagen: " + ruta);
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
