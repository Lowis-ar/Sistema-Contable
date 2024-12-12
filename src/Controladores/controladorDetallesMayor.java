/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Vistas.VistadetallesMayor;
import daos.DaoCatalogo;
import daos.DaoMayor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelos.LibroMayor;
import modelos.detallesMayor;

/**
 *
 * @author Mac
 */
public class controladorDetallesMayor  extends MouseAdapter implements ActionListener,
        MouseListener, KeyListener, ItemListener{

    
    VistadetallesMayor frmDetalles;
    ControladorLibroMayor ctrMayor;
    DefaultTableModel modelo;
    LibroMayor cuentaSeleccionada;
    DaoMayor dao;
    

  

    public controladorDetallesMayor(VistadetallesMayor frmDetalles, ControladorLibroMayor ctrMayor, LibroMayor cuentaSeleccionada) {
        this.frmDetalles = frmDetalles;
        this.ctrMayor = ctrMayor;
        this.cuentaSeleccionada = cuentaSeleccionada;
         dao = new DaoMayor();
        
        this.frmDetalles.CuentaMayor.setText(this.cuentaSeleccionada.getNombre());
        this.frmDetalles.tbDatosDetalles.addMouseListener(this);
        mostrar();
    }
    
   
    
    public void mostrar(){
        ArrayList<detallesMayor> lista = dao.MostrarDetalles(this.cuentaSeleccionada.getNombre());
         
                 
                 mostrarBusqueda(lista);
          
    }
    
    
    
    
    
    public void mostrarBusqueda(ArrayList<detallesMayor> list){
        modelo = new DefaultTableModel();
        String titulos[] = {"ID SUB CUENTA", "NOMBRE","PARTIDA","FECHA","CONCEPTO","MONTO","TRANSACCION"};
        modelo.setColumnIdentifiers(titulos);
       
        for(detallesMayor x : list){
            Object datos[] = {x.getIdSubCuenta(), x.getNombreSub(),x.getPartida(),x.getFecha(),x.getConcepto(),x.getMonto(),x.getTransaccion()};
            modelo.addRow(datos);
         
        }
        this.frmDetalles.tbDatosDetalles.setModel(modelo);
        this.frmDetalles.tbDatosDetalles.setDefaultEditor(Object.class, null);
    } 
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
   
    
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
