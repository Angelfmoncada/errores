package Vista;

import Dao.EstadisticasDAO;
import Graficos.PanelBarras;
import Graficos.PanelLineas;
import Graficos.PanelPastel;
import Modelo.ErrorDaoException;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

/**
 * Dashboard con gráficos estadísticos de errores.
 * Muestra: errores por severidad, por fase, por mes, y resumen general.
 */
@SuppressWarnings("this-escape")
public class FrmDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private final transient EstadisticasDAO estadisticasDAO;
    private PanelBarras panelBarras;
    private PanelPastel panelPastel;
    private PanelLineas panelLineas;
    private JPanel panelResumen;

    public FrmDashboard() {
        this.estadisticasDAO = new EstadisticasDAO();
        inicializarUI();
        cargarDatos();
    }

    private void inicializarUI() {
        setTitle("Dashboard - Estadísticas de Errores");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        Utilidades.Icono.setLogotipo(this);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // Panel de gráficos (2x2)
        JPanel panelGraficos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelGraficos.setBackground(new Color(240, 240, 240));
        panelGraficos.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        panelBarras = new PanelBarras("Errores por Severidad");
        panelPastel = new PanelPastel("Errores por Fase");
        panelLineas = new PanelLineas("Errores por Mes");
        panelResumen = crearPanelResumen(0, 0, 0, 0);

        panelGraficos.add(panelBarras);
        panelGraficos.add(panelPastel);
        panelGraficos.add(panelLineas);
        panelGraficos.add(panelResumen);

        add(panelGraficos, BorderLayout.CENTER);

        // Panel de botones inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(0, 0, 102));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(46, 204, 113));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        btnActualizar.addActionListener(e -> cargarDatos());

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(255, 51, 51));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        btnRegresar.addActionListener(e -> {
            new FrmPrincipal().setVisible(true);
            this.dispose();
        });

        panelBotones.add(btnActualizar);
        panelBotones.add(btnRegresar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        try {
            // Gráfico de barras: errores por severidad
            Map<String, Integer> porSeveridad = estadisticasDAO.contarPorSeveridad();
            panelBarras.setDatos(porSeveridad);

            // Gráfico de pastel: errores por fase
            Map<String, Integer> porFase = estadisticasDAO.contarPorFase();
            panelPastel.setDatos(porFase);

            // Gráfico de líneas: errores por mes
            Map<String, Integer> porMes = estadisticasDAO.contarPorMes();
            panelLineas.setDatos(porMes);

            // Resumen
            int total = estadisticasDAO.contarTotal();
            int resueltos = estadisticasDAO.contarResueltos();
            double promedio = estadisticasDAO.tiempoPromedioResolucion();
            int porcentaje = total > 0 ? (resueltos * 100 / total) : 0;

            actualizarResumen(total, resueltos, promedio, porcentaje);

        } catch (ErrorDaoException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar estadísticas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelResumen(int total, int resueltos, double promedio, int porcentaje) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Resumen General", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 0, 102));

        JLabel lblTotal = new JLabel("Total Errores: " + total, SwingConstants.CENTER);
        lblTotal.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        lblTotal.setName("lblTotal");

        JLabel lblResueltos = new JLabel("Resueltos: " + resueltos, SwingConstants.CENTER);
        lblResueltos.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        lblResueltos.setForeground(new Color(46, 204, 113));
        lblResueltos.setName("lblResueltos");

        JLabel lblPromedio = new JLabel(String.format("Tiempo Promedio: %.1f días", promedio), SwingConstants.CENTER);
        lblPromedio.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        lblPromedio.setName("lblPromedio");

        JLabel lblPorcentaje = new JLabel("Tasa de Resolución: " + porcentaje + "%", SwingConstants.CENTER);
        lblPorcentaje.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
        lblPorcentaje.setForeground(porcentaje >= 50 ? new Color(46, 204, 113) : new Color(231, 76, 60));
        lblPorcentaje.setName("lblPorcentaje");

        panel.add(lblTitulo);
        panel.add(lblTotal);
        panel.add(lblResueltos);
        panel.add(lblPromedio);
        panel.add(lblPorcentaje);

        return panel;
    }

    private void actualizarResumen(int total, int resueltos, double promedio, int porcentaje) {
        for (Component comp : panelResumen.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if ("lblTotal".equals(lbl.getName())) {
                    lbl.setText("Total Errores: " + total);
                } else if ("lblResueltos".equals(lbl.getName())) {
                    lbl.setText("Resueltos: " + resueltos);
                } else if ("lblPromedio".equals(lbl.getName())) {
                    lbl.setText(String.format("Tiempo Promedio: %.1f días", promedio));
                } else if ("lblPorcentaje".equals(lbl.getName())) {
                    lbl.setText("Tasa de Resolución: " + porcentaje + "%");
                    lbl.setForeground(porcentaje >= 50 ? new Color(46, 204, 113) : new Color(231, 76, 60));
                }
            }
        }
        panelResumen.revalidate();
        panelResumen.repaint();
    }
}
