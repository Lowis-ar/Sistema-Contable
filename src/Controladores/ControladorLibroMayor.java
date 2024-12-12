package Controladores;

import Reportes.Jasper;
import Vistas.VistaMayor;
import Vistas.VistadetallesMayor;

import daos.Conexion;
import daos.DaoMayor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelos.LibroMayor;
import modelos.detallesMayor;

public class ControladorLibroMayor extends MouseAdapter implements ActionListener,
        MouseListener, KeyListener, ItemListener {

    VistaMayor frmMayor;
    DaoMayor daoMayor;
    LibroMayor mayor;
    DefaultTableModel modelo;
    ArrayList<LibroMayor> listaMayor;
    LibroMayor cuentaSeleccionada;
      ArrayList<detallesMayor> listSub;

    public ControladorLibroMayor(VistaMayor frmMayor)  {
        this.frmMayor = frmMayor;
        this.frmMayor.btnDetalles.addActionListener(this);
        this.frmMayor.btnGuardar.addActionListener(this);
        this.frmMayor.btnGuardarSub.addActionListener(this);
        this.frmMayor.btnReporteM.addActionListener(this);
        this.frmMayor.btnReporteMD.addActionListener(this);
        this.frmMayor.tbDatos.addMouseListener(this);
        
        listSub = new ArrayList<>();
        listaMayor = new ArrayList();
        daoMayor = new DaoMayor();
       
        mostrar();
       
    }

    public ControladorLibroMayor(VistaMayor frmMayor, LibroMayor mayor) {
        this.frmMayor = frmMayor;
        this.mayor = mayor;
         listaMayor = new ArrayList();
        daoMayor = new DaoMayor();
        this.frmMayor.tbDatos.addMouseListener(this);
    }
    
    
    
    

    public void mostrar()  {

        listaMayor = (daoMayor.seleccionaDatos());
        modelo = new DefaultTableModel();
        String titulos[] = {"COD CUENTA", "CUENTA", "DEBE", "HABER", "SALDO"};
        modelo.setColumnIdentifiers(titulos);

        for (LibroMayor x : listaMayor.toArray(new LibroMayor[0])) {

            Object datos[] = {x.getCodigo(), x.getNombre(), x.getDebe(), x.getHaber(), x.getSaldo()};
            modelo.addRow(datos);
        }
        this.frmMayor.tbDatos.setModel(modelo);
        this.frmMayor.tbDatos.setDefaultEditor(Object.class, null);

    }
    
    public void guardar(){
        listaMayor = (daoMayor.seleccionaDatos());
     for (LibroMayor x : listaMayor.toArray(new LibroMayor[0])) {
            LibroMayor obj = new LibroMayor(x.getCodigo(), x.getNombre(), x.getDebe(), x.getHaber(), x.getSaldo());
            daoMayor.insertar(obj);
        }
     
        
    }
    
    public void guardarSub(){
        listSub = (daoMayor.seleccionaDatosSub());
     for (detallesMayor x : listSub.toArray(new detallesMayor[0])) {
            detallesMayor obj = new detallesMayor(x.getIdMayor(), x.getFecha(), x.getIdSubCuenta(), x.getNombreSub(), x.getPartida(),x.getConcepto(),x.getMonto(),x.getTransaccion());
            daoMayor.insertarSub(obj);
        }
     
        
    }
    
     @Override
    public void mouseClicked(MouseEvent e) {
         if (e.getSource()==this.frmMayor.tbDatos) {
             int fila = this.frmMayor.tbDatos.getSelectedRow();
             if (fila>=0) {
                 cuentaSeleccionada = listaMayor.get(fila);
                 this.frmMayor.btnDetalles.setEnabled(true);
                 
             }
         }
    }
    
    

  @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.frmMayor.btnDetalles) {
        VistadetallesMayor frm = new VistadetallesMayor(new JFrame(), true);
        controladorDetallesMayor ctrMayor = new controladorDetallesMayor(frm, this, cuentaSeleccionada);
        frm.iniciar();
    } else if (e.getSource() == this.frmMayor.btnGuardar) {
        // Mostrar cuadro de confirmación con botones Sí y No
        int opcion = JOptionPane.showConfirmDialog(
            frmMayor, 
            "ESTOS DATOS SOLO SE PUEDEN ALMACENAR UNA VEZ POR CICLO CONTABLE. ¿ESTÁ SEGURO QUE LOS QUIERE GUARDAR?", 
            "CONFIRMAR ACCIÓN", 
            JOptionPane.YES_NO_OPTION
        );

        // Comprobar qué botón presionó el usuario
        if (opcion == JOptionPane.YES_OPTION) {
            guardar(); // Llamar al método para guardar los datos
            JOptionPane.showMessageDialog(frmMayor, "DATOS ALMACENADOS", "LISTO", JOptionPane.INFORMATION_MESSAGE);
            frmMayor.btnGuardar.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(frmMayor, "OPERACIÓN CANCELADA", "CANCELADO", JOptionPane.WARNING_MESSAGE);
        }
    }else if(e.getSource() == this.frmMayor.btnGuardarSub){
                // Mostrar cuadro de confirmación con botones Sí y No
        int opcion = JOptionPane.showConfirmDialog(
            frmMayor, 
            "ESTOS DATOS SOLO SE PUEDEN ALMACENAR UNA VEZ POR CICLO CONTABLE. ¿ESTÁ SEGURO QUE LOS QUIERE GUARDAR?", 
            "CONFIRMAR ACCIÓN", 
            JOptionPane.YES_NO_OPTION
        );

        // Comprobar qué botón presionó el usuario
        if (opcion == JOptionPane.YES_OPTION) {
            guardarSub(); // Llamar al método para guardar los datos
            JOptionPane.showMessageDialog(frmMayor, "DATOS ALMACENADOS", "LISTO", JOptionPane.INFORMATION_MESSAGE);
            frmMayor.btnGuardarSub.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(frmMayor, "OPERACIÓN CANCELADA", "CANCELADO", JOptionPane.WARNING_MESSAGE);
        }
    }else if(e.getSource() == this.frmMayor.btnReporteM){
        Jasper jas = new Jasper();
        frmMayor.setVisible(false);
        jas.Reporte(5);
    }else if(e.getSource() == this.frmMayor.btnReporteMD){
        Jasper jas = new Jasper();
        frmMayor.setVisible(false);
        jas.Reporte(4);
    }
}


    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
