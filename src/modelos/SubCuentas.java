/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author guill
 */
public class SubCuentas {
    private String cod_subcuenta;
    private String nombre_sub;
    private Cuentas_Mayor cod_mayor;

    public SubCuentas() {
    }

    public SubCuentas(String cod_subcuenta) {
        this.cod_subcuenta = cod_subcuenta;
    }

    public SubCuentas(String cod_subcuenta, String nombre_sub) {
        this.cod_subcuenta = cod_subcuenta;
        this.nombre_sub = nombre_sub;
    }

    public SubCuentas(String cod_subcuenta, String nombre_sub, Cuentas_Mayor cod_mayor) {
        this.cod_subcuenta = cod_subcuenta;
        this.nombre_sub = nombre_sub;
        this.cod_mayor = cod_mayor;
    }

    public String getCod_subcuenta() {
        return cod_subcuenta;
    }

    public void setCod_subcuenta(String cod_subcuenta) {
        this.cod_subcuenta = cod_subcuenta;
    }

    public String getNombre_sub() {
        return nombre_sub;
    }

    public void setNombre_sub(String nombre_sub) {
        this.nombre_sub = nombre_sub;
    }

    public Cuentas_Mayor getCod_mayor() {
        return cod_mayor;
    }

    public void setCod_mayor(Cuentas_Mayor cod_mayor) {
        this.cod_mayor = cod_mayor;
    }
    
}
