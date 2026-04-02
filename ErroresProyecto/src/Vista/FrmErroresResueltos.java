package Vista;

import Dao.ErrorDAO;
import Modelo.ErrorTicket;
import Modelo.ErrorDaoException;

import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Módulo dedicado para visualizar errores resueltos (SOLUCIONADO/CERRADO).
 * Muestra quién resolvió, cuándo y cómo.
 */
public class FrmErroresResueltos extends JFrame {

    private final ErrorDAO errorDAO;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBuscarTitulo;
    private JComboBox<String> cboSeveridad;
    private JComboBox<String> cboResolutor;
    private JTextField txtDesde;
    private JTextField txtHasta;
    private JTextArea txtDetalleSolucion;

    public FrmErroresResueltos() {
        this.errorDAO = new ErrorDAO();
        inicializarUI();
        cargarResolutores();
        cargarTabla();
    }

    private void inicializarUI() {
        setTitle("Errores Resueltos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        Utilidades.Icono.setLogotipo(this);
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(new Color(204, 204, 204));

        // === Panel de filtros ===
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelFiltros.setBackground(new Color(0, 0, 102));

        Font fuenteLabel = new Font("Trebuchet MS", Font.BOLD, 12);
        Color colorLabel = Color.WHITE;

        JLabel lbl1 = new JLabel("Título:");
        lbl1.setFont(fuenteLabel);
        lbl1.setForeground(colorLabel);
        txtBuscarTitulo = new JTextField(12);

        JLabel lbl2 = new JLabel("Severidad:");
        lbl2.setFont(fuenteLabel);
        lbl2.setForeground(colorLabel);
        cboSeveridad = new JComboBox<>(new String[]{"Todas", "BAJA", "MEDIA", "ALTA", "CRITICA"});

        JLabel lbl3 = new JLabel("Resuelto por:");
        lbl3.setFont(fuenteLabel);
        lbl3.setForeground(colorLabel);
        cboResolutor = new JComboBox<>(new String[]{"Todos"});

        JLabel lbl4 = new JLabel("Desde:");
        lbl4.setFont(fuenteLabel);
        lbl4.setForeground(colorLabel);
        txtDesde = new JTextField(8);
        txtDesde.setToolTipText("YYYY-MM-DD");

        JLabel lbl5 = new JLabel("Hasta:");
        lbl5.setFont(fuenteLabel);
        lbl5.setForeground(colorLabel);
        txtHasta = new JTextField(8);
        txtHasta.setToolTipText("YYYY-MM-DD");

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(new Color(46, 204, 113));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
        btnFiltrar.addActionListener(e -> filtrar());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
        btnLimpiar.addActionListener(e -> {
            txtBuscarTitulo.setText("");
            cboSeveridad.setSelectedIndex(0);
            cboResolutor.setSelectedIndex(0);
            txtDesde.setText("");
            txtHasta.setText("");
            cargarTabla();
        });

        panelFiltros.add(lbl1);
        panelFiltros.add(txtBuscarTitulo);
        panelFiltros.add(lbl2);
        panelFiltros.add(cboSeveridad);
        panelFiltros.add(lbl3);
        panelFiltros.add(cboResolutor);
        panelFiltros.add(lbl4);
        panelFiltros.add(txtDesde);
        panelFiltros.add(lbl5);
        panelFiltros.add(txtHasta);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiar);

        add(panelFiltros, BorderLayout.NORTH);

        // === Tabla ===
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Título", "Severidad", "Fecha Registro", "Resuelto Por",
                         "Fecha Solución", "Solución"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Trebuchet MS", Font.BOLD, 12));
        tabla.setRowHeight(25);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarDetalle());

        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);

        // === Panel inferior: detalle + botones ===
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBackground(new Color(204, 204, 204));

        // Detalle de solución
        JPanel panelDetalle = new JPanel(new BorderLayout(5, 5));
        panelDetalle.setBackground(new Color(204, 204, 204));
        panelDetalle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblDetalle = new JLabel("Descripción detallada de la solución:");
        lblDetalle.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        panelDetalle.add(lblDetalle, BorderLayout.NORTH);

        txtDetalleSolucion = new JTextArea(4, 40);
        txtDetalleSolucion.setEditable(false);
        txtDetalleSolucion.setLineWrap(true);
        txtDetalleSolucion.setWrapStyleWord(true);
        txtDetalleSolucion.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
        panelDetalle.add(new JScrollPane(txtDetalleSolucion), BorderLayout.CENTER);

        panelInferior.add(panelDetalle, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        panelBotones.setBackground(new Color(0, 0, 102));

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(255, 51, 51));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        btnRegresar.addActionListener(e -> {
            new FrmPrincipal().setVisible(true);
            this.dispose();
        });

        panelBotones.add(btnRegresar);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarResolutores() {
        try {
            List<String> resolutores = errorDAO.obtenerResolutores();
            for (String r : resolutores) {
                cboResolutor.addItem(r);
            }
        } catch (ErrorDaoException ex) {
            // Ignorar - el combo ya tiene "Todos"
        }
    }

    private void cargarTabla() {
        filtrarConParametros(null, null, null, null, null);
    }

    private void filtrar() {
        String titulo = txtBuscarTitulo.getText().trim();
        if (titulo.isEmpty()) titulo = null;

        String severidad = (String) cboSeveridad.getSelectedItem();
        if ("Todas".equals(severidad)) severidad = null;

        String resolutor = (String) cboResolutor.getSelectedItem();
        if ("Todos".equals(resolutor)) resolutor = null;

        Timestamp desde = parsearFecha(txtDesde.getText().trim());
        Timestamp hasta = parsearFecha(txtHasta.getText().trim());

        filtrarConParametros(titulo, severidad, resolutor, desde, hasta);
    }

    private void filtrarConParametros(String titulo, String severidad, String resolutor,
                                       Timestamp desde, Timestamp hasta) {
        modeloTabla.setRowCount(0);
        try {
            List<ErrorTicket> errores = errorDAO.buscarResueltos(titulo, severidad, resolutor, desde, hasta);
            for (ErrorTicket e : errores) {
                modeloTabla.addRow(new Object[]{
                    e.getId(),
                    e.getTitulo(),
                    e.getSeveridad(),
                    e.getFecha(),
                    e.getResueltoPor(),
                    e.getFechaSolucion(),
                    e.getSolucion()
                });
            }

            if (errores.isEmpty()) {
                txtDetalleSolucion.setText("No se encontraron errores resueltos con los filtros seleccionados.");
            }
        } catch (ErrorDaoException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar errores resueltos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        int id = (int) tabla.getValueAt(fila, 0);
        try {
            List<ErrorTicket> errores = errorDAO.buscarResueltos(null, null, null, null, null);
            for (ErrorTicket e : errores) {
                if (e.getId() == id) {
                    StringBuilder detalle = new StringBuilder();
                    if (e.getDescripcionSolucion() != null && !e.getDescripcionSolucion().isEmpty()) {
                        detalle.append("DESCRIPCIÓN DE LA SOLUCIÓN:\n");
                        detalle.append(e.getDescripcionSolucion());
                    }
                    if (e.getPasosReproducir() != null && !e.getPasosReproducir().isEmpty()) {
                        if (detalle.length() > 0) detalle.append("\n\n");
                        detalle.append("PASOS PARA REPRODUCIR:\n");
                        detalle.append(e.getPasosReproducir());
                    }
                    if (e.getSolucion() != null && !e.getSolucion().isEmpty()) {
                        if (detalle.length() > 0) detalle.append("\n\n");
                        detalle.append("SOLUCIÓN BREVE:\n");
                        detalle.append(e.getSolucion());
                    }
                    txtDetalleSolucion.setText(detalle.length() > 0 ? detalle.toString() : "Sin descripción detallada.");
                    txtDetalleSolucion.setCaretPosition(0);
                    break;
                }
            }
        } catch (ErrorDaoException ex) {
            txtDetalleSolucion.setText("Error al cargar detalle.");
        }
    }

    private Timestamp parsearFecha(String texto) {
        if (texto == null || texto.isEmpty()) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date fecha = sdf.parse(texto);
            return new Timestamp(fecha.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
