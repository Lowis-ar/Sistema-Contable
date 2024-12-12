package modelos;

import java.util.Date;

/**
 *
 * @author kevin
 */
public class LibroDiario {

    private int numeroPartida;
    private Date fecha;
    private int codSubcuenta;
    private String nombreCuenta;
    private double monto;
    private String transaccion;
    private String concepto;

    public LibroDiario() {
    }
    
    

    public LibroDiario(int numeroPartida, Date fecha, int codSubcuenta, String nombreCuenta, double monto, String transaccion, String concepto) {
        this.numeroPartida = numeroPartida;
        this.fecha = fecha;
        this.codSubcuenta = codSubcuenta;
        this.nombreCuenta = nombreCuenta;
        this.monto = monto;
        this.transaccion = transaccion;
        this.concepto = concepto;
    }

    public int getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(int numeroPartida) {
        this.numeroPartida = numeroPartida;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCodSubcuenta() {
        return codSubcuenta;
    }

    public void setCodSubcuenta(int codSubcuenta) {
        this.codSubcuenta = codSubcuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
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

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    
    
    

    
}
