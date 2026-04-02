package Graficos;

import java.awt.*;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Panel que dibuja un gráfico de pastel (pie chart) usando Java2D.
 */
public class PanelPastel extends JPanel {

    private Map<String, Integer> datos;
    private String titulo;

    private static final Color[] COLORES = {
        new Color(41, 128, 185),   // Azul
        new Color(231, 76, 60),    // Rojo
        new Color(46, 204, 113),   // Verde
        new Color(241, 196, 15),   // Amarillo
        new Color(155, 89, 182),   // Morado
        new Color(230, 126, 34)    // Naranja
    };

    public PanelPastel(String titulo) {
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

        int total = datos.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return;

        // Dimensiones del pastel
        int diametro = Math.min(ancho - 160, alto - 80);
        int x = 20;
        int y = (alto - diametro) / 2;

        // Dibujar segmentos
        int anguloInicio = 0;
        int i = 0;
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            int angulo = (int) Math.round(360.0 * entry.getValue() / total);
            // Ajustar último segmento para completar 360
            if (i == datos.size() - 1) {
                angulo = 360 - anguloInicio;
            }

            Color color = COLORES[i % COLORES.length];
            g2d.setColor(color);
            g2d.fillArc(x, y, diametro, diametro, anguloInicio, angulo);

            // Borde
            g2d.setColor(Color.WHITE);
            g2d.drawArc(x, y, diametro, diametro, anguloInicio, angulo);

            anguloInicio += angulo;
            i++;
        }

        // Leyenda
        int leyendaX = x + diametro + 20;
        int leyendaY = 50;
        g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));

        i = 0;
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            Color color = COLORES[i % COLORES.length];
            g2d.setColor(color);
            g2d.fillRect(leyendaX, leyendaY + i * 22, 12, 12);

            g2d.setColor(Color.DARK_GRAY);
            double porcentaje = 100.0 * entry.getValue() / total;
            g2d.drawString(String.format("%s: %d (%.0f%%)", entry.getKey(), entry.getValue(), porcentaje),
                          leyendaX + 18, leyendaY + i * 22 + 11);
            i++;
        }
    }
}
