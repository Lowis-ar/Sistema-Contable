/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import daos.DaoCatalogo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import modelos.Cuenta;
import vistas.AgregarCatalogo;
import vistas.AgregarSubcuenta;
import vistas.Catalogos;
import Vistas.Dashboard;


/**
 *
 * @author guill
 */
public class ControladorMostrarCatalogo extends MouseAdapter implements ActionListener,
        MouseListener, KeyListener, ItemListener {

    Catalogos frmMostrar;
    ArrayList<Cuenta> listaCuenta;
    DefaultTableModel modelo;
    DaoCatalogo daoCatalogo;
    Cuenta cuentita;

    public ControladorMostrarCatalogo(Catalogos frmMostrar) {
        this.frmMostrar = frmMostrar;
        this.frmMostrar.buscar.addKeyListener(this);
        this.frmMostrar.tablaMostrar.addMouseListener(this);
       // this.frmMostrar.salir.addActionListener(this);
        listaCuenta = new ArrayList<>();
        daoCatalogo = new DaoCatalogo();
        mostrar();
    }

    public void mostrar() {
        listaCuenta = daoCatalogo.selectCuentasJerarquicas();
        modelo = new DefaultTableModel();
        String titulos[] = {"CODIGO DE CUENTA", "NOMBRE DE LA CUENTA"};
        modelo.setColumnIdentifiers(titulos);
        int i = 0;
        for (Cuenta a : listaCuenta) {
            i++;
            Object datos[] = {a.getCodigo(), a.getNombre()};
            modelo.addRow(datos);
        }
        this.frmMostrar.tablaMostrar.setModel(modelo);
        this.frmMostrar.tablaMostrar.setDefaultEditor(Object.class, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       /* if (e.getSource() == this.frmMostrar.salir) {
            salir();
        }*/
    }

    public void salir() {
        this.frmMostrar.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == this.frmMostrar.buscar) {
            ArrayList<Cuenta> lista = daoCatalogo.buscarCuentasJerarquicas(this.frmMostrar.buscar.getText() + e.getKeyChar());
            if (lista.isEmpty()) {
                mostrar();
            } else {
                mostrarBusqueda(lista);
            }
        }
    }

    public void mostrarBusqueda(ArrayList<Cuenta> listita) {
        modelo = new DefaultTableModel();
        String titulos[] = {"CODIGO DE CUENTA", "NOMBRE DE LA CUENTA"};
        modelo.setColumnIdentifiers(titulos);
        int i = 1;
        for (Cuenta x : listita) {
            Object datos[] = {x.getCodigo(), x.getNombre()};
            modelo.addRow(datos);
            i++;
        }
        this.frmMostrar.tablaMostrar.setModel(modelo);
        this.frmMostrar.tablaMostrar.setDefaultEditor(Object.class, null);
    } 

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
