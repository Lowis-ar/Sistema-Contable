package daos;

import Utilidades.SubCuentas;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelos.BalanzaComprobacion;

public class BalanzaComprobacionDAO {

    private static final String QUERY_BALANZA
            = "SELECT \n"
            + "    cm.cod_mayor AS codigo_mayor,\n"
            + "    cm.nombre AS cuenta_mayor,\n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) AS debe,\n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) AS haber,\n"
            + "    CASE \n"
            + "        WHEN COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) -\n"
            + "             COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) > 0 \n"
            + "        THEN \n"
            + "             COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) -\n"
            + "             COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) \n"
            + "        ELSE 0 \n"
            + "    END AS saldodeudor,\n"
            + "    CASE \n"
            + "        WHEN COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) -\n"
            + "             COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) > 0 \n"
            + "        THEN \n"
            + "             COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) -\n"
            + "             COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) \n"
            + "        ELSE 0 \n"
            + "    END AS saldoacreedor\n"
            + "FROM \n"
            + "    cuentas_mayor cm\n"
            + "LEFT JOIN \n"
            + "    subcuentas s ON cm.cod_mayor = s.cod_mayor\n"
            + "LEFT JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "GROUP BY \n"
            + "    cm.cod_mayor, cm.nombre\n"
            + "HAVING \n"
            + "    saldodeudor > 0 OR saldoacreedor > 0\n"
            + "ORDER BY \n"
            + "    cm.cod_mayor;";

    private static final String QUERY_SUBCUENTAS = "SELECT \n"
            + "    s.cod_subcuenta AS codigo_subcuenta,\n"
            + "    s.nombre AS nombre_subcuenta,\n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) AS total_debe,\n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) AS total_haber\n"
            + "FROM \n"
            + "    subcuentas s\n"
            + "LEFT JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "GROUP BY \n"
            + "    s.cod_subcuenta, s.nombre\n"
            + "ORDER BY \n"
            + "    s.cod_subcuenta;";

    private static final String QUERY_SUBCUENTAS_REGISTROS = "SELECT \n"
            + "    s.cod_subcuenta AS codigo_subcuenta,\n"
            + "    s.nombre AS nombre_subcuenta,\n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) AS total_debe,\n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) AS total_haber\n"
            + "FROM \n"
            + "    subcuentas s\n"
            + "INNER JOIN \n"
            + "    libro_diario ld ON s.cod_subcuenta = ld.cod_subcuenta\n"
            + "GROUP BY \n"
            + "    s.cod_subcuenta, s.nombre\n"
            + "HAVING \n"
            + "    COALESCE(SUM(CASE WHEN ld.transaccion = 'DEBE' THEN ld.monto ELSE 0 END), 0) > 0 \n"
            + "    OR COALESCE(SUM(CASE WHEN ld.transaccion = 'HABER' THEN ld.monto ELSE 0 END), 0) > 0\n"
            + "ORDER BY \n"
            + "    s.cod_subcuenta;";

    public List<BalanzaComprobacion> obtenerBalanzaComprobacion() {
        List<BalanzaComprobacion> balanza = new ArrayList<>();
        Conexion conexionBD = new Conexion();

        try (Connection conexion = conexionBD.getConexion(); PreparedStatement stmt = conexion.prepareStatement(QUERY_BALANZA); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BalanzaComprobacion registro = new BalanzaComprobacion(
                        rs.getString("codigo_mayor"),
                        rs.getString("cuenta_mayor"),
                        rs.getDouble("debe"),
                        rs.getDouble("haber"),
                        rs.getDouble("saldodeudor"),
                        rs.getDouble("saldoacreedor"),
                        rs.getDouble("saldodeudor") - rs.getDouble("saldoacreedor")
                );
                balanza.add(registro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balanza;
    }

    public ArrayList<SubCuentas> obtenerSubCuentas() {

        ArrayList<SubCuentas> subcuentas = new ArrayList<>();
        Conexion conexionBD = new Conexion();

        try (Connection conexion = conexionBD.getConexion(); PreparedStatement stmt = conexion.prepareStatement(QUERY_SUBCUENTAS_REGISTROS); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SubCuentas cuentas = new SubCuentas(
                        rs.getString("codigo_subcuenta"),
                        rs.getString("nombre_subcuenta"),
                        rs.getFloat("total_debe"),
                        rs.getFloat("total_haber")
                );
                subcuentas.add(cuentas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subcuentas;

    }

}
