/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Modelo.SesionUsuario;

/**
 *
 * @author Mass
 */
@SuppressWarnings("this-escape")
public class FrmPrincipal extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPrincipal.class.getName());

    public FrmPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        Utilidades.Icono.setLogotipo(this);
        // Mostrar el usuario autenticado en el título
        String usuario = SesionUsuario.getInstancia().getUsername();
        if (usuario != null) {
            this.setTitle("Gestor de Errores - " + usuario);
        }
        agregarBotonesExtra();
    }

    private void agregarBotonesExtra() {
        // Crear botones Dashboard y Errores Resueltos con mismo estilo
        javax.swing.JButton btnDashboard = new javax.swing.JButton("Dashboard");
        btnDashboard.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnDashboard.setBackground(new java.awt.Color(41, 128, 185));
        btnDashboard.setForeground(java.awt.Color.WHITE);
        btnDashboard.setPreferredSize(new java.awt.Dimension(160, 35));
        btnDashboard.addActionListener(e -> {
            new FrmDashboard().setVisible(true);
            this.dispose();
        });

        javax.swing.JButton btnResueltos = new javax.swing.JButton("Errores Resueltos");
        btnResueltos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnResueltos.setBackground(new java.awt.Color(41, 128, 185));
        btnResueltos.setForeground(java.awt.Color.WHITE);
        btnResueltos.setPreferredSize(new java.awt.Dimension(160, 35));
        btnResueltos.addActionListener(e -> {
            new FrmErroresResueltos().setVisible(true);
            this.dispose();
        });

        // Panel central con BoxLayout vertical para centrar todo
        javax.swing.JPanel panelCentral = new javax.swing.JPanel();
        panelCentral.setLayout(new javax.swing.BoxLayout(panelCentral, javax.swing.BoxLayout.Y_AXIS));
        panelCentral.setBackground(new java.awt.Color(0, 0, 102));
        panelCentral.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // Titulo centrado
        javax.swing.JLabel lblTitulo = new javax.swing.JLabel("Gestor de Errores");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Configurar todos los botones con mismo tamano y centrado
        javax.swing.JButton[] botones = {btnRegistrarError1, btnErrores1, btnDashboard, btnResueltos, btnSalir};
        java.awt.Dimension tamBoton = new java.awt.Dimension(220, 38);
        for (javax.swing.JButton btn : botones) {
            btn.setMaximumSize(tamBoton);
            btn.setPreferredSize(tamBoton);
            btn.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        }

        // Armar el panel
        panelCentral.add(javax.swing.Box.createVerticalGlue());
        panelCentral.add(lblTitulo);
        panelCentral.add(javax.swing.Box.createVerticalStrut(30));
        for (javax.swing.JButton btn : botones) {
            panelCentral.add(btn);
            panelCentral.add(javax.swing.Box.createVerticalStrut(10));
        }
        panelCentral.add(javax.swing.Box.createVerticalGlue());

        // Reemplazar contenido
        jPanel1.removeAll();
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(panelCentral, java.awt.BorderLayout.CENTER);

        jPanel1.revalidate();
        jPanel1.repaint();
        setSize(420, 420);
        setLocationRelativeTo(null);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblFondoRegistrar = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnErrores1 = new javax.swing.JButton();
        btnRegistrarError1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));

        lblFondoRegistrar.setBackground(new java.awt.Color(0, 0, 102));

        javax.swing.GroupLayout lblFondoRegistrarLayout = new javax.swing.GroupLayout(lblFondoRegistrar);
        lblFondoRegistrar.setLayout(lblFondoRegistrarLayout);
        lblFondoRegistrarLayout.setHorizontalGroup(
            lblFondoRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 189, Short.MAX_VALUE)
        );
        lblFondoRegistrarLayout.setVerticalGroup(
            lblFondoRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );

        btnSalir.setBackground(new java.awt.Color(41, 128, 185));
        btnSalir.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnSalir.setForeground(java.awt.Color.WHITE);
        btnSalir.setText("Salir");
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        btnErrores1.setBackground(new java.awt.Color(41, 128, 185));
        btnErrores1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnErrores1.setForeground(java.awt.Color.WHITE);
        btnErrores1.setText("Gestion de Errores");
        btnErrores1.addActionListener(this::btnErrores1ActionPerformed);

        btnRegistrarError1.setBackground(new java.awt.Color(41, 128, 185));
        btnRegistrarError1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnRegistrarError1.setForeground(java.awt.Color.WHITE);
        btnRegistrarError1.setText("Nuevo Error");
        btnRegistrarError1.addActionListener(this::btnRegistrarError1ActionPerformed);

        // Layout simple - sera reemplazado por agregarBotonesExtra()
        jPanel1.setLayout(new java.awt.BorderLayout());

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea salir?", "Confirmar salida",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnErrores1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnErrores1ActionPerformed
        
            new frmTableErrores().setVisible(true);
            this.dispose();
    }//GEN-LAST:event_btnErrores1ActionPerformed

    private void btnRegistrarError1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarError1ActionPerformed

 new FrmRegistrarError().setVisible(true);
 this.dispose();
    }//GEN-LAST:event_btnRegistrarError1ActionPerformed

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

        java.awt.EventQueue.invokeLater(() -> new FrmPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnErrores1;
    private javax.swing.JButton btnRegistrarError1;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel lblFondoRegistrar;
    // End of variables declaration//GEN-END:variables
}
