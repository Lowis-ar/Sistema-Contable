/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistas;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/**
 *
 * @author kevin
 */
public class VistaSalir extends JFrame implements ActionListener {
// atributos

    public JProgressBar myBarra;  // barra de progreso
    public int contador; // para contar
    JTextField nombre; // campo de texto

    public VistaSalir(String titulo) {
        this.setTitle(titulo); // titulo de la ventana
        this.setSize(390, 100); // tamaño
        this.setLocationRelativeTo(this); // ubica la ventana en el centro
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//cerrar los proceso
        this.setResizable(false); // habilita el boton de minimizar
        Container contenedor = this.getContentPane();
        contenedor.setLayout(null);// null, porque quiero poner los componentes

        myBarra = new JProgressBar(0, 100);
        myBarra.setStringPainted(true);
        myBarra.setBounds(10, 10, 350, 30);
        contador = 0;

        contenedor.add(myBarra);

        Hilo hilo = new Hilo(); //hilo es un objeto con capacidad de
        //correr en forma concurrente
        hilo.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private class Hilo extends Thread {

        public void run() { //constituye el cuerpo de un hilo en ejecución
            myBarra.setIndeterminate(false);
            while (contador < 100) {
                try {
                    Thread.sleep(150); // ponga al hilo en curso a dormir                 
                } catch (Exception ex) {
                }
                contador = contador + 10; // cuenta de 10 en 10
                myBarra.setValue(contador); // para que muestre el valor
                myBarra.repaint(); //pinta el valor en la barra
            }
            System.exit(0);
        }
    }
}
