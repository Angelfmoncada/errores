/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Modelo.Fase;
import Modelo.Severidad;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import Modelo.ErrorTicket;
import Servicio.GestorErrores;

/**
 *
 * @author Mass
 */
public class frmTableErrores extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmTableErrores.class.getName());
    
    private DefaultTableModel modeloTabla;

    /**
     * Creates new form frmTableErrores
     */
    public  frmTableErrores() {
        //this.setUndecorated(true);
        initComponents();
        Utilidades.Icono.setLogotipo(this);
        this.setLocationRelativeTo(null);
        
        
        //Evento de activar txtSolución solo si el ComboBox está en "Solucionado"
          cboFase.addActionListener(e -> {
        String faseSeleccionada = (String) cboFase.getSelectedItem();
        boolean mostrarSolucion = faseSeleccionada.equals("Solucionado");

        txtSolucion.setVisible(mostrarSolucion);
        lblSolucion.setVisible(mostrarSolucion); 
        if (!mostrarSolucion) {
            txtSolucion.setText(""); 
        }
    });
        
        txtSolucion.setVisible(false);  // Oculto por defecto
        lblSolucion.setVisible(false);
        
        configurarTabla();
        cargarTabla();
        agregarEventos();
    }
    
    
    //Crea el modelo tabla, lo asigna a JTable1 y define que solo se pueda seleccionar una fila sola a la vez
     private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Título", "Descripción", "Severidad", "Fase","Solución"}, 0
        );
        
        
        jTable1.setModel(modeloTabla);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void agregarEventos() {
        
        //Evento que se activa cuando se selecciona una fila y se muestran los datos
        jTable1.getSelectionModel().addListSelectionListener(e -> cargarDatosSeleccion());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnSalir.addActionListener(e -> dispose()); // botón salir
    }
    //Carga los datos de la tabla 
    private void cargarTabla() {
        modeloTabla.setRowCount(0); // limpiar tabla
       //Trae la lista desde la clase GestorErrores
        List<ErrorTicket> errores = new GestorErrores().obtenerTodosErrores();
        
        System.out.println("Errores encontrados: " + errores.size());
        //Recorre toda la tabla y trae los datos agregándolos a la tabla
        for (ErrorTicket e : errores) {
            modeloTabla.addRow(new Object[]{
                    e.getId(),
                    e.getTitulo(),
                    e.getDescripcion(),
                    e.getSeveridad(),
                    e.getFase(),
                    e.getSolucion()
            });
        }
    }

    
    /*
  Carga los datos del error seleccionado en la tabla
  hacia los componentes de la interfaz. 
  Actualiza el combobox de fase y el campo de texto de solución
  según la fila seleccionada.
 */
   private void cargarDatosSeleccion() {
    int fila = jTable1.getSelectedRow();
    if (fila >= 0) { //Verifica que exista una fila en la tabla
        // Cargar la fase usando IF
        Object faseTabla = jTable1.getValueAt(fila, 5); // Fase sigue siendo columna 4
        if (faseTabla != null) {
            
           //Asigna la fase dependiendo de la fase seleccionada del combobox
            String faseStr = faseTabla.toString();
            if (faseStr.equals("Registrado")) {
                cboFase.setSelectedIndex(0);
            } else if (faseStr.equals("Proceso")) {
                cboFase.setSelectedIndex(1);
            } else if (faseStr.equals("Solucionado")) {
                cboFase.setSelectedIndex(2);
            }
        }

        //Carga la solución en el campo 
        txtSolucion.setText((String) jTable1.getValueAt(fila, 5));
    }
}

    //Guarda los cambios seleccionados sobre el error seleccionado
    private void guardarCambios() {
        int fila = jTable1.getSelectedRow(); //Obtiene la fila seleccionada
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un error de la tabla");
            return;
        }
        //Trae los datos de la fila seleccionada
        int id = (int) jTable1.getValueAt(fila, 0);
        String titulo = (String) jTable1.getValueAt(fila, 1);
        String descripcion = (String) jTable1.getValueAt(fila, 2);
        Severidad sev = (Severidad) jTable1.getValueAt(fila, 3);
        //String usuario = (String) jTable1.getValueAt(fila, 5);
        String solucion = txtSolucion.getText().trim();

        // Obtener la fase seleccionada 
        Fase faseSeleccionada = Fase.REGISTRADO; 
        int index = cboFase.getSelectedIndex();
        if (index == 0) {
            faseSeleccionada = Fase.REGISTRADO;
        } else if (index == 1) {
            faseSeleccionada = Fase.PROCESO;
        } else if (index == 2) {
            faseSeleccionada = Fase.SOLUCIONADO;
        }

        // Crear objeto ErrorTicket y actualizar
        ErrorTicket error = new ErrorTicket(
    titulo,
    descripcion,
    sev,
    faseSeleccionada
);
        //Asigna solución y ID al error
        error.setSolucion(solucion);
        error.setId(id);
        
        //Actualiza el error en la base de datos
        new GestorErrores().actualizarError(error);
        JOptionPane.showMessageDialog(this, "Error actualizado correctamente");
        //Actualiza los datos de la tabla
        cargarTabla();
    }

    /*  private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cboFase = new javax.swing.JComboBox<>(new String[]{"Registrado", "Proceso", "Solucionado"});
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSolucion = new javax.swing.JTextArea();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Fase");
        jLabel2.setText("Errores");
        jLabel3.setText("Solución");

        txtSolucion.setColumns(20);
        txtSolucion.setRows(5);
        jScrollPane2.setViewportView(txtSolucion);

        btnSalir.setText("Salir");
        btnGuardar.setText("Guardar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30)
                                .addComponent(jLabel1)
                                .addGap(30)
                                .addComponent(cboFase, 120, 120, 120)
                                .addGap(30)
                                .addComponent(jLabel3)
                                .addGap(20)
                                .addComponent(jScrollPane2, 300, 300, 300)
                        )
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(100)
                                .addComponent(btnGuardar, 100, 100, 100)
                                .addGap(50)
                                .addComponent(btnSalir, 100, 100, 100)
                        )
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, 200, 200, 200)
                                .addGap(10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel3))
                                .addGap(5)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cboFase, 30, 30, 30)
                                        .addComponent(jScrollPane2, 100, 100, 100))
                                .addGap(20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnGuardar)
                                        .addComponent(btnSalir))
                        )
        );

        getContentPane().add(jPanel1);
        pack();
        setLocationRelativeTo(null);
    }*/

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        btnRegresarPrin = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

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

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        jLabel1.setText("Fase");

        cboFase.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        cboFase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Registrado", "Proceso", "Solucionado" }));

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel2.setText("Errores");

        lblSolucion.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        lblSolucion.setText("Solucion");

        txtSolucion.setColumns(20);
        txtSolucion.setRows(5);
        jScrollPane2.setViewportView(txtSolucion);

        btnSalir.setBackground(new java.awt.Color(255, 0, 0));
        btnSalir.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        btnGuardar.setBackground(new java.awt.Color(0, 0, 102));
        btnGuardar.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnRegresarPrin.setBackground(new java.awt.Color(255, 51, 51));
        btnRegresarPrin.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        btnRegresarPrin.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresarPrin.setText("Regresar");
        btnRegresarPrin.addActionListener(this::btnRegresarPrinActionPerformed);

        jPanel2.setBackground(new java.awt.Color(0, 0, 102));
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

        jPanel4.setBackground(new java.awt.Color(0, 0, 102));

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
                .addGap(33, 33, 33)
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

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
    
        System.exit(0);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
            
        
            
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnRegresarPrinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarPrinActionPerformed
        new  FrmPrincipal().setVisible(true);
        this.dispose();

        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegresarPrinActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmTableErrores().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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

