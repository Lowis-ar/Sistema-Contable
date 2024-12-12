/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import java.util.ArrayList;
import java.sql.SQLException;
import modelos.Cuentas_Mayor;
import modelos.SubCuentas;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

/**
 *
 * @author guill
 */
public class DaoSubCuenta {

    Conexion conexion;
    private ArrayList<Cuentas_Mayor> listaMayor;
    private ArrayList<SubCuentas> listaSub;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
    private SubCuentas subcuenta;

    public ArrayList<Cuentas_Mayor> select_nombres_mayor(String dato) {
        String sql = "SELECT c.nombre AS nombre FROM cuentas_mayor AS c INNER JOIN cuentas_principales As p ON p.cod_principal = c.cod_principal WHERE p.nombre LIKE ? ORDER BY c.cod_mayor;";
        return selectNombres(sql, dato);
    }

    public Cuentas_Mayor select_cod_mayor(String dato) {
        String sql = "SELECT cod_mayor FROM cuentas_mayor WHERE nombre LIKE ? LIMIT 1;";
        return selectmayor(sql, dato);
    }

    private static final String SELECT_SUB = "SELECT s.cod_subcuenta, s.nombre, c.nombre AS nombreMayor FROM subcuentas AS s INNER JOIN cuentas_mayor AS c ON c.cod_mayor = s.cod_mayor ORDER BY s.cod_subcuenta;";

    private static final String INSERTAR_SUB = "INSERT INTO subcuentas(cod_subcuenta, nombre, cod_mayor) VALUES (?,?,?)";

    private static final String ACTUALIZAR_SUB = "UPDATE subcuentas SET nombre =?, cod_mayor=? WHERE cod_subcuenta =?;";

    public DaoSubCuenta() {
        this.conexion = new Conexion();
    }

    public ArrayList<Cuentas_Mayor> selectNombres(String sql, String dato) {
        ArrayList<Cuentas_Mayor> lista = new ArrayList<>();

        this.conexion = new Conexion();
        this.conexion.getConexion();
        this.accesoDB = conexion.getConexion();
        try (PreparedStatement stmt = accesoDB.prepareStatement(sql)) {

            stmt.setString(1, "%" + dato + "%"); // Preparar el parámetro con LIKE

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cuentas_Mayor cuenta = new Cuentas_Mayor();
                    cuenta.setNombre_Mayor(rs.getString("nombre")); // Asignar el nombre desde el ResultSet
                    lista.add(cuenta);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    private Cuentas_Mayor selectmayor(String sql, String dato) {
        Cuentas_Mayor obj = null;
        try {
            this.conexion = new Conexion();
            this.conexion.getConexion();
            this.accesoDB = conexion.getConexion();
            ps = accesoDB.prepareStatement(sql);
            ps.setString(1, "%" + dato + "%");
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new Cuentas_Mayor();
                obj.setCod_Mayor(rs.getString("cod_mayor"));
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

    public ArrayList<SubCuentas> selectTodoSubCuenta() {

        this.listaSub = new ArrayList();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_SUB);
            this.rs = ps.executeQuery();

            SubCuentas obj = null;
            while (this.rs.next()) {
                obj = new SubCuentas();
                obj.setCod_subcuenta(rs.getString("cod_subcuenta"));
                obj.setNombre_sub(rs.getString("nombre"));
                Cuentas_Mayor m = new Cuentas_Mayor();
                m.setNombre_Mayor(rs.getString("nombreMayor"));
                obj.setCod_mayor(m);
                this.listaSub.add(obj);
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.listaSub;
    }
    
    public String insertSubCuenta(SubCuentas sub) {

        String resultado;
        int resultado_insertar;
        try {
            this.conexion = new Conexion();
            this.conexion.getConexion();
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERTAR_SUB);
            this.ps.setString(1, sub.getCod_subcuenta());
            this.ps.setString(2, sub.getNombre_sub());
            this.ps.setString(3, sub.getCod_mayor().getCod_Mayor());

            System.out.println("servicio_insertar" + sub.toString());
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
    
    public String updateSubCuenta(SubCuentas sub) {
        System.out.println(sub.getCod_subcuenta());
        String resultado;
        int res_actualizar;
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(ACTUALIZAR_SUB);

            this.ps.setString(1,  sub.getNombre_sub());
            this.ps.setString(2, sub.getCod_mayor().getCod_Mayor());
            this.ps.setString(3, sub.getCod_subcuenta());
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
    
    public boolean deleteSub(String codSub) {
        String sql = "DELETE FROM subcuentas WHERE cod_subcuenta = ?";
        try (Connection conn = this.conexion.getConexion(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codSub);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó al menos un registro
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Retorna false si ocurre algún error
        }
    }
}
