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
public class Cuentas_Principales {
    private String cod_Principal;
    private String nombre_Principal;
    private Catalogo cod_principal;
    private ArrayList<Cuentas_Mayor> listaMayor;

    public Cuentas_Principales() {
    }

    public Cuentas_Principales(String cod_Principal) {
        this.cod_Principal = cod_Principal;
    }

    public Cuentas_Principales(String cod_Principal, String nombre_Principal) {
        this.cod_Principal = cod_Principal;
        this.nombre_Principal = nombre_Principal;
    }

    public Cuentas_Principales(String cod_Principal, String nombre_Principal, Catalogo cod_principal) {
        this.cod_Principal = cod_Principal;
        this.nombre_Principal = nombre_Principal;
        this.cod_principal = cod_principal;
    }

    public Cuentas_Principales(String cod_Principal, String nombre_Principal, Catalogo cod_principal, ArrayList<Cuentas_Mayor> listaMayor) {
        this.cod_Principal = cod_Principal;
        this.nombre_Principal = nombre_Principal;
        this.cod_principal = cod_principal;
        this.listaMayor = listaMayor;
    }

    public String getCod_Principal() {
        return cod_Principal;
    }

    public void setCod_Principal(String cod_Principal) {
        this.cod_Principal = cod_Principal;
    }

    public String getNombre_Principal() {
        return nombre_Principal;
    }

    public void setNombre_Principal(String nombre_Principal) {
        this.nombre_Principal = nombre_Principal;
    }

    public Catalogo getCod_principal() {
        return cod_principal;
    }

    public void setCod_principal(Catalogo cod_principal) {
        this.cod_principal = cod_principal;
    }

    public ArrayList<Cuentas_Mayor> getListaMayor() {
        return listaMayor;
    }

    public void setListaMayor(ArrayList<Cuentas_Mayor> listaMayor) {
        this.listaMayor = listaMayor;
    }
    
}
