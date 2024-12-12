package modelos;

/**
 *
 * @author Luis
 */
public class BalanceGeneral {

    private String codigo;
    private String tipo;
    private String cuenta;
    private String Monto;

    public BalanceGeneral() {
    }

    public BalanceGeneral(String codigo, String tipo, String cuenta, String Monto) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.cuenta = cuenta;
        this.Monto = Monto;
    }

    public BalanceGeneral(String tipo, String cuenta, String Monto) {
        this.tipo = tipo;
        this.cuenta = cuenta;
        this.Monto = Monto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getMonto() {
        return Monto;
    }

    public void setMonto(String Monto) {
        this.Monto = Monto;
    }

}
