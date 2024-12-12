package Controladores;

import Reportes.Jasper;
import Vistas.VistaBalanzaComprobacion;
import Vistas.VistaSubCuentas;
import daos.BalanzaComprobacionDAO;
import daos.Conexion;
import daos.ConnectionPool;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelos.BalanzaComprobacion;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ControladorBalanzaC extends MouseAdapter implements ActionListener,
        MouseListener, KeyListener, ItemListener {

    VistaBalanzaComprobacion vista;
    DefaultTableModel modelo;
    BalanzaComprobacionDAO daoBalanza;
    ArrayList<BalanzaComprobacion> lista;
    Jasper jasper = new Jasper();

    public ControladorBalanzaC(VistaBalanzaComprobacion vista) {
        this.vista = vista;
        this.vista.btnMostrar.addActionListener(this);
        this.vista.btnMostrarSubCuentas.addActionListener(this);
        this.vista.btnPdf.addActionListener(this);
        lista = new ArrayList();
        daoBalanza = new BalanzaComprobacionDAO();
    }

    public void mostrar() {
        this.vista.Tbalanza.setEnabled(false);

        double totalDebe = 0;
        double totalHaber = 0;
        double totalDeudor = 0;
        double totalAcreedor = 0;

        modelo = new DefaultTableModel();
        String titulo[] = {"CODIGO", "CUENTA", "DEBE", "HABER", "SALDO DEUDOR", "SALDO ACREEDOR"};
        modelo.setColumnIdentifiers(titulo);
        int i = 0;
        for (BalanzaComprobacion x : daoBalanza.obtenerBalanzaComprobacion()) {
            i++;
            Object datos[] = {x.getCodigo(), x.getCuenta(), x.getDebe(), x.getHaber(), x.getSaldoDeudor(), x.getSaldoAcreedor()};
            modelo.addRow(datos);
            totalDebe += x.getDebe();
            totalHaber += x.getHaber();
            totalDeudor += x.getSaldoDeudor();
            totalAcreedor += x.getSaldoAcreedor();
        }
        
        this.vista.Tbalanza.setModel(modelo);
        this.vista.lbDebe.setText(String.valueOf(totalDebe));
        this.vista.lbHaber.setText(String.valueOf(totalHaber));
        this.vista.lbDeudor.setText(String.valueOf(totalDeudor));
        this.vista.lbAcreedor.setText(String.valueOf(totalAcreedor));
      //  this.vista.tfAcreedor.setText(String.valueOf(totalAcreedor));

        System.out.println(totalDebe);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.btnMostrar) {
            mostrar();
        } else if (e.getSource() == this.vista.btnMostrarSubCuentas) {
       
        VistaSubCuentas vista1 = new VistaSubCuentas(new JFrame(), true);
       
        ControladorSubCuentas crt1 = new ControladorSubCuentas(vista1);
       
        vista1.setVisible(true);
    }else if (e.getSource() == this.vista.btnPdf) {
         vista.setVisible(false);
          jasper.Reporte(2);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

}
