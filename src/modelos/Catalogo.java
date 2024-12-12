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
public class Catalogo {
    private String cod_catalogo;
    private String nombre;
    private ArrayList<Cuentas_Principales> listaPrincipal;
    
    public Catalogo() {
    }

    public Catalogo(String cod_catalogo) {
        this.cod_catalogo = cod_catalogo;
    }

    public Catalogo(String cod_catalogo, String nombre) {
        this.cod_catalogo = cod_catalogo;
        this.nombre = nombre;
    }

    public Catalogo(String cod_catalogo, String nombre, ArrayList<Cuentas_Principales> listaPrincipal) {
        this.cod_catalogo = cod_catalogo;
        this.nombre = nombre;
        this.listaPrincipal = listaPrincipal;
    }

    public String getCod_catalogo() {
        return cod_catalogo;
    }

    public void setCod_catalogo(String cod_catalogo) {
        this.cod_catalogo = cod_catalogo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Cuentas_Principales> getListaPrincipal() {
        return listaPrincipal;
    }

    public void setListaPrincipal(ArrayList<Cuentas_Principales> listaPrincipal) {
        this.listaPrincipal = listaPrincipal;
    }


    
}
