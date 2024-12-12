/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import java.util.ArrayList;
import modelos.Catalogo;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import modelos.Cuenta;
import modelos.Cuentas_Mayor;
import modelos.Cuentas_Principales;
import modelos.SubCuentas;

/**
 *
 * @author guill
 */
public class DaoCatalogo {

    Conexion conexion;
    private ArrayList<Catalogo> listaCatalogo;
    private ArrayList<Cuentas_Mayor> listaMayor;
    private ArrayList<Cuentas_Principales> listaPrincipal;
    private ArrayList<SubCuentas> listaSub;

    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    
    private Catalogo catalogo;

    private Cuentas_Principales cuentasprin;
    private SubCuentas subcuenta;


    private static final String SELECCIONAR_CATALOGO = "SELECT cod_catalogo, nombre FROM catalogo;";
    private static final String SELECT_CUENTA_PRINC = "SELECT cod_principal, nombre FROM cuentas_principales;";

    public Cuentas_Principales select_cod_principal(String dato) {
        String SELECT_ID_PRINC = "SELECT cod_principal FROM cuentas_principales WHERE nombre LIKE ? LIMIT 1;";
        return selectPrincipal(SELECT_ID_PRINC, dato);
    }

    private static final String SELECT_MAYOR = "SELECT c.cod_mayor, c.nombre, c.naturaleza, p.nombre AS cuenta_principal\n"
            + "FROM cuentas_mayor AS c\n"
            + "INNER JOIN cuentas_principales AS p ON p.cod_principal = c.cod_principal ORDER BY c.cod_mayor;";

    private static final String INSERTAR_MAYOR = "INSERT INTO cuentas_mayor(cod_mayor,"
            + " nombre, naturaleza, cod_principal) VALUES (?,?,?,?)";

    private static final String ACTUALIZAR_MAYOR = "UPDATE cuentas_mayor SET "
            + " nombre =?, naturaleza =?, cod_principal=? WHERE cod_mayor =?;";

    public DaoCatalogo() {
        this.conexion = new Conexion();
    }

