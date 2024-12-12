package daos;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import modelos.BalanceGeneral;
import java.sql.SQLException;

/**
 *
 * @author Luis
 */
public class DaoBalanceGeneral {

    Conexion conexion = new Conexion();
    private ArrayList<BalanceGeneral> listaBalanceGeneral;
    private ResultSet rs = null;
    private Statement st = null;
    private PreparedStatement ps;
    private Connection accesoDB;

    private static final String CONSULTAR_BALANZA_ACTIVOS = "SELECT cm.cod_mayor AS codigo, \n"
            + "       cm.nombre AS nombre_subcuenta, \n"
            + "       COALESCE(SUM(CASE \n"
            + "                    WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Debe' THEN (ld.monto)\n"
            + "                    WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Haber' THEN -(ld.monto)\n"
            + "                    WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Debe' THEN -(ld.monto)\n"
            + "                    WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Haber' THEN (ld.monto)\n"
            + "                    ELSE 0 END), 0) AS saldo\n"
            + "FROM cuentas_mayor cm\n"
            + "JOIN subcuentas s ON cm.cod_mayor = s.cod_mayor\n"
            + "LEFT JOIN libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "WHERE cm.cod_principal = (SELECT cp.cod_principal \n"
            + "                           FROM cuentas_principales cp \n"
            + "                           WHERE cp.nombre = ?)\n"
            + "  AND YEAR(ld.fecha) = ?\n"
            + "GROUP BY cm.cod_mayor, cm.nombre\n"
            + "ORDER BY cm.cod_mayor;";

    private static final String CONSULTAR_BALANZA_PASIVOS = "SELECT cm.cod_mayor AS codigo, \n"
            + "       cm.nombre AS nombre_subcuenta, \n"
            + "       COALESCE(SUM(CASE \n"
            + "                    WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Debe' THEN (ld.monto)\n"
            + "                    WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Haber' THEN -(ld.monto)\n"
            + "                    WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Debe' THEN -(ld.monto)\n"
            + "                    WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Haber' THEN (ld.monto)\n"
            + "                    ELSE 0 END), 0) AS saldo\n"
            + "FROM cuentas_mayor cm\n"
            + "JOIN subcuentas s ON cm.cod_mayor = s.cod_mayor\n"
            + "LEFT JOIN libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "WHERE cm.cod_principal = (SELECT cp.cod_principal \n"
            + "                           FROM cuentas_principales cp \n"
            + "                           WHERE cp.nombre = ?)\n"
            + "  AND YEAR(ld.fecha) = ?\n"
            + "GROUP BY cm.cod_mayor, cm.nombre\n"
            + "ORDER BY cm.cod_mayor;";

    private static final String CONSULTAR_TOTAL_CUENTAS_ACTIVO = "SELECT \n"
            + "    COALESCE(SUM(CASE \n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Haber' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Debe' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        ELSE 0 \n"
            + "    END), 0) AS total\n"
            + "FROM \n"
            + "    subcuentas s\n"
            + "JOIN \n"
            + "    cuentas_mayor cm ON s.cod_mayor = cm.cod_mayor\n"
            + "JOIN \n"
            + "    cuentas_principales cp ON cm.cod_principal = cp.cod_principal\n"
            + "LEFT JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "WHERE \n"
            + "    cp.nombre = ? AND EXTRACT(YEAR FROM ld.fecha) = ?;";

    private static final String CONSULTAR_TOTAL_CUENTAS_PASIVO = "SELECT \n"
            + "    COALESCE(SUM(CASE \n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Haber' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Debe' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        ELSE 0 \n"
            + "    END), 0) AS total\n"
            + "FROM \n"
            + "    subcuentas s\n"
            + "JOIN \n"
            + "    cuentas_mayor cm ON s.cod_mayor = cm.cod_mayor\n"
            + "JOIN \n"
            + "    cuentas_principales cp ON cm.cod_principal = cp.cod_principal\n"
            + "LEFT JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "WHERE \n"
            + "    cp.nombre = ? AND EXTRACT(YEAR FROM ld.fecha) = ?;";

    private static final String CONSULTAR_TOTAL_ACTIVOS = "SELECT \n"
            + "    COALESCE(SUM(CASE \n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Haber' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Debe' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        ELSE 0 \n"
            + "    END), 0) AS total\n"
            + "FROM \n"
            + "    subcuentas s\n"
            + "JOIN \n"
            + "    cuentas_mayor cm ON s.cod_mayor = cm.cod_mayor\n"
            + "JOIN \n"
            + "    cuentas_principales cp ON cm.cod_principal = cp.cod_principal\n"
            + "LEFT JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "WHERE \n"
            + "    cp.nombre IN ('Activo Corriente', 'Activo No Corriente') AND EXTRACT(YEAR FROM ld.fecha) = ?;";

    private static final String CONSULTAR_TOTAL_PASIVOS = "SELECT \n"
            + "    COALESCE(SUM(CASE \n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'DEUDOR' AND ld.transaccion = 'Haber' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Debe' THEN -CAST(ld.monto AS DECIMAL)\n"
            + "        WHEN cm.naturaleza = 'ACREEDOR' AND ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL)\n"
            + "        ELSE 0 \n"
            + "    END), 0) AS total\n"
            + "FROM \n"
            + "    subcuentas s\n"
            + "JOIN \n"
            + "    cuentas_mayor cm ON s.cod_mayor = cm.cod_mayor\n"
            + "JOIN \n"
            + "    cuentas_principales cp ON cm.cod_principal = cp.cod_principal\n"
            + "LEFT JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "WHERE \n"
            + "    cp.nombre IN ('Pasivo Corriente', 'Pasivo No Corriente', 'Capital Contable') AND EXTRACT(YEAR FROM ld.fecha) = ?;";

    private static final String INGRESAR_DETALLES_BALANCE_GENERAL = "INSERT INTO balance_general_detalles(cuenta, monto, n_balance, tipo_cuenta) VALUES (?, ?, ?, ?)";

    private static final String INGRESAR_TOTALES_BALANCE_GENERAL = "INSERT INTO balance_general_totales(\n"
            + "	total_activo_corriente, total_activo_nocorriente, total_pasivo_corriente, total_pasivo_nocorriente, total_capital, total_activo, total_pasivo, n_balance)\n"
            + "	VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INICIAR_BALANCE_GENERAL = "INSERT INTO balance_general(\n"
            + "	n_balance, fecha)\n"
            + "	VALUES (?, ?);";

    private static final String OBTENER_N_BALANCE = "SELECT n_balance from balance_general";

    public ArrayList<BalanceGeneral> CargarBalanceGeneralActivos(String tipo, int anio) {

        this.listaBalanceGeneral = new ArrayList();

        try {

            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CONSULTAR_BALANZA_ACTIVOS);
            this.ps.setString(1, tipo);
            this.ps.setInt(2, anio);
            this.rs = ps.executeQuery();
            String comparar = "1106";
            BalanceGeneral obj = null;
            while (this.rs.next()) {
                obj = new BalanceGeneral();
                if (rs.getString("codigo").compareTo(comparar) == 0 ) {
                    obj.setCodigo(rs.getString("codigo"));
                    obj.setCuenta("INVENTARIO FINAL");
                    obj.setMonto(Float.toString((float) 200000.00));
                    this.listaBalanceGeneral.add(obj);
                } else {
                    obj.setCodigo(rs.getString("codigo"));
                    obj.setCuenta(rs.getString("nombre_subcuenta"));
                    obj.setMonto(Float.toString(rs.getFloat("saldo")));
                    this.listaBalanceGeneral.add(obj);
                }
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.listaBalanceGeneral;
    }

    public ArrayList<BalanceGeneral> CargarBalanceGeneralPasivos(String tipo, int anio) {

        this.listaBalanceGeneral = new ArrayList();

        try {

            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CONSULTAR_BALANZA_PASIVOS);
            this.ps.setString(1, tipo);
            this.ps.setInt(2, anio);
            this.rs = ps.executeQuery();

            BalanceGeneral obj = null;
            while (this.rs.next()) {
                obj = new BalanceGeneral();
                obj.setCodigo(rs.getString("codigo"));
                obj.setCuenta(rs.getString("nombre_subcuenta"));
                obj.setMonto(Float.toString(rs.getFloat("saldo")));
                this.listaBalanceGeneral.add(obj);
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.listaBalanceGeneral;
    }

    public float GetTotalCuentasActivo(String tipo, int anio) {
        float total = 0;

        try {

            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CONSULTAR_TOTAL_CUENTAS_ACTIVO);
            this.ps.setString(1, tipo);
            this.ps.setInt(2, anio);
            this.rs = ps.executeQuery();

            while (this.rs.next()) {
                total = rs.getFloat("total");
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }

    public float GetTotalCuentasPasivo(String tipo, int anio) {
        float total = 0;

        try {

            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CONSULTAR_TOTAL_CUENTAS_PASIVO);
            this.ps.setString(1, tipo);
            this.ps.setInt(2, anio);
            this.rs = ps.executeQuery();

            while (this.rs.next()) {
                total = rs.getFloat("total");
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }

    public float GetTotalActivos(int anio) {
        float total = 0;

        try {

            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CONSULTAR_TOTAL_ACTIVOS);
            this.ps.setInt(1, anio);
            this.rs = ps.executeQuery();

            while (this.rs.next()) {
                total = rs.getFloat("total");
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }

    public float GetTotalPasivos(int anio) {
        float total = 0;

        try {

            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(CONSULTAR_TOTAL_PASIVOS);
            this.ps.setInt(1, anio);
            this.rs = ps.executeQuery();

            while (this.rs.next()) {
                total = rs.getFloat("total");
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }

    public int GetNBalance() {
        int n = 0;

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(OBTENER_N_BALANCE);
            this.rs = ps.executeQuery();

            while (this.rs.next()) {
                n = rs.getInt("n_balance");
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        n = n + 1;

        return n;

    }

    public String IngresardetallesBalanceGeneral(String cuenta, double monto, int n_balance, int tipo_cuenta) {
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INGRESAR_DETALLES_BALANCE_GENERAL);
            this.ps.setString(1, cuenta);
            this.ps.setDouble(2, monto);
            this.ps.setInt(3, n_balance);
            this.ps.setInt(4, tipo_cuenta);
            ps.execute();

            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
            return "fracaso";
        }
        return "exito";
    }

    public String IngresarTotalesBalanceGeneral(double total_activo_corriente, double total_activo_nocorriente, double total_pasivo_corriente, double total_pasivo_nocorriente, double total_capital, double total_activo, double total_pasivo, int n_balance) {
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INGRESAR_TOTALES_BALANCE_GENERAL);
            this.ps.setDouble(1, total_activo_corriente);
            this.ps.setDouble(2, total_activo_nocorriente);
            this.ps.setDouble(3, total_pasivo_corriente);
            this.ps.setDouble(4, total_pasivo_nocorriente);
            this.ps.setDouble(5, total_capital);
            this.ps.setDouble(6, total_activo);
            this.ps.setDouble(7, total_pasivo);
            this.ps.setInt(8, n_balance);

            ps.execute();

            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
            return "fracaso";
        }
        return "exito";
    }

    public String IniciarBalanceGeneral(int i, int anio) {
        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INICIAR_BALANCE_GENERAL);
            this.ps.setInt(1, i);
            this.ps.setInt(2, anio);

            ps.execute();

            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
            return "fracaso";
        }
        return "exito";
    }

}
