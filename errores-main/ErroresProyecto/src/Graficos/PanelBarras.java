package Graficos;

import java.awt.*;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Panel que dibuja un gráfico de barras verticales usando Java2D.
 */
@SuppressWarnings("this-escape")
public class PanelBarras extends JPanel {

    private static final long serialVersionUID = 1L;
    private transient Map<String, Integer> datos;
    private String titulo;

    private static final Color[] COLORES = {
        new Color(41, 128, 185),   // Azul
        new Color(231, 76, 60),    // Rojo
        new Color(46, 204, 113),   // Verde
        new Color(241, 196, 15),   // Amarillo
        new Color(155, 89, 182),   // Morado
        new Color(230, 126, 34)    // Naranja
    };

    public PanelBarras(String titulo) {
        this.titulo = titulo;
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }

    public void setDatos(Map<String, Integer> datos) {
        this.datos = datos;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int margenIzq = 60;
        int margenDer = 20;
        int margenSup = 40;
        int margenInf = 60;

        // Título
        g2d.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        g2d.setColor(new Color(0, 0, 102));
        FontMetrics fmTitulo = g2d.getFontMetrics();
        g2d.drawString(titulo, (ancho - fmTitulo.stringWidth(titulo)) / 2, 25);

        if (datos == null || datos.isEmpty()) {
            g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
            g2d.setColor(Color.GRAY);
            g2d.drawString("Sin datos disponibles", ancho / 2 - 60, alto / 2);
            return;
        }

        int areaAncho = ancho - margenIzq - margenDer;
        int areaAlto = alto - margenSup - margenInf;
        int numBarras = datos.size();
        int anchoBarra = Math.min(60, areaAncho / (numBarras * 2));
        int espacio = (areaAncho - numBarras * anchoBarra) / (numBarras + 1);

        // Encontrar valor máximo
        int maxValor = datos.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        // Ejes
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(margenIzq, margenSup, margenIzq, alto - margenInf);
        g2d.drawLine(margenIzq, alto - margenInf, ancho - margenDer, alto - margenInf);

        // Líneas de referencia y etiquetas del eje Y
        g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));
        for (int i = 0; i <= 4; i++) {
            int valor = maxValor * i / 4;
            int y = alto - margenInf - (areaAlto * i / 4);
            g2d.setColor(new Color(220, 220, 220));
            g2d.drawLine(margenIzq + 1, y, ancho - margenDer, y);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString(String.valueOf(valor), margenIzq - 30, y + 5);
        }

        // Barras
        int i = 0;
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            int x = margenIzq + espacio + i * (anchoBarra + espacio);
            int alturaBarra = (int) ((double) entry.getValue() / maxValor * areaAlto);
            int y = alto - margenInf - alturaBarra;

            Color color = COLORES[i % COLORES.length];
            g2d.setColor(color);
            g2d.fillRoundRect(x, y, anchoBarra, alturaBarra, 5, 5);

            // Valor sobre la barra
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Trebuchet MS", Font.BOLD, 11));
            String valor = String.valueOf(entry.getValue());
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(valor, x + (anchoBarra - fm.stringWidth(valor)) / 2, y - 5);

            // Etiqueta debajo
            g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));
            fm = g2d.getFontMetrics();
            String etiqueta = entry.getKey();
            g2d.drawString(etiqueta, x + (anchoBarra - fm.stringWidth(etiqueta)) / 2, alto - margenInf + 15);

            i++;
        }
    }
}
