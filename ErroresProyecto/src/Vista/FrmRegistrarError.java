/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;
import Modelo.Fase;
import Modelo.Severidad;
import Modelo.ErrorTicket;
import Servicio.GestorErrores;
import Utilidades.ImagenCaptura;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mass
 */
public class FrmRegistrarError extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmRegistrarError.class.getName());

    private File archivoCaptura; // Archivo de captura seleccionado
    private javax.swing.JButton btnAdjuntarCaptura;
    private javax.swing.JLabel lblCaptura;
    private javax.swing.JLabel lblPreviewCaptura;

    public FrmRegistrarError() {
        initComponents();
        Utilidades.Icono.setLogotipo(this);
        this.setLocationRelativeTo(null);
        agregarComponentesCaptura();
    }

    private void agregarComponentesCaptura() {
        lblCaptura = new javax.swing.JLabel("Captura:");
        lblCaptura.setFont(new java.awt.Font("Trebuchet MS", 1, 14));

        btnAdjuntarCaptura = new javax.swing.JButton("Adjuntar Captura");
        btnAdjuntarCaptura.setBackground(new java.awt.Color(0, 102, 0));
        btnAdjuntarCaptura.setFont(new java.awt.Font("Trebuchet MS", 1, 12));
        btnAdjuntarCaptura.setForeground(new java.awt.Color(255, 255, 255));
        btnAdjuntarCaptura.addActionListener(e -> seleccionarCaptura());

        lblPreviewCaptura = new javax.swing.JLabel("Sin captura adjunta");
        lblPreviewCaptura.setPreferredSize(new java.awt.Dimension(150, 100));
        lblPreviewCaptura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreviewCaptura.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));

        // Agregar al panel principal
        javax.swing.JPanel panelCaptura = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        panelCaptura.setBackground(new java.awt.Color(204, 204, 204));
        panelCaptura.add(lblCaptura);
        panelCaptura.add(btnAdjuntarCaptura);
        panelCaptura.add(lblPreviewCaptura);

        getContentPane().add(panelCaptura, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void seleccionarCaptura() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar captura de pantalla");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoCaptura = fileChooser.getSelectedFile();
            // Mostrar miniatura
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

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 14));
        jLabel1.setText("Descripción:");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 14));
        jLabel2.setText("Error:");

        txtTitulo.setFont(new java.awt.Font("Trebuchet MS", 3, 14));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane1.setViewportView(txtDescripcion);

        cboSeveridad.setFont(new java.awt.Font("Trebuchet MS", 1, 14));
        cboSeveridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Baja", "Media", "Alta", "Critica" }));

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 1, 14));
        jLabel3.setText("Severidad");

        btnGuardarError.setBackground(new java.awt.Color(0, 0, 102));
        btnGuardarError.setFont(new java.awt.Font("Trebuchet MS", 3, 14));
        btnGuardarError.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarError.setText("Guardar");
        btnGuardarError.addActionListener(this::btnGuardarErrorActionPerformed);

        btnRegresarPrin.setBackground(new java.awt.Color(255, 51, 51));
        btnRegresarPrin.setFont(new java.awt.Font("Trebuchet MS", 1, 14));
        btnRegresarPrin.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresarPrin.setText("Regresar");
        btnRegresarPrin.addActionListener(this::btnRegresarPrinActionPerformed);

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

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
        new  FrmPrincipal().setVisible(true);
        this.dispose();


    }

    private void btnGuardarErrorActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtTitulo.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        if (txtTitulo.getText().trim().length() > 100) {
            JOptionPane.showMessageDialog(this, "El título no puede exceder 100 caracteres");
            return;
        }

        int index = cboSeveridad.getSelectedIndex();
        Modelo.Severidad sev;

        if (index == 0) {
            sev = Modelo.Severidad.BAJA;
        } else if (index == 1) {
            sev = Modelo.Severidad.MEDIA;
        } else if (index == 2) {
            sev = Modelo.Severidad.ALTA;
        } else {
            sev = Modelo.Severidad.CRITICA;
        }

        String solucion = null;

        ErrorTicket error = new ErrorTicket(
            txtTitulo.getText().trim(),
            txtDescripcion.getText().trim(),
            sev,
            Modelo.Fase.REGISTRADO
        );

        error.setSolucion(solucion);

        // Copiar captura si se adjuntó una
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
            GestorErrores gestor = new GestorErrores();
            gestor.registrarError(error);
            javax.swing.JOptionPane.showMessageDialog(this, "Error registrado correctamente");
            txtTitulo.setText("");
            txtDescripcion.setText("");
            cboSeveridad.setSelectedIndex(0);
            this.dispose();
            new FrmPrincipal().setVisible(true);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Error al guardar: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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
        cboSeveridad.setSelectedIndex(0);
    }

   
}


