package Utilidades;

import Conexion.ConexionBD;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import javax.imageio.ImageIO;

/**
 * Genera imagenes de captura simuladas para cada error y las asocia en la BD.
 * Uso: java -cp "build/classes;lib/mysql-connector-j-9.6.0.jar" Utilidades.GenerarCapturas
 */
public class GenerarCapturas {

    private static final String DIR = "capturas/";
    private static final Color[] COLORES_FONDO = {
        new Color(255, 235, 238), // rojo claro
        new Color(255, 243, 224), // naranja claro
        new Color(227, 242, 253), // azul claro
        new Color(232, 245, 233), // verde claro
        new Color(243, 229, 245), // morado claro
        new Color(255, 249, 196), // amarillo claro
        new Color(255, 235, 238), // rojo claro
        new Color(224, 247, 250), // cyan claro
    };

    public static void main(String[] args) throws Exception {
        new File(DIR).mkdirs();

        // Obtener errores de la BD
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, titulo, descripcion, severidad, fase FROM errores ORDER BY id")) {

            int i = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String desc = rs.getString("descripcion");
                String sev = rs.getString("severidad");
                String fase = rs.getString("fase");

                String archivo = generarImagen(id, titulo, desc, sev, fase, COLORES_FONDO[i % COLORES_FONDO.length]);

                // Actualizar la BD con la ruta
                try (PreparedStatement ps = con.prepareStatement("UPDATE errores SET captura_error = ? WHERE id = ?")) {
                    ps.setString(1, archivo);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }
                System.out.println("Captura generada para error #" + id + ": " + archivo);
                i++;
            }
        }
        System.out.println("Todas las capturas generadas.");
    }

    private static String generarImagen(int id, String titulo, String desc, String sev, String fase, Color fondo) throws Exception {
        int ancho = 640;
        int alto = 400;
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fondo
        g.setColor(fondo);
        g.fillRect(0, 0, ancho, alto);

        // Barra superior
        Color colorSev = getColorSeveridad(sev);
        g.setColor(colorSev);
        g.fillRect(0, 0, ancho, 60);

        // Titulo en barra
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g.drawString("Error #" + id + " - " + titulo, 20, 38);

        // Badge severidad
        g.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g.drawString("[" + sev + "]", ancho - 100, 38);

        // Fase
        g.setColor(new Color(60, 60, 60));
        g.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.drawString("Fase: " + fase, 20, 90);

        // Linea separadora
        g.setColor(new Color(200, 200, 200));
        g.drawLine(20, 100, ancho - 20, 100);

        // Descripcion
        g.setColor(new Color(33, 33, 33));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dibujarTextoMultilinea(g, "Descripcion:", 20, 125, ancho - 40);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dibujarTextoMultilinea(g, desc, 20, 145, ancho - 40);

        // Cuadro simulando error en pantalla
        g.setColor(new Color(255, 255, 255, 200));
        g.fillRoundRect(40, 220, ancho - 80, 130, 15, 15);
        g.setColor(colorSev);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(40, 220, ancho - 80, 130, 15, 15);

        // Icono de error
        g.setColor(colorSev);
        g.setFont(new Font("Segoe UI", Font.BOLD, 28));
        g.drawString("!", 65, 280);
        g.fillOval(55, 255, 30, 30);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g.drawString("!", 66, 280);

        // Texto del error
        g.setColor(new Color(33, 33, 33));
        g.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.drawString("Error detectado en el sistema", 100, 270);
        g.setFont(new Font("Consolas", Font.PLAIN, 11));
        String truncDesc = desc.length() > 70 ? desc.substring(0, 70) + "..." : desc;
        g.drawString(truncDesc, 100, 295);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g.setColor(new Color(120, 120, 120));
        g.drawString("Severidad: " + sev + "  |  Fase: " + fase + "  |  ID: " + id, 100, 320);

        // Timestamp
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        g.drawString("Captura generada automaticamente - Gestor de Errores", 20, alto - 15);

        g.dispose();

        String nombre = DIR + "error_" + id + "_captura.png";
        ImageIO.write(img, "png", new File(nombre));
        return nombre;
    }

    private static Color getColorSeveridad(String sev) {
        switch (sev) {
            case "CRITICA": return new Color(198, 40, 40);
            case "ALTA": return new Color(230, 126, 34);
            case "MEDIA": return new Color(41, 128, 185);
            case "BAJA": return new Color(46, 125, 50);
            default: return new Color(100, 100, 100);
        }
    }

    private static void dibujarTextoMultilinea(Graphics2D g, String texto, int x, int y, int maxAncho) {
        FontMetrics fm = g.getFontMetrics();
        String[] palabras = texto.split(" ");
        StringBuilder linea = new StringBuilder();
        int yActual = y;
        for (String palabra : palabras) {
            String prueba = linea.length() > 0 ? linea + " " + palabra : palabra;
            if (fm.stringWidth(prueba) > maxAncho) {
                g.drawString(linea.toString(), x, yActual);
                linea = new StringBuilder(palabra);
                yActual += fm.getHeight();
            } else {
                linea = new StringBuilder(prueba);
            }
        }
        if (linea.length() > 0) {
            g.drawString(linea.toString(), x, yActual);
        }
    }
}
