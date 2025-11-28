package modelos;

public class BalanceGeneral {

    private String codigo;
    private String cuenta;
    private String monto; // usas Strings en el DAO; aquí mantenemos ese tipo

    public BalanceGeneral() {
    }

    public BalanceGeneral(String codigo, String cuenta, String monto) {
        this.codigo = codigo;
        this.cuenta = cuenta;
        this.monto = monto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String Codigo) {
        this.codigo = Codigo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String Cuenta) {
        this.cuenta = Cuenta;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String Monto) {
        this.monto = Monto;
    }

}
