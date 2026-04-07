package Vista;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.SesionUsuario;
import Modelo.Severidad;
import Servicio.GestorErrores;
import Utilidades.ExportadorCSV;
import Utilidades.ImagenCaptura;
import Utilidades.Tema;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Vista principal para consultar, editar y eliminar errores.
 * Mejoras: colores por severidad, doble clic detalle, ordenamiento por columna,
 * barra de estado, contador de registros, exportacion CSV.
 */
public class frmTableErrores extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(frmTableErrores.class.getName());

    private final GestorErrores gestor = new GestorErrores();
    private DefaultTableModel modeloTabla;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JComboBox<String> cboFiltroFase;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnVerCaptura;
    private javax.swing.JButton btnExportarCSV;
    private javax.swing.JLabel lblDescSolucion;
    private javax.swing.JTextArea txtDescSolucion;
    private javax.swing.JScrollPane scrollDescSolucion;

    // Barra de estado
    private javax.swing.JLabel lblStatusUsuario;
    private javax.swing.JLabel lblStatusTotal;

    public frmTableErrores() {
        initComponents();
        Utilidades.Icono.setLogotipo(this);
        this.setLocationRelativeTo(null);

        crearPanelDescripcionSolucion();
        configurarEventoFase();
        crearBarraBusqueda();
        crearBarraEstado();
        configurarTabla();
        cargarTabla();
        agregarEventos();
    }

    // ==================== BARRA DE ESTADO ====================

    private void crearBarraEstado() {
        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBackground(Tema.PRIMARIO);
        barraEstado.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        String usuario = SesionUsuario.getInstancia().getUsername();
        lblStatusUsuario = new JLabel("  Usuario: " + (usuario != null ? usuario : "---"));
        lblStatusUsuario.setFont(Tema.FUENTE_STATUS);
        lblStatusUsuario.setForeground(Tema.TEXTO_CLARO);

        lblStatusTotal = new JLabel("Total: 0 errores  ");
        lblStatusTotal.setFont(Tema.FUENTE_STATUS);
        lblStatusTotal.setForeground(Tema.TEXTO_CLARO);
        lblStatusTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        barraEstado.add(lblStatusUsuario, BorderLayout.WEST);
        barraEstado.add(lblStatusTotal, BorderLayout.EAST);

        // Reemplazar jPanel4 (barra inferior) con la barra de estado
        jPanel4.setLayout(new BorderLayout());
        jPanel4.removeAll();
        jPanel4.add(barraEstado, BorderLayout.CENTER);
        jPanel4.revalidate();
    }

    private void actualizarBarraEstado(int total) {
        lblStatusTotal.setText("Total: " + total + " error" + (total != 1 ? "es" : "") + "  ");
    }

    // ==================== PANEL DESCRIPCION SOLUCION ====================

    private void crearPanelDescripcionSolucion() {
        lblDescSolucion = new JLabel("Descripcion de la Solucion:");
        lblDescSolucion.setFont(Tema.FUENTE_SUBTITULO);
        txtDescSolucion = new JTextArea(3, 20);
        txtDescSolucion.setLineWrap(true);
        txtDescSolucion.setWrapStyleWord(true);
        scrollDescSolucion = new JScrollPane(txtDescSolucion);

        JPanel panelDescSol = new JPanel(new BorderLayout(5, 5));
        panelDescSol.setBackground(Tema.FONDO);
        panelDescSol.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        panelDescSol.add(lblDescSolucion, BorderLayout.NORTH);
        panelDescSol.add(scrollDescSolucion, BorderLayout.CENTER);
        getContentPane().add(panelDescSol, BorderLayout.SOUTH);
    }

    private void configurarEventoFase() {
        cboFase.addActionListener(e -> {
            String faseSeleccionada = (String) cboFase.getSelectedItem();
            boolean mostrarSolucion = "Solucionado".equals(faseSeleccionada);

            txtSolucion.setVisible(mostrarSolucion);
            lblSolucion.setVisible(mostrarSolucion);
            lblDescSolucion.setVisible(mostrarSolucion);
            scrollDescSolucion.setVisible(mostrarSolucion);
            if (!mostrarSolucion) {
                txtSolucion.setText("");
                txtDescSolucion.setText("");
            }
        });

        txtSolucion.setVisible(false);
        lblSolucion.setVisible(false);
        lblDescSolucion.setVisible(false);
        scrollDescSolucion.setVisible(false);
    }

    // ==================== BARRA BUSQUEDA ====================

    private void crearBarraBusqueda() {
        txtBuscar = new javax.swing.JTextField(15);
        txtBuscar.setFont(Tema.FUENTE_CAMPO);

        String[] opciones = new String[Fase.values().length + 1];
        opciones[0] = "Todas";
        String[] etiquetas = Fase.getEtiquetas();
        for (int i = 0; i < etiquetas.length; i++) {
            opciones[i + 1] = etiquetas[i].toUpperCase();
        }
        cboFiltroFase = new javax.swing.JComboBox<>(opciones);
        cboFiltroFase.setFont(Tema.FUENTE_BOTON);

        btnBuscar = crearBoton("Buscar", Tema.PRIMARIO);
        btnBuscar.addActionListener(e -> buscarErrores());

        btnVerCaptura = crearBoton("Ver Captura", new java.awt.Color(0, 102, 0));
        btnVerCaptura.addActionListener(e -> verCaptura());

        btnExportarCSV = crearBoton("Exportar CSV", Tema.PRIMARIO_CLARO);
        btnExportarCSV.addActionListener(e -> exportarCSV());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 5));
        panelBusqueda.setBackground(Tema.FONDO);
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(new JLabel("Fase:"));
        panelBusqueda.add(cboFiltroFase);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnVerCaptura);
        panelBusqueda.add(btnExportarCSV);
        getContentPane().add(panelBusqueda, BorderLayout.NORTH);
    }

    private javax.swing.JButton crearBoton(String texto, java.awt.Color fondo) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.setBackground(fondo);
        btn.setFont(Tema.FUENTE_BOTON);
        btn.setForeground(Tema.TEXTO_CLARO);
        return btn;
    }

    // ==================== TABLA ====================

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Titulo", "Severidad", "Fase", "Fecha",
                         "Registrado Por", "En Proceso Por", "Resuelto Por", "Fecha Solucion"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1.setModel(modeloTabla);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        // Colores por severidad (columna 2)
        Tema.aplicarEstiloTabla(jTable1, 2);

        // Ordenamiento por columna al hacer clic en header
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        jTable1.setRowSorter(sorter);

        int[] anchos = {40, 160, 90, 110, 130, 110, 110, 110, 140};
        for (int i = 0; i < anchos.length; i++) {
            jTable1.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    private void agregarEventos() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccion();
            }
        });

        // Doble clic para ver detalle completo
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarDetalleError();
                }
            }
        });
    }

    // ==================== DETALLE COMPLETO (doble clic) ====================

    private void mostrarDetalleError() {
        int filaVista = jTable1.getSelectedRow();
        if (filaVista < 0) return;

        int filaModelo = jTable1.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        ErrorTicket err = gestor.buscarPorId(id);
        if (err == null) return;

        StringBuilder detalle = new StringBuilder();
        detalle.append("ID: ").append(err.getId()).append("\n");
        detalle.append("Titulo: ").append(err.getTitulo()).append("\n");
        detalle.append("Severidad: ").append(err.getSeveridad().getEtiqueta()).append("\n");
        detalle.append("Fase: ").append(err.getFase().getEtiqueta()).append("\n");
        detalle.append("Fecha: ").append(err.getFecha() != null ? Tema.FORMATO_FECHA_HORA.format(err.getFecha()) : "---").append("\n\n");

        detalle.append("--- Responsables ---\n");
        detalle.append("Registrado por: ").append(err.getRegistradoPor() != null ? err.getRegistradoPor() : "---").append("\n");
        detalle.append("En proceso por: ").append(err.getProcesoPor() != null ? err.getProcesoPor() : "---").append("\n");
        detalle.append("Resuelto por: ").append(err.getResueltoPor() != null ? err.getResueltoPor() : "---").append("\n");
        if (err.getFechaSolucion() != null) {
            detalle.append("Fecha solucion: ").append(Tema.FORMATO_FECHA_HORA.format(err.getFechaSolucion())).append("\n");
        }

        detalle.append("\n--- Descripcion ---\n").append(err.getDescripcion()).append("\n\n");

        if (err.getPasosReproducir() != null && !err.getPasosReproducir().isEmpty()) {
            detalle.append("--- Pasos para Reproducir ---\n").append(err.getPasosReproducir()).append("\n\n");
        }
        if (err.getSolucion() != null && !err.getSolucion().isEmpty()) {
            detalle.append("--- Solucion ---\n").append(err.getSolucion()).append("\n\n");
        }
        if (err.getDescripcionSolucion() != null && !err.getDescripcionSolucion().isEmpty()) {
            detalle.append("--- Descripcion de Solucion ---\n").append(err.getDescripcionSolucion()).append("\n");
        }

        JTextArea txtDetalle = new JTextArea(detalle.toString());
        txtDetalle.setEditable(false);
        txtDetalle.setFont(Tema.FUENTE_CAMPO);
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);

        JDialog dialogo = new JDialog(this, "Detalle del Error #" + err.getId(), true);
        dialogo.add(new JScrollPane(txtDetalle));
        dialogo.setSize(550, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    // ==================== CARGAR / BUSCAR ====================

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<ErrorTicket> errores = gestor.obtenerTodosErrores();
            for (ErrorTicket e : errores) {
                agregarFilaTabla(e);
            }
            actualizarBarraEstado(errores.size());
        } catch (Exception ex) {
            logger.warning("Error al cargar datos: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarFilaTabla(ErrorTicket e) {
        modeloTabla.addRow(new Object[]{
            e.getId(),
            e.getTitulo(),
            e.getSeveridad(),
            e.getFase(),
            e.getFecha(),
            e.getRegistradoPor() != null ? e.getRegistradoPor() : "---",
            e.getProcesoPor() != null ? e.getProcesoPor() : "---",
            e.getResueltoPor() != null ? e.getResueltoPor() : "---",
            e.getFechaSolucion()
        });
    }

    private void cargarDatosSeleccion() {
        int filaVista = jTable1.getSelectedRow();
        if (filaVista < 0) return;

        int filaModelo = jTable1.convertRowIndexToModel(filaVista);

        Object faseTabla = modeloTabla.getValueAt(filaModelo, 3);
        if (faseTabla != null) {
            String faseStr = faseTabla.toString();
            Fase[] fases = Fase.values();
            for (int i = 0; i < fases.length; i++) {
                if (fases[i].name().equals(faseStr)) {
                    cboFase.setSelectedIndex(i);
                    break;
                }
            }
        }

        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        ErrorTicket err = gestor.buscarPorId(id);
        if (err != null) {
            txtSolucion.setText(err.getSolucion() != null ? err.getSolucion() : "");
            txtDescSolucion.setText(err.getDescripcionSolucion() != null ? err.getDescripcionSolucion() : "");
        }
    }

    private void guardarCambios() {
        int filaVista = jTable1.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int filaModelo = jTable1.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        String solucion = txtSolucion.getText().trim();
        Fase faseSeleccionada = Fase.fromIndex(cboFase.getSelectedIndex());

        // Obtener datos completos del error desde la BD
        ErrorTicket previo = gestor.buscarPorId(id);
        if (previo == null) return;

        ErrorTicket error = new ErrorTicket(previo.getTitulo(), previo.getDescripcion(), previo.getSeveridad(), faseSeleccionada);
        error.setSolucion(solucion);
        error.setId(id);

        String descSol = txtDescSolucion.getText().trim();
        if (!descSol.isEmpty()) {
            error.setDescripcionSolucion(descSol);
        }

        try {
            gestor.actualizarError(error);
            JOptionPane.showMessageDialog(this, "Error actualizado correctamente");
            cargarTabla();
        } catch (Exception ex) {
            logger.warning("Error al actualizar: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarErrores() {
        String titulo = txtBuscar.getText().trim();
        String fase = (String) cboFiltroFase.getSelectedItem();
        if ("Todas".equals(fase)) {
            fase = null;
        }

        modeloTabla.setRowCount(0);
        try {
            List<ErrorTicket> errores = gestor.buscarErrores(titulo, fase);
            for (ErrorTicket e : errores) {
                agregarFilaTabla(e);
            }
            actualizarBarraEstado(errores.size());
        } catch (Exception ex) {
            logger.warning("Error al buscar: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== ACCIONES ====================

    private void verCaptura() {
        int filaVista = jTable1.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int filaModelo = jTable1.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);
        ErrorTicket errorSel = gestor.buscarPorId(id);

        if (errorSel == null || errorSel.getCapturaError() == null || errorSel.getCapturaError().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este error no tiene captura adjunta");
            return;
        }

        ImageIcon imagen = ImagenCaptura.cargarImagenCompleta(errorSel.getCapturaError(), 800, 600);
        if (imagen == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen");
            return;
        }

        JDialog dialogo = new JDialog(this, "Captura: " + errorSel.getTitulo(), true);
        dialogo.add(new JScrollPane(new JLabel(imagen)));
        dialogo.setSize(850, 650);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void exportarCSV() {
        try {
            List<ErrorTicket> errores = gestor.obtenerTodosErrores();
            if (errores.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay errores para exportar");
                return;
            }

            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar reporte CSV");
            fc.setFileFilter(new FileNameExtensionFilter("Archivo CSV (*.csv)", "csv"));
            fc.setSelectedFile(new File("reporte_errores.csv"));

            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = fc.getSelectedFile();
                if (!archivo.getName().endsWith(".csv")) {
                    archivo = new File(archivo.getAbsolutePath() + ".csv");
                }
                ExportadorCSV.exportar(errores, archivo);
                JOptionPane.showMessageDialog(this,
                    "Reporte exportado exitosamente:\n" + archivo.getAbsolutePath());
            }
        } catch (Exception ex) {
            logger.warning("Error al exportar: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarError() {
        int filaVista = jTable1.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de eliminar este error?", "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int filaModelo = jTable1.convertRowIndexToModel(filaVista);
                int id = (int) modeloTabla.getValueAt(filaModelo, 0);
                gestor.eliminarError(id);
                JOptionPane.showMessageDialog(this, "Error eliminado correctamente");
                cargarTabla();
            } catch (Exception ex) {
                logger.warning("Error al eliminar: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== GENERATED CODE ====================

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cboFase = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lblSolucion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSolucion = new javax.swing.JTextArea();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnRegresarPrin = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(Tema.FONDO);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {{null, null, null, null}},
            new String [] {"Title 1", "Title 2", "Title 3", "Title 4"}
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(Tema.FUENTE_SUBTITULO);
        jLabel1.setText("Fase");

        cboFase.setFont(Tema.FUENTE_BOTON);
        cboFase.setModel(new javax.swing.DefaultComboBoxModel<>(Fase.getEtiquetas()));

        jLabel2.setFont(Tema.FUENTE_TITULO);
        jLabel2.setText("Errores");

        lblSolucion.setFont(Tema.FUENTE_SUBTITULO);
        lblSolucion.setText("Solucion");

        txtSolucion.setColumns(20);
        txtSolucion.setRows(5);
        jScrollPane2.setViewportView(txtSolucion);

        btnSalir.setBackground(Tema.PELIGRO);
        btnSalir.setFont(Tema.FUENTE_BOTON);
        btnSalir.setForeground(Tema.TEXTO_CLARO);
        btnSalir.setText("Salir");
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        btnGuardar.setBackground(Tema.PRIMARIO);
        btnGuardar.setFont(Tema.FUENTE_BOTON);
        btnGuardar.setForeground(Tema.TEXTO_CLARO);
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnEliminar.setBackground(new java.awt.Color(153, 0, 0));
        btnEliminar.setFont(Tema.FUENTE_BOTON);
        btnEliminar.setForeground(Tema.TEXTO_CLARO);
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(e -> eliminarError());

        btnRegresarPrin.setBackground(Tema.PELIGRO);
        btnRegresarPrin.setFont(Tema.FUENTE_BOTON);
        btnRegresarPrin.setForeground(Tema.TEXTO_CLARO);
        btnRegresarPrin.setText("Regresar");
        btnRegresarPrin.addActionListener(this::btnRegresarPrinActionPerformed);

        jPanel2.setBackground(Tema.PRIMARIO);
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 25));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 25, Short.MAX_VALUE));

        jPanel4.setBackground(Tema.PRIMARIO);
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 28));
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 28, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnGuardar).addGap(18, 18, 18)
                .addComponent(btnEliminar).addGap(18, 18, 18)
                .addComponent(btnRegresarPrin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir).addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE).addComponent(jLabel2).addGap(340, 340, 340))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup().addGap(30, 30, 30).addComponent(jLabel1))
                    .addComponent(cboFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSolucion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7).addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1).addComponent(lblSolucion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresarPrin).addComponent(btnGuardar)
                    .addComponent(btnEliminar).addComponent(btnSalir))
                .addGap(20, 20, 20)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de que desea salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        guardarCambios();
    }

    private void btnRegresarPrinActionPerformed(java.awt.event.ActionEvent evt) {
        new FrmPrincipal().setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new frmTableErrores().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRegresarPrin;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cboFase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblSolucion;
    private javax.swing.JTextArea txtSolucion;
    // End of variables declaration//GEN-END:variables
}
