/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vistas;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kevin
 */
public class VistaMostrarLibroDiario extends javax.swing.JDialog {

    DefaultTableModel tb = new DefaultTableModel();

    /**
     * Creates new form LibroDiarioVista
     * @param parent
     * @param modal
     */
    public VistaMostrarLibroDiario(java.awt.Frame parent, boolean modal,
            String titulo) {
        super(parent, modal);
        initComponents();
        lbTitulo.setText(titulo);
    }

    public VistaMostrarLibroDiario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelFondo = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lbTitulo = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        tfSumaDebe = new javax.swing.JTextField();
        tfSumaHaber = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDatos = new rojerusan.RSTableMetro();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        lbCodigo = new javax.swing.JLabel();
        tfBuscarPartida = new javax.swing.JTextField();
        lbBuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        PanelFondo.setBackground(new java.awt.Color(255, 255, 255));
        PanelFondo.setPreferredSize(new java.awt.Dimension(945, 574));
        PanelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelFondo.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1100, 10));
        PanelFondo.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 1100, 10));

        lbTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PanelFondo.add(lbTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 1100, 30));

        lbTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTotal.setText("Total");
        PanelFondo.add(lbTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 530, -1, 30));

        tfSumaDebe.setEditable(false);
        tfSumaDebe.setBackground(new java.awt.Color(255, 255, 255));
        tfSumaDebe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tfSumaDebe.setForeground(new java.awt.Color(153, 0, 0));
        tfSumaDebe.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfSumaDebe.setBorder(null);
        PanelFondo.add(tfSumaDebe, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 530, 140, 30));

        tfSumaHaber.setEditable(false);
        tfSumaHaber.setBackground(new java.awt.Color(255, 255, 255));
        tfSumaHaber.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tfSumaHaber.setForeground(new java.awt.Color(153, 0, 0));
        tfSumaHaber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfSumaHaber.setBorder(null);
        PanelFondo.add(tfSumaHaber, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 530, 130, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("$");
        PanelFondo.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 530, 10, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("$");
        PanelFondo.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 530, 10, 30));

        tbDatos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDatos.setColorBackgoundHead(new java.awt.Color(153, 0, 0));
        tbDatos.setRowHeight(25);
        jScrollPane2.setViewportView(tbDatos);

        PanelFondo.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1080, 420));

        btnAnterior.setBackground(new java.awt.Color(255, 255, 255));
        btnAnterior.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnAnterior.setForeground(new java.awt.Color(153, 0, 0));
        btnAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/off_16.png"))); // NOI18N
        btnAnterior.setText("<-- Partida Anterior ");
        btnAnterior.setBorder(null);
        btnAnterior.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PanelFondo.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 170, 40));

        btnSiguiente.setBackground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnSiguiente.setForeground(new java.awt.Color(153, 0, 0));
        btnSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/on_16.png"))); // NOI18N
        btnSiguiente.setText("Siguiente Partida -->");
        btnSiguiente.setBorder(null);
        btnSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PanelFondo.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, 180, 40));

        lbCodigo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbCodigo.setText("Buscar por número de Partida");
        PanelFondo.add(lbCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        tfBuscarPartida.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tfBuscarPartida.setForeground(new java.awt.Color(153, 0, 0));
        tfBuscarPartida.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        PanelFondo.add(tfBuscarPartida, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 50, 30));

        lbBuscar.setBackground(new java.awt.Color(255, 255, 255));
        lbBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/buscar.png"))); // NOI18N
        lbBuscar.setBorder(null);
        lbBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PanelFondo.add(lbBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, 30, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(LibroDiarioVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LibroDiarioVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LibroDiarioVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LibroDiarioVista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                LibroDiarioVista dialog = new LibroDiarioVista(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelFondo;
    public javax.swing.JButton btnAnterior;
    public javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JButton lbBuscar;
    public javax.swing.JLabel lbCodigo;
    public javax.swing.JLabel lbTitulo;
    private javax.swing.JLabel lbTotal;
    public rojerusan.RSTableMetro tbDatos;
    public javax.swing.JTextField tfBuscarPartida;
    public javax.swing.JTextField tfSumaDebe;
    public javax.swing.JTextField tfSumaHaber;
    // End of variables declaration//GEN-END:variables
}