    private Cuentas_Principales selectPrincipal(String sql, String dato) {
        Cuentas_Principales obj = null;
        try {
            Connection con = conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + dato + "%");
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new Cuentas_Principales();
                obj.setCod_Principal(rs.getString("cod_principal"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Agrega un log adecuado o manejo de errores
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Agrega un log adecuado o manejo de errores
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    public ArrayList<Cuentas_Principales> selectTodoPrincipales() {

        ArrayList<Cuentas_Principales> listaPrin = new ArrayList();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement("SELECT cod_principal, nombre FROM cuentas_principales;");
            this.rs = ps.executeQuery();

            Cuentas_Principales obj = null;
            while (this.rs.next()) {
                obj = new Cuentas_Principales();
                obj.setCod_Principal(rs.getString("cod_principal"));
                obj.setNombre_Principal(rs.getString("nombre"));
                listaPrin.add(obj);
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPrin;
    }

    public ArrayList<Cuentas_Mayor> selectTodoMayor() {

        this.listaMayor = new ArrayList();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_MAYOR);
            this.rs = ps.executeQuery();

            Cuentas_Mayor obj = null;
            while (this.rs.next()) {
                obj = new Cuentas_Mayor();
                obj.setCod_Mayor(rs.getString("cod_mayor"));
                obj.setNombre_Mayor(rs.getString("nombre"));
                obj.setNaturaleza(rs.getString("naturaleza"));
                Cuentas_Principales cuentasprinc = new Cuentas_Principales();
                cuentasprinc.setNombre_Principal(rs.getString("cuenta_principal"));
                obj.setCod_principal((cuentasprinc));
                this.listaMayor.add(obj);
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.listaMayor;
    }

    public String insertMayor(Cuentas_Mayor mayor) {

        String resultado;
        int resultado_insertar;
        try {
            this.conexion = new Conexion();
            this.conexion.getConexion();
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERTAR_MAYOR);
            this.ps.setString(1, mayor.getCod_Mayor());
            this.ps.setString(2, mayor.getNombre_Mayor());
            this.ps.setString(3, mayor.getNaturaleza());
            this.ps.setString(4, mayor.getCod_principal().getCod_Principal());

            System.out.println("servicio_insertar" + mayor);
            resultado_insertar = this.ps.executeUpdate();
            this.conexion.cerrarConexiones();
            if (resultado_insertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_servicio";
            }
        } catch (SQLException e) {
            resultado = "error_excepcion";
            System.out.println("fallo insertar" + e.getErrorCode());
            e.printStackTrace();
        }
        return resultado;
    }

    public String updateMayor(Cuentas_Mayor mayor) {
        System.out.println(mayor.getCod_Mayor());
        String resultado;
        int res_actualizar;
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(ACTUALIZAR_MAYOR);

            this.ps.setString(1, mayor.getNombre_Mayor());
            this.ps.setString(2, mayor.getNaturaleza());
            this.ps.setString(3, mayor.getCod_principal().getCod_Principal());
            this.ps.setString(4, mayor.getCod_Mayor());
            res_actualizar = this.ps.executeUpdate();
            System.out.println(res_actualizar);

            if (res_actualizar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_actualizar";
            }
        } catch (SQLException e) {
            resultado = "error_exception";
            e.printStackTrace();
        }
        return resultado;
    }

    public ArrayList<Cuenta> selectCuentasJerarquicas() {
        ArrayList<Cuenta> listaCuentas = new ArrayList<>();

        try {
            this.conexion = new Conexion();
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(
                    "SELECT DISTINCT c.cod_catalogo AS Codigo, c.nombre AS Nombre FROM catalogo c\n"
                    + "UNION ALL SELECT DISTINCT CONCAT(cp.cod_principal) AS Codigo, cp.nombre AS Nombre\n"
                    + "FROM cuentas_principales cp JOIN catalogo c ON cp.cod_catalogo = c.cod_catalogo\n"
                    + "UNION ALL SELECT DISTINCT CONCAT(cm.cod_mayor) AS Codigo, cm.nombre AS Nombre\n"
                    + "FROM cuentas_mayor cm JOIN cuentas_principales cp ON cm.cod_principal = cp.cod_principal\n"
                    + "JOIN catalogo c ON cp.cod_catalogo = c.cod_catalogo\n"
                    + "UNION ALL SELECT DISTINCT CONCAT(sc.cod_subcuenta) AS Codigo, sc.nombre AS Nombre\n"
                    + "FROM subcuentas sc JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                    + "JOIN cuentas_principales cp ON cm.cod_principal = cp.cod_principal\n"
                    + "JOIN catalogo c ON cp.cod_catalogo = c.cod_catalogo\n"
                    + "ORDER BY Codigo;"
            );

            this.rs = ps.executeQuery();

            Cuenta cuenta = null;
            while (this.rs.next()) {
                cuenta = new Cuenta();
                cuenta.setCodigo(rs.getString("Codigo"));
                cuenta.setNombre(rs.getString("Nombre"));
                listaCuentas.add(cuenta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.ps != null) {
                    this.ps.close();
                }
                if (this.rs != null) {
                    this.rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.conexion.cerrarConexiones();
        }

        return listaCuentas;
    }

    public ArrayList<Cuenta> buscarCuentasJerarquicas(String busqueda) {
        ArrayList<Cuenta> listaCuentas = new ArrayList<>();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(
                    "SELECT DISTINCT c.cod_catalogo AS Codigo, c.nombre AS Nombre "
                    + "FROM catalogo c "
                    + "WHERE c.cod_catalogo LIKE ? OR c.nombre LIKE ? "
                    + "UNION ALL "
                    + "SELECT DISTINCT CONCAT(cp.cod_principal) AS Codigo, cp.nombre AS Nombre "
                    + "FROM cuentas_principales cp "
                    + "JOIN catalogo c ON cp.cod_catalogo = c.cod_catalogo "
                    + "WHERE cp.cod_principal LIKE ? OR cp.nombre LIKE ? "
                    + "UNION ALL "
                    + "SELECT DISTINCT CONCAT(cm.cod_mayor) AS Codigo, cm.nombre AS Nombre "
                    + "FROM cuentas_mayor cm "
                    + "JOIN cuentas_principales cp ON cm.cod_principal = cp.cod_principal "
                    + "JOIN catalogo c ON cp.cod_catalogo = c.cod_catalogo "
                    + "WHERE cm.cod_mayor LIKE ? OR cm.nombre LIKE ? "
                    + "UNION ALL "
                    + "SELECT DISTINCT CONCAT(sc.cod_subcuenta) AS Codigo, sc.nombre AS Nombre "
                    + "FROM subcuentas sc "
                    + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor "
                    + "JOIN cuentas_principales cp ON cm.cod_principal = cp.cod_principal "
                    + "JOIN catalogo c ON cp.cod_catalogo = c.cod_catalogo "
                    + "WHERE sc.cod_subcuenta LIKE ? OR sc.nombre LIKE ? "
                    + "ORDER BY Codigo;"
            );

            String queryBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 8; i++) {
                this.ps.setString(i, queryBusqueda);
            }

            this.rs = ps.executeQuery();

            Cuenta cuenta;
            while (this.rs.next()) {
                cuenta = new Cuenta();
                cuenta.setCodigo(rs.getString("Codigo"));
                cuenta.setNombre(rs.getString("Nombre"));
                listaCuentas.add(cuenta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.ps != null) {
                    this.ps.close();
                }
                if (this.rs != null) {
                    this.rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.conexion.cerrarConexiones();
        }

        return listaCuentas;
    }

    public boolean deleteMayor(String codMayor) {
        String sql = "DELETE FROM cuentas_mayor WHERE cod_mayor = ?";
        try (Connection conn = this.conexion.getConexion(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codMayor);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó al menos un registro
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }

}
