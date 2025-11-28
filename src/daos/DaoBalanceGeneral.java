package daos;

import modelos.BalanceGeneral;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO completamente corregido para Balance General
 * Calcula correctamente Activos, Pasivos y Capital según la naturaleza contable
 * De acuerdo a base14partidas.sql
 */
public class DaoBalanceGeneral {

    private final Conexion conexion = new Conexion();

    private static final String SQL_SALDO_CUENTAS =
        "SELECT " +
        "  cp.cod_catalogo, " +
        "  cp.cod_principal, " +
        "  cp.nombre AS tipo_principal, " +
        "  s.nombre AS cuenta, " +
        "  COALESCE(SUM(CASE WHEN ld.transaccion='debe' THEN ld.monto ELSE 0 END),0) AS total_debe, " +
        "  COALESCE(SUM(CASE WHEN ld.transaccion='haber' THEN ld.monto ELSE 0 END),0) AS total_haber " +
        "FROM subcuentas s " +
        "JOIN cuentas_mayor cm ON s.cod_mayor = cm.cod_mayor " +
        "JOIN cuentas_principales cp ON cp.cod_principal = cm.cod_principal " +
        "LEFT JOIN libro_diario ld ON ld.cod_subcuenta = s.cod_subcuenta AND YEAR(ld.fecha) = ? " +
        "WHERE cp.cod_catalogo IN ('1','2','3') " +
        "GROUP BY cp.cod_catalogo, cp.cod_principal, cp.nombre, s.nombre " +
        "ORDER BY cp.cod_catalogo, cp.cod_principal;";

    private static final String SQL_INSERT_INICIO =
        "INSERT INTO balance_general(n_balance, fecha) VALUES (?, ?)";

    private static final String SQL_INSERT_DETALLE =
        "INSERT INTO balance_general_detalles(cuenta, monto, n_balance, tipo_cuenta) " +
        "VALUES (?, ?, ?, ?)";

    private static final String SQL_INSERT_TOTALES =
        "INSERT INTO balance_general_totales(" +
        " total_activo_corriente, total_activo_nocorriente, total_pasivo_corriente, " +
        " total_pasivo_nocorriente, total_capital, total_activo, total_pasivo, n_balance) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_GET_N =
        "SELECT COALESCE(MAX(n_balance),0) AS n FROM balance_general";


    // -------------------------------------------------------------
    // MÉTODO PRINCIPAL: OBTENER TODO EL BALANCE DE UN AÑO
    // -------------------------------------------------------------
    public BalanceData cargarBalance(int anio) {

        ArrayList<BalanceGeneral> activosCorriente = new ArrayList<>();
        ArrayList<BalanceGeneral> activosNoCorriente = new ArrayList<>();
        ArrayList<BalanceGeneral> pasivosCorriente = new ArrayList<>();
        ArrayList<BalanceGeneral> pasivosNoCorriente = new ArrayList<>();
        ArrayList<BalanceGeneral> capital = new ArrayList<>();

        double totalAC = 0, totalANC = 0, totalPC = 0, totalPNC = 0, totalCAP = 0;

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL_SALDO_CUENTAS)) {

            ps.setInt(1, anio);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String catalogo = rs.getString("cod_catalogo"); // 1 = Activo, 2 = Pasivo, 3 = Capital
                String principal = rs.getString("cod_principal");
                String cuenta = rs.getString("cuenta");
                double debe = rs.getDouble("total_debe");
                double haber = rs.getDouble("total_haber");
                double saldo;

                // Naturaleza contable
                if (catalogo.equals("1")) { // ACTIVO = deudor
                    saldo = debe - haber;
                } else { // PASIVO / CAPITAL = acreedor
                    saldo = haber - debe;
                }

                if (saldo == 0) continue;

                BalanceGeneral bg = new BalanceGeneral();
                bg.setCuenta(cuenta);
                bg.setMonto(String.format("%.2f", saldo));

                // Clasificación por catálogo/principal
                switch (catalogo) {

                    case "1": // ACTIVO
                        if (principal.equals("11")) {
                            activosCorriente.add(bg);
                            totalAC += saldo;
                        } else if (principal.equals("12")) {
                            activosNoCorriente.add(bg);
                            totalANC += saldo;
                        }
                        break;

                    case "2": // PASIVO
                        if (principal.equals("21")) {
                            pasivosCorriente.add(bg);
                            totalPC += saldo;
                        } else if (principal.equals("22")) {
                            pasivosNoCorriente.add(bg);
                            totalPNC += saldo;
                        }
                        break;

                    case "3": // CAPITAL
                        capital.add(bg);
                        totalCAP += saldo;
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new BalanceData(
            activosCorriente, activosNoCorriente,
            pasivosCorriente, pasivosNoCorriente,
            capital,
            totalAC, totalANC, totalPC, totalPNC, totalCAP
        );
    }

    // -------------------------------------------------------------
    // INSERTAR BALANCE
    // -------------------------------------------------------------
    public int generarNumeroBalance() {
        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL_GET_N)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("n") + 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public boolean iniciarBalance(int n, int anio) {
        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT_INICIO)) {

            ps.setInt(1, n);
            ps.setInt(2, anio);
            ps.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertarDetalle(String cuenta, double monto, int n, int tipoCuenta) {
        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT_DETALLE)) {

            ps.setString(1, cuenta);
            ps.setDouble(2, monto);
            ps.setInt(3, n);
            ps.setInt(4, tipoCuenta);
            ps.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertarTotales(double AC, double ANC, double PC, double PNC,
                                   double CAP, double totalA, double totalP, int n) {

        try (Connection cn = conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT_TOTALES)) {

            ps.setDouble(1, AC);
            ps.setDouble(2, ANC);
            ps.setDouble(3, PC);
            ps.setDouble(4, PNC);
            ps.setDouble(5, CAP);
            ps.setDouble(6, totalA);
            ps.setDouble(7, totalP);
            ps.setInt(8, n);
            ps.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // -------------------------------------------------------------
    // CLASE INTERNA PARA EMPAQUETAR DATOS DEL BALANCE
    // -------------------------------------------------------------
    public static class BalanceData {
        public ArrayList<BalanceGeneral> AC, ANC, PC, PNC, CAP;
        public double totalAC, totalANC, totalPC, totalPNC, totalCAP;

        public BalanceData(ArrayList<BalanceGeneral> AC, ArrayList<BalanceGeneral> ANC,
                           ArrayList<BalanceGeneral> PC, ArrayList<BalanceGeneral> PNC,
                           ArrayList<BalanceGeneral> CAP,
                           double totalAC, double totalANC, double totalPC, double totalPNC, double totalCAP) {

            this.AC = AC;  this.ANC = ANC;
            this.PC = PC;  this.PNC = PNC;
            this.CAP = CAP;

            this.totalAC = totalAC;
            this.totalANC = totalANC;
            this.totalPC = totalPC;
            this.totalPNC = totalPNC;
            this.totalCAP = totalCAP;
        }
    }
}
