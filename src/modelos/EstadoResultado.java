/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author guill
 */
public class EstadoResultado {
    
    private String ventas_Totales;
    private String costo_Ventas;
    private String utilidad_Bruta;
    
    private String gastos_Admin;
    private String gastos_Ventas;
    private String utilidad_Operativa;
    
    private String ingresos_Finan;
    private String gastos_Finan;
    private String utilidad_Antes;
    private String utilidad_Neta;

    public EstadoResultado() {
    }

    public EstadoResultado(String ventas_Totales, String costo_Ventas, String utilidad_Bruta, String gastos_Admin, String gastos_Ventas, String utilidad_Operativa, String ingresos_Finan, String gastos_Finan, String utilidad_Antes, String utilidad_Neta) {
        this.ventas_Totales = ventas_Totales;
        this.costo_Ventas = costo_Ventas;
        this.utilidad_Bruta = utilidad_Bruta;
        this.gastos_Admin = gastos_Admin;
        this.gastos_Ventas = gastos_Ventas;
        this.utilidad_Operativa = utilidad_Operativa;
        this.ingresos_Finan = ingresos_Finan;
        this.gastos_Finan = gastos_Finan;
        this.utilidad_Antes = utilidad_Antes;
        this.utilidad_Neta = utilidad_Neta;
    }

    public String getVentas_Totales() {
        return ventas_Totales;
    }

    public void setVentas_Totales(String ventas_Totales) {
        this.ventas_Totales = ventas_Totales;
    }

    public String getCosto_Ventas() {
        return costo_Ventas;
    }

    public void setCosto_Ventas(String costo_Ventas) {
        this.costo_Ventas = costo_Ventas;
    }

    public String getUtilidad_Bruta() {
        return utilidad_Bruta;
    }

    public void setUtilidad_Bruta(String utilidad_Bruta) {
        this.utilidad_Bruta = utilidad_Bruta;
    }

    public String getGastos_Admin() {
        return gastos_Admin;
    }

    public void setGastos_Admin(String gastos_Admin) {
        this.gastos_Admin = gastos_Admin;
    }

    public String getGastos_Ventas() {
        return gastos_Ventas;
    }

    public void setGastos_Ventas(String gastos_Ventas) {
        this.gastos_Ventas = gastos_Ventas;
    }

    public String getUtilidad_Operativa() {
        return utilidad_Operativa;
    }

    public void setUtilidad_Operativa(String utilidad_Operativa) {
        this.utilidad_Operativa = utilidad_Operativa;
    }

    public String getIngresos_Finan() {
        return ingresos_Finan;
    }

    public void setIngresos_Finan(String ingresos_Finan) {
        this.ingresos_Finan = ingresos_Finan;
    }

    public String getGastos_Finan() {
        return gastos_Finan;
    }

    public void setGastos_Finan(String gastos_Finan) {
        this.gastos_Finan = gastos_Finan;
    }

    public String getUtilidad_Antes() {
        return utilidad_Antes;
    }

    public void setUtilidad_Antes(String utilidad_Antes) {
        this.utilidad_Antes = utilidad_Antes;
    }

    public String getUtilidad_Neta() {
        return utilidad_Neta;
    }

    public void setUtilidad_Neta(String utilidad_Neta) {
        this.utilidad_Neta = utilidad_Neta;
    }
 
    
}
