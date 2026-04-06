package Vista;

import Modelo.ErrorTicket;
import Modelo.Fase;
import Modelo.Severidad;
import Servicio.GestorErrores;
import Utilidades.ImagenCaptura;
import Utilidades.Tema;
import Utilidades.ValidadorCampos;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Formulario para registrar un nuevo error en el sistema.
 * Incluye validacion visual de campos, adjunto de capturas y pasos para reproducir.
 *
 * Refactorizado: se usa Severidad.fromIndex() en lugar de if-else repetidos,
 * ValidadorCampos para validacion visual y Tema para estilos consistentes.
 */
public class FrmRegistrarError extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(FrmRegistrarError.class.getName());

    private File archivoCaptura;
    private javax.swing.JButton btnAdjuntarCaptura;
    private javax.swing.JLabel lblCaptura;
    private javax.swing.JLabel lblPreviewCaptura;
    private javax.swing.JTextArea txtPasosReproducir;

    public FrmRegistrarError() {
        initComponents();
        Utilidades.Icono.setLogotipo(this);
        this.setLocationRelativeTo(null);
        agregarComponentesExtra();
    }

    private void agregarComponentesExtra() {
        javax.swing.JPanel panelExtra = new javax.swing.JPanel();
        panelExtra.setLayout(new javax.swing.BoxLayout(panelExtra, javax.swing.BoxLayout.Y_AXIS));
        panelExtra.setBackground(Tema.FONDO);

        // Seccion: Pasos para reproducir
        javax.swing.JPanel panelPasos = new javax.swing.JPanel(new java.awt.BorderLayout(5, 5));
        panelPasos.setBackground(Tema.FONDO);
        panelPasos.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

        javax.swing.JLabel lblPasos = new javax.swing.JLabel("Pasos para Reproducir:");
        lblPasos.setFont(Tema.FUENTE_SUBTITULO);
        panelPasos.add(lblPasos, java.awt.BorderLayout.NORTH);

        txtPasosReproducir = new javax.swing.JTextArea(4, 30);
        txtPasosReproducir.setLineWrap(true);
        txtPasosReproducir.setWrapStyleWord(true);
        txtPasosReproducir.setFont(Tema.FUENTE_CAMPO);
        panelPasos.add(new javax.swing.JScrollPane(txtPasosReproducir), java.awt.BorderLayout.CENTER);
        panelExtra.add(panelPasos);

        // Seccion: Captura de pantalla
        lblCaptura = new javax.swing.JLabel("Captura:");
        lblCaptura.setFont(Tema.FUENTE_SUBTITULO);

        btnAdjuntarCaptura = new javax.swing.JButton("Adjuntar Captura");
        btnAdjuntarCaptura.setBackground(new java.awt.Color(0, 102, 0));
        btnAdjuntarCaptura.setFont(Tema.FUENTE_BOTON);
        btnAdjuntarCaptura.setForeground(Tema.TEXTO_CLARO);
        btnAdjuntarCaptura.addActionListener(e -> seleccionarCaptura());

        lblPreviewCaptura = new javax.swing.JLabel("Sin captura adjunta");
        lblPreviewCaptura.setPreferredSize(new java.awt.Dimension(150, 100));
        lblPreviewCaptura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreviewCaptura.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));

        javax.swing.JPanel panelCaptura = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        panelCaptura.setBackground(Tema.FONDO);
        panelCaptura.add(lblCaptura);
        panelCaptura.add(btnAdjuntarCaptura);
        panelCaptura.add(lblPreviewCaptura);
        panelExtra.add(panelCaptura);

        getContentPane().add(panelExtra, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void seleccionarCaptura() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar captura de pantalla");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagenes (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoCaptura = fileChooser.getSelectedFile();
            javax.swing.ImageIcon miniatura = ImagenCaptura.cargarMiniatura(archivoCaptura.getAbsolutePath(), 150, 100);
            if (miniatura != null) {
                lblPreviewCaptura.setIcon(miniatura);
                lblPreviewCaptura.setText("");
            } else {
                lblPreviewCaptura.setIcon(null);
                lblPreviewCaptura.setText(archivoCaptura.getName());
            }
        }
    }

    /**
     * Valida los campos del formulario con retroalimentacion visual.
     * @return true si todos los campos obligatorios son validos
     */
    private boolean validarFormulario() {
        boolean valido = true;

        if (!ValidadorCampos.validarNoVacio(txtTitulo)) {
            valido = false;
        } else if (!ValidadorCampos.validarLongitud(txtTitulo, 100)) {
            valido = false;
        }

        if (!ValidadorCampos.validarNoVacio(txtDescripcion)) {
            valido = false;
        }

        return valido;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        cboSeveridad = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnGuardarError = new javax.swing.JButton();
        btnRegresarPrin = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(Tema.FONDO);

        jLabel1.setFont(Tema.FUENTE_SUBTITULO);
        jLabel1.setText("Descripcion:");

        jLabel2.setFont(Tema.FUENTE_SUBTITULO);
        jLabel2.setText("Error:");

        txtTitulo.setFont(Tema.FUENTE_CAMPO);

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        txtDescripcion.setFont(Tema.FUENTE_CAMPO);
        jScrollPane1.setViewportView(txtDescripcion);

        cboSeveridad.setFont(Tema.FUENTE_SUBTITULO);
        cboSeveridad.setModel(new javax.swing.DefaultComboBoxModel<>(Severidad.getEtiquetas()));

        jLabel3.setFont(Tema.FUENTE_SUBTITULO);
        jLabel3.setText("Severidad");

        btnGuardarError.setBackground(Tema.PRIMARIO);
        btnGuardarError.setFont(Tema.FUENTE_BOTON);
        btnGuardarError.setForeground(Tema.TEXTO_CLARO);
        btnGuardarError.setText("Guardar");
        btnGuardarError.addActionListener(this::btnGuardarErrorActionPerformed);

        btnRegresarPrin.setBackground(Tema.PELIGRO);
        btnRegresarPrin.setFont(Tema.FUENTE_BOTON);
        btnRegresarPrin.setForeground(Tema.TEXTO_CLARO);
        btnRegresarPrin.setText("Regresar");
        btnRegresarPrin.addActionListener(this::btnRegresarPrinActionPerformed);

        jPanel2.setBackground(Tema.PRIMARIO);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));

        jPanel3.setBackground(Tema.PRIMARIO);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(btnGuardarError)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegresarPrin)
                .addGap(187, 187, 187))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(cboSeveridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cboSeveridad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarError)
                    .addComponent(btnRegresarPrin))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void btnRegresarPrinActionPerformed(java.awt.event.ActionEvent evt) {
        new FrmPrincipal().setVisible(true);
        this.dispose();
    }

    private void btnGuardarErrorActionPerformed(java.awt.event.ActionEvent evt) {
        if (!validarFormulario()) {
            JOptionPane.showMessageDialog(this, "Corrija los campos marcados en rojo");
            return;
        }

        Severidad sev = Severidad.fromIndex(cboSeveridad.getSelectedIndex());

        ErrorTicket error = new ErrorTicket(
            txtTitulo.getText().trim(),
            txtDescripcion.getText().trim(),
            sev,
            Fase.REGISTRADO
        );

        String pasos = txtPasosReproducir.getText().trim();
        if (!pasos.isEmpty()) {
            error.setPasosReproducir(pasos);
        }

        if (archivoCaptura != null) {
            try {
                String rutaCaptura = ImagenCaptura.copiarImagen(archivoCaptura);
                error.setCapturaError(rutaCaptura);
            } catch (java.io.IOException ioEx) {
                JOptionPane.showMessageDialog(this,
                    "Error al adjuntar captura: " + ioEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            new GestorErrores().registrarError(error);
            JOptionPane.showMessageDialog(this, "Error registrado correctamente");
            limpiarCampos();
            this.dispose();
            new FrmPrincipal().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        java.awt.EventQueue.invokeLater(() -> new FrmRegistrarError().setVisible(true));
    }

    // Variables declaration
    private javax.swing.JButton btnGuardarError;
    private javax.swing.JButton btnRegresarPrin;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboSeveridad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtTitulo;

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtPasosReproducir.setText("");
        cboSeveridad.setSelectedIndex(0);
        archivoCaptura = null;
        lblPreviewCaptura.setIcon(null);
        lblPreviewCaptura.setText("Sin captura adjunta");
        ValidadorCampos.resetearBorde(txtTitulo);
        ValidadorCampos.resetearBorde(txtDescripcion);
    }
}
