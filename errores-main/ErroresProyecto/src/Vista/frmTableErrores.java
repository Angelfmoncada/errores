package Vista;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.SesionUsuario;
import Modelo.Severidad;
import Servicio.GestorErrores;
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
@SuppressWarnings("this-escape")
public class frmTableErrores extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(frmTableErrores.class.getName());

    private final transient GestorErrores gestor = new GestorErrores();
    private DefaultTableModel modeloTabla;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JComboBox<String> cboFiltroFase;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnVerCaptura;
    private javax.swing.JButton btnAdjuntarCaptura;
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

        btnAdjuntarCaptura = crearBoton("Adjuntar Captura", new java.awt.Color(0, 102, 153));
        btnAdjuntarCaptura.addActionListener(e -> adjuntarCaptura());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 5));
        panelBusqueda.setBackground(Tema.FONDO);
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(new JLabel("Fase:"));
        panelBusqueda.add(cboFiltroFase);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnVerCaptura);
        panelBusqueda.add(btnAdjuntarCaptura);
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

        // Validar campos obligatorios cuando la fase es "Solucionado"
        if (faseSeleccionada == Fase.SOLUCIONADO) {
            if (solucion.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debe ingresar la solución para marcar como Solucionado",
                    "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
                txtSolucion.requestFocus();
                return;
            }
            if (txtDescSolucion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debe ingresar la descripción de la solución para marcar como Solucionado",
                    "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
                txtDescSolucion.requestFocus();
                return;
            }
        }

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

    /**
     * Permite adjuntar o cambiar la captura de pantalla de un error seleccionado.
     */
    private void adjuntarCaptura() {
        int filaVista = jTable1.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int filaModelo = jTable1.convertRowIndexToModel(filaVista);
        int id = (int) modeloTabla.getValueAt(filaModelo, 0);

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar captura de pantalla");
        fc.setFileFilter(new FileNameExtensionFilter("Imagenes (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fc.getSelectedFile();
            try {
                String rutaCaptura = ImagenCaptura.copiarImagen(archivoSeleccionado);
                gestor.adjuntarCaptura(id, rutaCaptura);
                JOptionPane.showMessageDialog(this, "Captura adjuntada correctamente al error #" + id);
                cargarTabla();
            } catch (java.io.IOException ioEx) {
                JOptionPane.showMessageDialog(this,
                    "Error al adjuntar captura: " + ioEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    private void initComponents() {
        jTable1 = new javax.swing.JTable();
        cboFase = new javax.swing.JComboBox<>();
        txtSolucion = new javax.swing.JTextArea();
        lblSolucion = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion de Errores");
        getContentPane().setBackground(Tema.FONDO);
        getContentPane().setLayout(new BorderLayout(0, 0));

        // === HEADER ===
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Tema.PRIMARIO);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblTitulo = new JLabel("Gestion de Errores");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitulo.setForeground(Tema.TEXTO_CLARO);
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        // Indicador de usuario en header
        String usr = Modelo.SesionUsuario.getInstancia().getUsername();
        JLabel lblUsr = new JLabel("Usuario: " + (usr != null ? usr : "---"));
        lblUsr.setFont(Tema.FUENTE_STATUS);
        lblUsr.setForeground(new java.awt.Color(180, 200, 255));
        panelHeader.add(lblUsr, BorderLayout.EAST);

        // === TABLA ===
        jScrollPane1 = new JScrollPane(jTable1);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());

        // === PANEL EDICION (fase + solucion) ===
        JPanel panelEdicion = new JPanel(new java.awt.GridBagLayout());
        panelEdicion.setBackground(Tema.FONDO);
        panelEdicion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(12, 16, 8, 16)
        ));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(4, 6, 4, 6);
        gbc.anchor = java.awt.GridBagConstraints.WEST;

        // Fase
        JLabel jLabel1 = new JLabel("Fase:");
        jLabel1.setFont(Tema.FUENTE_SUBTITULO);
        gbc.gridx = 0; gbc.gridy = 0;
        panelEdicion.add(jLabel1, gbc);

        cboFase.setFont(Tema.FUENTE_CAMPO);
        cboFase.setModel(new javax.swing.DefaultComboBoxModel<>(Fase.getEtiquetas()));
        gbc.gridx = 1; gbc.gridy = 0;
        panelEdicion.add(cboFase, gbc);

        // Solucion
        lblSolucion.setFont(Tema.FUENTE_SUBTITULO);
        lblSolucion.setText("Solucion:");
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(4, 30, 4, 6);
        panelEdicion.add(lblSolucion, gbc);

        txtSolucion.setColumns(40);
        txtSolucion.setRows(4);
        txtSolucion.setLineWrap(true);
        txtSolucion.setWrapStyleWord(true);
        txtSolucion.setFont(Tema.FUENTE_CAMPO);
        jScrollPane2 = new JScrollPane(txtSolucion);
        gbc.gridx = 3; gbc.gridy = 0; gbc.gridheight = 2;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(4, 6, 4, 6);
        panelEdicion.add(jScrollPane2, gbc);

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 12, 8));
        panelBotones.setBackground(Tema.FONDO);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(200, 200, 210)));

        btnGuardar = crearBoton("Guardar", Tema.PRIMARIO);
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);
        btnEliminar = crearBoton("Eliminar", new java.awt.Color(153, 0, 0));
        btnEliminar.addActionListener(e -> eliminarError());
        btnRegresarPrin = crearBoton("Regresar", new java.awt.Color(100, 100, 120));
        btnRegresarPrin.addActionListener(this::btnRegresarPrinActionPerformed);
        btnSalir = crearBoton("Salir", Tema.PELIGRO);
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRegresarPrin);
        panelBotones.add(btnSalir);

        // === BARRA ESTADO ===
        jPanel4 = new JPanel(new BorderLayout());
        jPanel4.setBackground(Tema.PRIMARIO);
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 28));

        // === PANEL CENTRAL (tabla + edicion + botones) ===
        jPanel1 = new JPanel(new BorderLayout(0, 0));
        jPanel1.setBackground(Tema.FONDO);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Tema.FONDO);
        panelInferior.add(panelEdicion, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        jPanel1.add(panelInferior, BorderLayout.SOUTH);

        // === ENSAMBLAR ===
        getContentPane().add(panelHeader, BorderLayout.NORTH);
        getContentPane().add(jPanel1, BorderLayout.CENTER);
        getContentPane().add(jPanel4, BorderLayout.SOUTH);

        setSize(850, 620);
        setLocationRelativeTo(null);
    }

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
