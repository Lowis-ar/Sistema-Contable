/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import daos.DaoEstadoResultado;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import modelos.AjusteIVA;
import modelos.CierreContable;
import modelos.PartidaCierre;
import vistas.VistaEstadoResultado;

/**
 *
 * @author guill
 */
public class a {

    public static void main(String[] args) throws SQLException {
        VistaEstadoResultado v = new VistaEstadoResultado(new JFrame(), true);
        ControladorEstadoResultado c = new ControladorEstadoResultado(v);
        v.iniciar();
    }
//        CierreContable c = new CierreContable();
//        AjusteIVA ajuste = new AjusteIVA();
//        double a = c.obtener_Ventas();
//        
//        //antes de esto tiene que sacar los reportes y el backup de la base antes del ajuste de iva.
//        ajuste.ajustarIva();
//
//        c.ventasNetas();
//        c.comprasTotales();
//        c.comprasNetas();
//        //
//        c.mercaderiaDisponible();
//        
//        c.costoVenta(200000.00);
//        //
//        c.utilidadBruta();
//        c.liquidarVentas(a);
//        
//        c.liquidarGastos();
//        c.liquidarOtrosGastos();
//        
//        c.utilidadEjercicio();
//        
//        //para hacer partida de cierre;
//        c.partidaCierre();
//        //luego de ejecutar la partida de cierre, tiene que hacer un backup de la base tiniendo todos los datos de los ajustes y el cierre
//        
//        
//        
//        
//        
//        //hacer backup de la base 
//       //antes de que drop a la base se tiene que hacer esta parte
//        int numeroPartida = c.obtenerNumeroPartida();
//        ArrayList<PartidaCierre> lista1 = c.obtenerSaldoActivoCierre(numeroPartida);
//        ArrayList<PartidaCierre> lista2 = c.obtenerSaldoPasivoCierre(numeroPartida);
//        ArrayList<PartidaCierre> lista3 = c.obtenerSaldoPatrimonioCierre(numeroPartida);
//        
//
////hacer drop de la base
//        //restaurar la base nueva, que seria la vacia
//        
//        //hacer este insert.
//        for (PartidaCierre x : lista1) {
//            c.insertarTransaccion(1, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE APERTURA", "Debe");
//        }
//        for (PartidaCierre x : lista2) {
//            c.insertarTransaccion(1, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE APERTURA", "Debe");
//        }
//        for (PartidaCierre x : lista3) {
//            c.insertarTransaccion(1, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE APERTURA", "Haber");
//        }
//
//    }
}
