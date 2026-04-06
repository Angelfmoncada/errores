package Vista;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.Severidad;
import Servicio.GestorErrores;
import Utilidades.ExportadorCSV;
import Utilidades.ImagenCaptura;
import Utilidades.Tema;

import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Vista principal para consultar, editar y eliminar errores.
 * Incluye busqueda por titulo/fase, exportacion a CSV y vista de capturas.
 *
 * Refactorizado: se elimino duplicacion de codigo usando GestorErrores.buscarPorId(),
 * Fase.fromIndex(), Tema para estilos y ExportadorCSV para exportacion.
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

    public frmTableErrores() {
        initComponents();
        Utilidades.Icono.setLogotipo(this);
        this.setLocationRelativeTo(null);

        crearPanelDescripcionSolucion();
        configurarEventoFase();
        crearBarraBusqueda();
        configurarTabla();
        cargarTabla();
        agregarEventos();
    }

    private void crearPanelDescripcionSolucion() {
        lblDescSolucion = new javax.swing.JLabel("Descripcion de la Solucion:");
        lblDescSolucion.setFont(Tema.FUENTE_SUBTITULO);
        txtDescSolucion = new javax.swing.JTextArea(3, 20);
        txtDescSolucion.setLineWrap(true);
        txtDescSolucion.setWrapStyleWord(true);
        scrollDescSolucion = new javax.swing.JScrollPane(txtDescSolucion);

        javax.swing.JPanel panelDescSol = new javax.swing.JPanel(new java.awt.BorderLayout(5, 5));
        panelDescSol.setBackground(Tema.FONDO);
        panelDescSol.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 14, 5, 14));
        panelDescSol.add(lblDescSolucion, java.awt.BorderLayout.NORTH);
        panelDescSol.add(scrollDescSolucion, java.awt.BorderLayout.CENTER);
        getContentPane().add(panelDescSol, java.awt.BorderLayout.SOUTH);
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

        javax.swing.JPanel panelBusqueda = new javax.swing.JPanel();
        panelBusqueda.setBackground(Tema.FONDO);
        panelBusqueda.add(new javax.swing.JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(new javax.swing.JLabel("Fase:"));
        panelBusqueda.add(cboFiltroFase);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnVerCaptura);
        panelBusqueda.add(btnExportarCSV);
        getContentPane().add(panelBusqueda, java.awt.BorderLayout.NORTH);
    }

    private javax.swing.JButton crearBoton(String texto, java.awt.Color fondo) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.setBackground(fondo);
        btn.setFont(Tema.FUENTE_BOTON);
        btn.setForeground(Tema.TEXTO_CLARO);
        return btn;
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Titulo", "Descripcion", "Severidad", "Fase", "Fecha",
                         "Solucion", "Resuelto Por", "Fecha Solucion"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1.setModel(modeloTabla);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        Tema.aplicarEstiloTabla(jTable1);

        int[] anchos = {45, 150, 210, 95, 115, 130, 170, 115, 140};
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
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<ErrorTicket> errores = gestor.obtenerTodosErrores();
            for (ErrorTicket e : errores) {
                agregarFilaTabla(e);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarFilaTabla(ErrorTicket e) {
        modeloTabla.addRow(new Object[]{
            e.getId(),
            e.getTitulo(),
            e.getDescripcion(),
            e.getSeveridad(),
            e.getFase(),
            e.getFecha(),
            e.getSolucion(),
            e.getResueltoPor(),
            e.getFechaSolucion()
        });
    }

    private void cargarDatosSeleccion() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) return;

        Object faseTabla = jTable1.getValueAt(fila, 4);
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

        txtSolucion.setText((String) jTable1.getValueAt(fila, 6));

        int id = (int) jTable1.getValueAt(fila, 0);
        ErrorTicket err = gestor.buscarPorId(id);
        if (err != null) {
            txtDescSolucion.setText(err.getDescripcionSolucion() != null ? err.getDescripcionSolucion() : "");
        }
    }

    private void guardarCambios() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int id = (int) jTable1.getValueAt(fila, 0);
        String titulo = (String) jTable1.getValueAt(fila, 1);
        String descripcion = (String) jTable1.getValueAt(fila, 2);
        Severidad sev = (Severidad) jTable1.getValueAt(fila, 3);
        String solucion = txtSolucion.getText().trim();
        Fase faseSeleccionada = Fase.fromIndex(cboFase.getSelectedIndex());

        ErrorTicket error = new ErrorTicket(titulo, descripcion, sev, faseSeleccionada);
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verCaptura() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int id = (int) jTable1.getValueAt(fila, 0);
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
        JLabel lblImagen = new JLabel(imagen);
        JScrollPane scroll = new JScrollPane(lblImagen);
        dialogo.add(scroll);
        dialogo.setSize(850, 650);
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    /**
     * Exporta los errores visibles en la tabla a un archivo CSV.
     */
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
            JOptionPane.showMessageDialog(this,
                "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarError() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de eliminar este error?", "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) jTable1.getValueAt(fila, 0);
                gestor.eliminarError(id);
                JOptionPane.showMessageDialog(this, "Error eliminado correctamente");
                cargarTabla();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
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
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel4.setBackground(Tema.PRIMARIO);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnRegresarPrin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(312, 312, 312))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1))
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
                .addGap(7, 7, 7)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblSolucion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresarPrin)
                    .addComponent(btnGuardar)
                    .addComponent(btnEliminar)
                    .addComponent(btnSalir))
                .addGap(32, 32, 32)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de que desea salir?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION);
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
