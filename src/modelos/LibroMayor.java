/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;


public class LibroMayor {
    
    private String codigo;
    private String nombre;
    
    
    private Double debe;
    private Double haber;
    private Double saldo;

    public LibroMayor() {
    }

    public LibroMayor(String codigo) {
        this.codigo = codigo;
    }

    public LibroMayor(String codigo, String nombre, Double debe, Double haber) {
        this.codigo = codigo;
        this.nombre = nombre;
        
        this.debe = debe;
        this.haber = haber;
    }

    
    
    public LibroMayor(String codigo, String nombre,  Double debe, Double haber, Double saldo) {
        this.codigo = codigo;
        this.nombre = nombre;
        
        this.debe = debe;
        this.haber = haber;
        this.saldo = saldo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getDebe() {
        return debe;
    }

    public void setDebe(Double debe) {
        this.debe = debe;
    }

    public Double getHaber() {
        return haber;
    }

    public void setHaber(Double haber) {
        this.haber = haber;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

   
    
}
