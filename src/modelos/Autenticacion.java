package modelos;

/**
 *
 * @author kevin
 */
public class Autenticacion {
     private int idAutenticacion;
    private String dui ;
    private String nombre ;
    private String usuario;
    private String clave;
    private String permiso;

    public Autenticacion() {
    }

    public Autenticacion(String dui, String nombre, String usuario, String clave, String permiso) {
        this.dui = dui;
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
        this.permiso = permiso;
    }

    public Autenticacion(int idAutenticacion, String dui, String nombre, String usuario, String clave, String permiso) {
        this.idAutenticacion = idAutenticacion;
        this.dui = dui;
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
        this.permiso = permiso;
    }

    public int getIdAutenticacion() {
        return idAutenticacion;
    }

    public void setIdAutenticacion(int idAutenticacion) {
        this.idAutenticacion = idAutenticacion;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }
}
