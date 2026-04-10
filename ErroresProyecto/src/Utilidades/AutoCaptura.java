package Utilidades;

import Modelo.SesionUsuario;
import Dao.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Utilidad para capturar pantallas automaticamente de cada vista del sistema.
 * Uso: java -cp "build/classes;lib/mysql-connector-j-9.6.0.jar" Utilidades.AutoCaptura
 */
public class AutoCaptura {

    private static final String DIR = "creaciondedocumento/capturas/";
    private static int contador = 0;

    public static void main(String[] args) throws Exception {
        // Configurar Look and Feel
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        new File(DIR).mkdirs();

        // Login automatico
        SesionUsuario.getInstancia().iniciarSesion("admin", "admin");

        // 1. Login
        capturarVentana(new Vista.Login(), "01_Login");

        // 2. Menu Principal
        capturarVentana(new Vista.FrmPrincipal(), "02_Menu_Principal");

        // 3. Registrar Error
        capturarVentana(new Vista.FrmRegistrarError(), "03_Registrar_Error");

        // 4. Ver Errores (tabla con datos)
        capturarVentana(new Vista.frmTableErrores(), "04_Ver_Errores_Tabla");

        // 5. Dashboard
        capturarVentana(new Vista.FrmDashboard(), "05_Dashboard");

        // 6. Errores Resueltos
        capturarVentana(new Vista.FrmErroresResueltos(), "06_Errores_Resueltos");

        System.out.println("Todas las capturas guardadas en: " + DIR);
        System.exit(0);
    }

    private static void capturarVentana(JFrame frame, String nombre) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.toFront();
        });

        Thread.sleep(1500);

        SwingUtilities.invokeAndWait(() -> {
            try {
                int w = frame.getWidth();
                int h = frame.getHeight();
                if (w > 0 && h > 0) {
                    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    frame.paint(img.getGraphics());
                    File archivo = new File(DIR + nombre + ".png");
                    ImageIO.write(img, "png", archivo);
                    System.out.println("Captura guardada: " + archivo.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Error capturando " + nombre + ": " + e.getMessage());
            }
            frame.dispose();
        });

        Thread.sleep(500);
    }
}
