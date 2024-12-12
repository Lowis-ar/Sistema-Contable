/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import daos.DaoSubCuenta;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelos.Cuentas_Mayor;
import modelos.SubCuentas;
import vistas.AgregarSubcuenta;

/**
 *
 * @author guill
 */
public class ControladorSubCuenta extends MouseAdapter implements ActionListener,
        MouseListener, KeyListener, ItemListener {

    AgregarSubcuenta frmSub;
    ArrayList<SubCuentas> listaSub;
    ControladorMostrarCatalogo ctrol;
    DaoSubCuenta daoSub;
    SubCuentas subcuentas;
    DefaultTableModel modelo;

    public ControladorSubCuenta(AgregarSubcuenta frmSub) {
        this.frmSub = frmSub;
        this.ctrol = ctrol;
        this.frmSub.registrar.addActionListener(this);
        this.frmSub.editar.addActionListener(this);
        this.frmSub.cancelar.addActionListener(this);
//        this.frmSub.salir.addActionListener(this);
        this.frmSub.tablita.addMouseListener(this);
        this.frmSub.editar.setEnabled(false);
        daoSub = new DaoSubCuenta();
        mostrar();
    }

    public ControladorSubCuenta(AgregarSubcuenta frmSub, ControladorMostrarCatalogo ctrol, SubCuentas subcuentas) {
        this.frmSub = frmSub;
        this.ctrol = ctrol;
        this.subcuentas = subcuentas;

        this.frmSub.registrar.addActionListener(this);
        this.frmSub.editar.addActionListener(this);
        this.frmSub.cancelar.addActionListener(this);
//        this.frmSub.salir.addActionListener(this);

        daoSub = new DaoSubCuenta();
    }

    public void mostrar() {
        listaSub = daoSub.selectTodoSubCuenta();
        modelo = new DefaultTableModel();
        String titulos[] = {"CODIGO", "NOMBRE", "CUENTA MAYOR"};
        modelo.setColumnIdentifiers(titulos);
        int i = 0;
        for (SubCuentas x : listaSub) {
            i++;
            Object datos[] = {x.getCod_subcuenta(), x.getNombre_sub(), x.getCod_mayor().getNombre_Mayor()};
            modelo.addRow(datos);
        }
        this.frmSub.tablita.setModel(modelo);
        this.frmSub.tablita.setDefaultEditor(Object.class, null);
    }

    public void guardar() {
        String codigo = this.frmSub.codigo.getText();
        String nombre = this.frmSub.nombreCuenta.getText();
        String cod_mayor = (String) this.frmSub.cod_mayor.getSelectedItem();

        if (codigo.isEmpty() || nombre.isEmpty() || cod_mayor == null) {
            JOptionPane.showMessageDialog(frmSub, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Cuentas_Mayor sub = daoSub.select_cod_mayor(cod_mayor);

        if (this.subcuentas == null) {
            SubCuentas s = new SubCuentas(codigo, nombre, sub);
            daoSub.insertSubCuenta(s);
        } else {
            this.subcuentas.setCod_subcuenta(codigo);
            this.subcuentas.setNombre_sub(nombre);
            this.subcuentas.setCod_mayor(sub);
            daoSub.updateSubCuenta(subcuentas);
        }
        mostrar();
        limpiarFormulario();
        this.subcuentas = null;
    }

// Método para limpiar los campos del formulario
    private void limpiarFormulario() {
        this.frmSub.codigo.setText(""); 
        this.frmSub.nombreCuenta.setText("");
    }

    public void limpiar() {
        this.frmSub.codigo.setText("");
        this.frmSub.nombreCuenta.setText("");
        this.subcuentas = null;
        this.frmSub.editar.setEnabled(false);

    }

    public void agregar() {
        if (this.subcuentas != null) {
            this.frmSub.codigo.setText(this.subcuentas.getCod_subcuenta());
            this.frmSub.nombreCuenta.setText(this.subcuentas.getNombre_sub());
            this.frmSub.cod_mayor.setSelectedItem(this.subcuentas.getCod_mayor().getNombre_Mayor());
            this.frmSub.codigo.setEditable(false);
        }
    }

    public void eliminar() {
        if (this.subcuentas != null) {
            int confirmacion = JOptionPane.showConfirmDialog(frmSub, "¿Estás seguro de que deseas eliminar esta cuenta?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminado = daoSub.deleteSub(this.subcuentas.getCod_subcuenta());
                if (eliminado) {
                    JOptionPane.showMessageDialog(frmSub, "Registro eliminado con éxito");
                    mostrar();
                    limpiar();
                    this.subcuentas = null;
                } else {
                    JOptionPane.showMessageDialog(frmSub, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frmSub, "No hay ninguna cuenta seleccionada para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void salir() {
        this.frmSub.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frmSub.registrar) {
            guardar();
            mostrar();
            this.frmSub.codigo.setEnabled(true);
            this.subcuentas = null;
        }
        if (e.getSource() == this.frmSub.editar) {
            agregar();
        } else if (e.getSource() == this.frmSub.cancelar) {
            limpiar();
            this.frmSub.codigo.setEditable(true);
        } 
//        else if (e.getSource() == this.frmSub.salir) {
//            salir();
//        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.frmSub.tablita) {
            int fila = this.frmSub.tablita.getSelectedRow();
            if (fila >= 0 && e.getClickCount() == 2) {
                this.subcuentas = listaSub.get(fila);
                this.frmSub.editar.setEnabled(true);
            }
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
