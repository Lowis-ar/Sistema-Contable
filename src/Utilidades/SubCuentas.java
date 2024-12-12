package Utilidades;


public class SubCuentas {
    private String codigo;
    private String nombre;
    private float debe;
    private float haber;

    public SubCuentas() {
    }

    public SubCuentas(String codigo, String nombre, float debe, float haber) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.debe = debe;
        this.haber = haber;
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

    public float getDebe() {
        return debe;
    }

    public void setDebe(float debe) {
        this.debe = debe;
    }

    public float getHaber() {
        return haber;
    }

    public void setHaber(float haber) {
        this.haber = haber;
    }
    
    
    
}
