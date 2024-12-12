/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.util.Date;

/**
 *
 * @author Mac
 */
public class detallesMayor {
    
    
    String idMayor;
    Date fecha; 
    String idSubCuenta;
    String nombreSub;
    int partida;
    String concepto;
    double monto;
    String transaccion;

    public detallesMayor() {
    }

    public detallesMayor(Date fecha, String idSubCuenta, String nombreSub, int partida, String concepto, double monto, String transaccion) {
        this.fecha = fecha;
        this.idSubCuenta = idSubCuenta;
        this.nombreSub = nombreSub;
        this.partida = partida;
        this.concepto = concepto;
        this.monto = monto;
        this.transaccion = transaccion;
    }

    public detallesMayor(String idMayor, Date fecha, String idSubCuenta, String nombreSub, int partida, String concepto, double monto, String transaccion) {
        this.idMayor = idMayor;
        this.fecha = fecha;
        this.idSubCuenta = idSubCuenta;
        this.nombreSub = nombreSub;
        this.partida = partida;
        this.concepto = concepto;
        this.monto = monto;
        this.transaccion = transaccion;
    }

    public String getIdMayor() {
        return idMayor;
    }

    public void setIdMayor(String idMayor) {
        this.idMayor = idMayor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdSubCuenta() {
        return idSubCuenta;
    }

    public void setIdSubCuenta(String idSubCuenta) {
        this.idSubCuenta = idSubCuenta;
    }

    public String getNombreSub() {
        return nombreSub;
    }

    public void setNombreSub(String nombreSub) {
        this.nombreSub = nombreSub;
    }

    public int getPartida() {
        return partida;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }
    
    
    
}
