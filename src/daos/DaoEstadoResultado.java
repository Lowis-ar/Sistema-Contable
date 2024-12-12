/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelos.EstadoResultado;

/**
 *
 * @author guill
 */
public class DaoEstadoResultado {

    Conexion conexion;
    private Connection conection;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private EstadoResultado estado;

    public String select_ventas() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '5101'";
        return ventas(sql);
    }
    
    public String select_dev_ventas() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '4103'";
        return ventas(sql);
    } 
    
    public String select_reb_ventas() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '4104'";
        return ventas(sql);
    }

    public String select_inv_inicial() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '1106'";
        return ventas(sql);
    }
    
    public String select_compras() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '4101'";
        return ventas(sql);
    }
    
    public String select_Gastos_compras() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '4102'";
        return ventas(sql);
    }
    public String select_Dev_compras() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '5102'";
        return ventas(sql);
    }
    public String select_Des_compras() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '5103'";
        return ventas(sql);
    }
    
    public String otros_gatos() {
        String sql = "SELECT SUM(ld.monto) AS total_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '4304'";
        return ventas(sql);
    }
    
    
    
    public EstadoResultado select_ventas_totales() {
        String sql = "SELECT \n"
                + "    (ventas - devoluciones - rebajas) AS total_ventas\n"
                + "FROM (\n"
                + "    SELECT \n"
                + "        (SELECT COALESCE(SUM(monto), 0) \n"
                + "         FROM libro_diario \n"
                + "         JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "         JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor\n"
                + "         WHERE cuentas_mayor.cod_mayor = '5101') AS ventas,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) \n"
                + "         FROM libro_diario \n"
                + "         JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "         JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor\n"
                + "         WHERE cuentas_mayor.cod_mayor = '4103') AS devoluciones,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) \n"
                + "         FROM libro_diario \n"
                + "         JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "         JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor\n"
                + "         WHERE cuentas_mayor.cod_mayor = '4104') AS rebajas\n"
                + ") AS ventas_totales;";
        return ventas_Totales(sql);
    }

    public EstadoResultado select_costo_de_venta() {
        String sql = "SELECT \n"
                + "    (inventario_inicial - inventario_final + compras + gastos_compras - devoluciones - rebajas) AS costo_ventas\n"
                + "FROM (\n"
                + "    SELECT \n"
                + "        (SELECT COALESCE(SUM(monto), 0) FROM libro_diario  \n"
                + "        WHERE cod_subcuenta = '110601') AS inventario_inicial,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) FROM libro_diario\n"
                + "        WHERE cod_subcuenta = '110602') AS inventario_final,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) FROM libro_diario \n"
                + "        JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "        JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor \n"
                + "        WHERE cuentas_mayor.cod_mayor = '4101') AS compras,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) FROM libro_diario \n"
                + "        JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "        JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor \n"
                + "        WHERE cuentas_mayor.cod_mayor = '4102') AS gastos_compras,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) FROM libro_diario \n"
                + "        JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "        JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor\n"
                + "        WHERE cuentas_mayor.cod_mayor = '5102') AS devoluciones,\n"
                + "        (SELECT COALESCE(SUM(monto), 0) FROM libro_diario\n"
                + "        JOIN subcuentas ON subcuentas.cod_subcuenta = libro_diario.cod_subcuenta\n"
                + "        JOIN cuentas_mayor ON cuentas_mayor.cod_mayor = subcuentas.cod_mayor\n"
                + "        WHERE cuentas_mayor.cod_mayor = '5103') AS rebajas\n"
                + ") AS costo_venta;";
        return costo_Venta(sql);
    }

    public EstadoResultado select_gastos_admin() {
        String sql = "SELECT COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto WHEN ld.transaccion = 'Haber' THEN -ld.monto ELSE 0 END), 0) AS gastos_administrativos\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta \n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor \n"
                + "WHERE cm.cod_mayor = '4302'";
        return gastos_admin(sql);
    }

    public EstadoResultado select_gasto_venta() {
        String sql = "SELECT COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto WHEN ld.transaccion = 'Haber' THEN -ld.monto ELSE 0 END), 0) AS gastos_ventas\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta \n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor \n"
                + "WHERE cm.cod_mayor = '4301';";
        return gasto_ventas(sql);
    }

    public EstadoResultado select_ingresos_finan() {
        String sql = "SELECT COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto WHEN ld.transaccion = 'Haber' THEN -ld.monto ELSE 0 END), 0) AS ingresos_financieros\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta \n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor \n"
                + "WHERE cm.cod_mayor = '5201';";
        return ingresos_finan(sql);
    }

    public EstadoResultado select_gasto_finan() {
        String sql = "SELECT COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto WHEN ld.transaccion = 'Haber' THEN -ld.monto ELSE 0 END), 0) AS gastos_financieros\n"
                + "FROM libro_diario ld\n"
                + "JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta \n"
                + "JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor \n"
                + "WHERE cm.cod_mayor = '4303';";
        return gasto_finan(sql);
    }
    
    private EstadoResultado ventas_Totales(String sql) {
        EstadoResultado obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new EstadoResultado();
                obj.setVentas_Totales(rs.getString("total_ventas"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    private EstadoResultado costo_Venta(String sql) {
        EstadoResultado obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new EstadoResultado();
                obj.setCosto_Ventas(rs.getString("costo_ventas"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    private EstadoResultado gastos_admin(String sql) {
        EstadoResultado obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
            
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new EstadoResultado();
                obj.setGastos_Admin(rs.getString("gastos_administrativos"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    private EstadoResultado gasto_ventas(String sql) {
        EstadoResultado obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
           
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new EstadoResultado();
                obj.setGastos_Ventas(rs.getString("gastos_ventas"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    private EstadoResultado ingresos_finan(String sql) {
        EstadoResultado obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
           
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new EstadoResultado();
                obj.setIngresos_Finan(rs.getString("ingresos_financieros"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    private EstadoResultado gasto_finan(String sql) {
        EstadoResultado obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = new EstadoResultado();
                obj.setGastos_Finan(rs.getString("gastos_financieros"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

    private String ventas(String sql) {
        String obj = null;
        try {
            this.conexion = new Conexion();
            this.conection = conexion.getConexion();
            ps = conection.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                obj = (rs.getString("total_ventas"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conexion.cerrarConexiones();
        }
        return obj;
    }

}
