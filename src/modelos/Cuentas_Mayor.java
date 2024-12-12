/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.util.ArrayList;

/**
 *
 * @author guill
 */
public class Cuentas_Mayor {
    private String cod_Mayor;
    private String nombre_Mayor;
    private String naturaleza;
    private Cuentas_Principales cod_principal;
    private ArrayList<SubCuentas> listaSub;

    public Cuentas_Mayor() {
    }

    public Cuentas_Mayor(String cod_Mayor) {
        this.cod_Mayor = cod_Mayor;
    }

    public Cuentas_Mayor(String cod_Mayor, String nombre_Mayor, String naturaleza) {
        this.cod_Mayor = cod_Mayor;
        this.nombre_Mayor = nombre_Mayor;
        this.naturaleza = naturaleza;
    }

    public Cuentas_Mayor(String cod_Mayor, String nombre_Mayor, String naturaleza, Cuentas_Principales cod_principal) {
        this.cod_Mayor = cod_Mayor;
        this.nombre_Mayor = nombre_Mayor;
        this.naturaleza = naturaleza;
        this.cod_principal = cod_principal;
    }

    public Cuentas_Mayor(String cod_Mayor, String nombre_Mayor, String naturaleza, Cuentas_Principales cod_principal, ArrayList<SubCuentas> listaSub) {
        this.cod_Mayor = cod_Mayor;
        this.nombre_Mayor = nombre_Mayor;
        this.naturaleza = naturaleza;
        this.cod_principal = cod_principal;
        this.listaSub = listaSub;
    }

    public String getCod_Mayor() {
        return cod_Mayor;
    }

    public void setCod_Mayor(String cod_Mayor) {
        this.cod_Mayor = cod_Mayor;
    }

    public String getNombre_Mayor() {
        return nombre_Mayor;
    }

    public void setNombre_Mayor(String nombre_Mayor) {
        this.nombre_Mayor = nombre_Mayor;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public Cuentas_Principales getCod_principal() {
        return cod_principal;
    }

    public void setCod_principal(Cuentas_Principales cod_principal) {
        this.cod_principal = cod_principal;
    }

    public ArrayList<SubCuentas> getListaSub() {
        return listaSub;
    }

    public void setListaSub(ArrayList<SubCuentas> listaSub) {
        this.listaSub = listaSub;
    }
    
    
    
}
