/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Mass
 */
public class Icono {
        
    public static void setLogotipo(JFrame frame) {
        try {
            // Usamos la ruta que mencionaste anteriormente
            Image icono = new ImageIcon(Icono.class.getResource("/Imagenes/Icono_Interfaz.png")).getImage();
            frame.setIconImage(icono);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }
    
}
