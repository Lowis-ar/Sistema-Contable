
package modelos;


public class BalanzaComprobacion {
    
private String codigo;
    private String cuenta;
    private double debe;
    private double haber;
    private double saldoDeudor;
    private double saldoAcreedor;
    private double saldo;

    public BalanzaComprobacion() {
    }

    public BalanzaComprobacion(String codigo, String cuenta, double debe, double haber, double saldoDeudor, double saldoAcreedor, double saldo) {
        this.codigo = codigo;
        this.cuenta = cuenta;
        this.debe = debe;
        this.haber = haber;
        this.saldoDeudor = saldoDeudor;
        this.saldoAcreedor = saldoAcreedor;
        this.saldo = saldo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public double getDebe() {
        return debe;
    }

    public void setDebe(double debe) {
        this.debe = debe;
    }

    public double getHaber() {
        return haber;
    }

    public void setHaber(double haber) {
        this.haber = haber;
    }

    public double getSaldoDeudor() {
        return saldoDeudor;
    }

    public void setSaldoDeudor(double saldoDeudor) {
        this.saldoDeudor = saldoDeudor;
    }

    public double getSaldoAcreedor() {
        return saldoAcreedor;
    }

    public void setSaldoAcreedor(double saldoAcreedor) {
        this.saldoAcreedor = saldoAcreedor;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    
}
