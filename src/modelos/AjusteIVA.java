package modelos;

import daos.DaoLibroDiario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AjusteIVA {
    private Connection connection;

    // Constructor para inicializar la conexión a la base de datos
    public AjusteIVA() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Conectar a la base de datos llamada 'sic'
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sic", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. Obtener IVA Crédito Fiscal
    public double obtenerIvaCredito() throws SQLException {
        String query = "SELECT \n"
                + "    COALESCE(SUM(CASE \n"
                + "                    WHEN ld.transaccion = 'Debe' THEN ld.monto \n"
                + "                    ELSE -ld.monto \n"
                + "                 END), 0) AS saldo_iva_credito\n"
                + "FROM libro_diario ld\n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "LEFT JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '1108';";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo_iva_credito");
            }
        }
        return 0.0;
    }

    // 2. Obtener IVA Débito Fiscal
    public double obtenerIvaDebito() throws SQLException {
        String query = "SELECT \n"
                + "    COALESCE(SUM(CASE \n"
                + "                    WHEN ld.transaccion = 'Haber' THEN ld.monto \n"
                + "                    ELSE -ld.monto \n"
                + "                 END), 0) AS saldo_iva_debito\n"
                + "FROM libro_diario ld\n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "LEFT JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor\n"
                + "WHERE cm.cod_mayor = '2107';";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo_iva_debito");
            }
        }
        return 0.0;
    }

    // 3. Ajustar IVA
    public void ajustarIva() throws SQLException {
        double debito = obtenerIvaDebito();
        double credito = obtenerIvaCredito();
        DaoLibroDiario s = new DaoLibroDiario();
        int i = s.obtenerUltimaPartida() + 1;
        if (debito > credito) {
            double pagar = debito - credito;

            // Insertar IVA Débito Fiscal (Haber)
            insertarTransaccion(i, "210701", debito, "Liquidación de IVA - Carga de IVA Débito Fiscal", "Debe");

            // Insertar IVA Crédito Fiscal (Debe)
            insertarTransaccion(i, "110801", credito, "Liquidación de IVA - Abono de IVA Crédito Fiscal", "Haber");

            // Insertar la diferencia en Cuentas por Pagar (Haber)
            insertarTransaccion(i, "210401", pagar, "Liquidación de IVA - Diferencia abonada a Cuentas y Documentos por Pagar", "Haber");

        } else {
            double remanente = credito - debito;

            // Insertar IVA Débito Fiscal (Haber)
            insertarTransaccion(i, "210701", debito, "Liquidación de IVA - Carga de IVA Débito Fiscal", "Debe");

            // Insertar IVA Crédito Fiscal (Debe)
            insertarTransaccion(i, "110801", credito, "Liquidación de IVA - Abono de IVA Crédito Fiscal", "Haber");

            // Insertar la diferencia en Remanente de IVA (Debe)
            insertarTransaccion(i, "130101", remanente, "Liquidación de IVA - Diferencia cargada a Remanente de IVA", "Debe");
        }
    }

    // Método para insertar una transacción en la tabla libro_diario
    private void insertarTransaccion(int partida, String codSubcuenta, double monto, String concepto, String transaccion) throws SQLException {
        String insertQuery = "INSERT INTO libro_diario (numero_partida, cod_subcuenta, fecha, monto, concepto, transaccion) "
                + "VALUES (?, ?, CURRENT_DATE, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, partida); // Número de partida
            stmt.setString(2, codSubcuenta); // Código de la subcuenta
            stmt.setDouble(3, monto); // Monto
            stmt.setString(4, concepto); // Concepto
            stmt.setString(5, transaccion); // Tipo de transacción (Debe/Haber)
            stmt.executeUpdate();
        }
    }
}
