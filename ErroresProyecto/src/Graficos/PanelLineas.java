package Graficos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Panel que dibuja un gráfico de líneas usando Java2D.
 */
public class PanelLineas extends JPanel {

    private Map<String, Integer> datos;
    private String titulo;

    public PanelLineas(String titulo) {
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
        int maxValor = datos.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        // Ejes
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(margenIzq, margenSup, margenIzq, alto - margenInf);
        g2d.drawLine(margenIzq, alto - margenInf, ancho - margenDer, alto - margenInf);

        // Líneas de referencia Y
        g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));
        for (int i = 0; i <= 4; i++) {
            int valor = maxValor * i / 4;
            int y = alto - margenInf - (areaAlto * i / 4);
            g2d.setColor(new Color(220, 220, 220));
            g2d.drawLine(margenIzq + 1, y, ancho - margenDer, y);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString(String.valueOf(valor), margenIzq - 30, y + 5);
        }

        // Calcular puntos
        List<String> claves = new ArrayList<>(datos.keySet());
        List<Integer> valores = new ArrayList<>(datos.values());
        int numPuntos = claves.size();

        if (numPuntos == 1) {
            // Un solo punto: dibujar como punto centrado
            int x = margenIzq + areaAncho / 2;
            int y = alto - margenInf - (int) ((double) valores.get(0) / maxValor * areaAlto);
            g2d.setColor(new Color(41, 128, 185));
            g2d.fillOval(x - 5, y - 5, 10, 10);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString(String.valueOf(valores.get(0)), x + 8, y - 3);
            g2d.drawString(claves.get(0), x - 15, alto - margenInf + 15);
            return;
        }

        int espacioX = areaAncho / (numPuntos - 1);

        // Área bajo la línea (relleno semi-transparente)
        int[] xPoints = new int[numPuntos + 2];
        int[] yPoints = new int[numPuntos + 2];
        for (int i = 0; i < numPuntos; i++) {
            xPoints[i] = margenIzq + i * espacioX;
            yPoints[i] = alto - margenInf - (int) ((double) valores.get(i) / maxValor * areaAlto);
        }
        xPoints[numPuntos] = margenIzq + (numPuntos - 1) * espacioX;
        yPoints[numPuntos] = alto - margenInf;
        xPoints[numPuntos + 1] = margenIzq;
        yPoints[numPuntos + 1] = alto - margenInf;

        g2d.setColor(new Color(41, 128, 185, 50));
        g2d.fillPolygon(xPoints, yPoints, numPuntos + 2);

        // Línea
        g2d.setColor(new Color(41, 128, 185));
        g2d.setStroke(new BasicStroke(2.5f));
        for (int i = 0; i < numPuntos - 1; i++) {
            g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }

        // Puntos y etiquetas
        g2d.setStroke(new BasicStroke(1f));
        for (int i = 0; i < numPuntos; i++) {
            // Punto
            g2d.setColor(new Color(41, 128, 185));
            g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(xPoints[i] - 2, yPoints[i] - 2, 4, 4);

            // Valor sobre el punto
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
            g2d.drawString(String.valueOf(valores.get(i)), xPoints[i] + 5, yPoints[i] - 8);

            // Etiqueta en eje X
            g2d.setFont(new Font("Trebuchet MS", Font.PLAIN, 9));
            FontMetrics fm = g2d.getFontMetrics();
            String etiqueta = claves.get(i);
            // Rotar etiqueta si hay muchos puntos
            if (numPuntos > 6) {
                Graphics2D g2dRot = (Graphics2D) g2d.create();
                g2dRot.rotate(-Math.PI / 4, xPoints[i], alto - margenInf + 12);
                g2dRot.drawString(etiqueta, xPoints[i], alto - margenInf + 12);
                g2dRot.dispose();
            } else {
                g2d.drawString(etiqueta, xPoints[i] - fm.stringWidth(etiqueta) / 2, alto - margenInf + 15);
            }
        }
    }
}
