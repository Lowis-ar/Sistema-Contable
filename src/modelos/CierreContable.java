/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CierreContable {

    private Connection connection;
    ArrayList<PartidaCierre> listaCierre;
    ArrayList<LibroDiario> listaUltima;

    // Constructor que inicializa automáticamente la conexión
    public CierreContable() {
        // Configuración predeterminada para la base de datos
        String url = "jdbc:mysql://localhost:3306/sic"; // Cambia el nombre de la base de datos si es necesario
        String user = "root"; // Cambia el usuario si es diferente
        String password = ""; // Cambia la contraseña

        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Obtener el saldo de una cuenta específica
    private double obtenerSaldo(String codCuenta) throws SQLException {
        String query = "SELECT COALESCE(SUM(CASE WHEN transaccion = 'Debe' THEN monto ELSE -monto END), 0) AS saldo "
                + "FROM libro_diario ld "
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta "
                + "LEFT JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor "
                + "WHERE cm.cod_mayor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codCuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        }
        return 0.0;
    }

    private double obtenerSaldoHaber(String codCuenta) throws SQLException {
        String query = "SELECT COALESCE(SUM(CASE WHEN transaccion = 'Haber' THEN monto ELSE -monto END), 0) AS saldo "
                + "FROM libro_diario ld "
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta "
                + "LEFT JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor "
                + "WHERE cm.cod_mayor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codCuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        }
        return 0.0;
    }

    public ArrayList<PartidaCierre> obtenerSaldoActivoCierre(int partida) throws SQLException {
        listaCierre = new ArrayList<>();
        PartidaCierre c = null;
        String query = "SELECT sc.cod_subcuenta, COALESCE(SUM(CASE WHEN ld.transaccion = 'Haber' THEN ld.monto ELSE -ld.monto END), 0) AS saldo\n"
                + "FROM libro_diario ld \n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "WHERE sc.cod_subcuenta LIKE '1%' AND ld.numero_partida = ?\n"
                + "GROUP BY sc.cod_subcuenta\n"
                + "HAVING COALESCE(SUM(CASE WHEN ld.transaccion = 'Haber' THEN ld.monto ELSE -ld.monto END), 0) != 0.0;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, partida);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c = new PartidaCierre();
                c.setCodigo(rs.getInt("cod_subcuenta"));
                c.setMonto(rs.getDouble("saldo"));
                listaCierre.add(c);
            }
        }
        return listaCierre;
    }
    public ArrayList<PartidaCierre> obtenerSaldoPasivoCierre(int partida) throws SQLException {
        listaCierre = new ArrayList<>();
        PartidaCierre c = null;
        String query = "SELECT sc.cod_subcuenta, COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto ELSE -ld.monto END), 0) AS saldo\n"
                + "FROM libro_diario ld \n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "WHERE sc.cod_subcuenta LIKE '2%' AND ld.numero_partida = ?\n"
                + "GROUP BY sc.cod_subcuenta\n"
                + "HAVING COALESCE(SUM(CASE WHEN ld.transaccion = 'Haber' THEN ld.monto ELSE -ld.monto END), 0) != 0.0;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, partida);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c = new PartidaCierre();
                c.setCodigo(rs.getInt("cod_subcuenta"));
                c.setMonto(rs.getDouble("saldo"));
                listaCierre.add(c);
            }
        }
        return listaCierre;
    }
    
    
    public ArrayList<PartidaCierre> obtenerSaldoPatrimonioCierre(int partida) throws SQLException {
        listaCierre = new ArrayList<>();
        PartidaCierre c = null;
        String query = "SELECT sc.cod_subcuenta, COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto ELSE -ld.monto END), 0) AS saldo\n"
                + "FROM libro_diario ld \n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "WHERE sc.cod_subcuenta LIKE '3%' AND ld.numero_partida = ?\n"
                + "GROUP BY sc.cod_subcuenta\n"
                + "HAVING COALESCE(SUM(CASE WHEN ld.transaccion = 'Haber' THEN ld.monto ELSE -ld.monto END), 0) != 0.0;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, partida);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c = new PartidaCierre();
                c.setCodigo(rs.getInt("cod_subcuenta"));
                c.setMonto(rs.getDouble("saldo"));
                listaCierre.add(c);
            }
        }
        return listaCierre;
    }

    private ArrayList<PartidaCierre> obtenerSaldoActivo() throws SQLException {
        listaCierre = new ArrayList<>();
        PartidaCierre c = null;
        String query = "SELECT sc.cod_subcuenta, COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto ELSE -ld.monto END), 0) AS saldo\n"
                + "FROM libro_diario ld \n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "WHERE sc.cod_subcuenta LIKE '1%'\n"
                + "GROUP BY sc.cod_subcuenta\n"
                + "HAVING \n"
                + "COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto ELSE -ld.monto END), 0) != 0.0;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c = new PartidaCierre();
                c.setCodigo(rs.getInt("cod_subcuenta"));
                c.setMonto(rs.getDouble("saldo"));
                listaCierre.add(c);
            }
        }
        return listaCierre;
    }

    private ArrayList<PartidaCierre> obtenerSaldoPasivo() throws SQLException {
        listaCierre = new ArrayList<>();
        PartidaCierre c = null;
        String query = "SELECT sc.cod_subcuenta, COALESCE(SUM(CASE WHEN ld.transaccion = 'Haber' THEN ld.monto ELSE -ld.monto END), 0) AS saldo\n"
                + "FROM libro_diario ld \n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "WHERE sc.cod_subcuenta LIKE '2%'\n"
                + "GROUP BY sc.cod_subcuenta\n"
                + "HAVING \n"
                + "COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto ELSE -ld.monto END), 0) != 0.0;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c = new PartidaCierre();
                c.setCodigo(rs.getInt("cod_subcuenta"));
                c.setMonto(rs.getDouble("saldo"));
                listaCierre.add(c);
            }
        }
        return listaCierre;
    }

    private ArrayList<PartidaCierre> obtenerSaldoPatrimonio() throws SQLException {
        listaCierre = new ArrayList<>();
        PartidaCierre c = null;
        String query = "SELECT sc.cod_subcuenta, COALESCE(SUM(CASE WHEN ld.transaccion = 'Haber' THEN ld.monto ELSE -ld.monto END), 0) AS saldo\n"
                + "FROM libro_diario ld \n"
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta\n"
                + "WHERE sc.cod_subcuenta LIKE '3%'\n"
                + "GROUP BY sc.cod_subcuenta\n"
                + "HAVING \n"
                + "COALESCE(SUM(CASE WHEN ld.transaccion = 'Debe' THEN ld.monto ELSE -ld.monto END), 0) != 0.0;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                c = new PartidaCierre();
                c.setCodigo(rs.getInt("cod_subcuenta"));
                c.setMonto(rs.getDouble("saldo"));
                listaCierre.add(c);
            }
        }
        return listaCierre;
    }

    private double obtenerSaldoAbsoluto(String codCuenta) throws SQLException {
        String query = "SELECT COALESCE(SUM(monto), 0) AS saldo "
                + "FROM libro_diario ld "
                + "LEFT JOIN subcuentas sc ON ld.cod_subcuenta = sc.cod_subcuenta "
                + "LEFT JOIN cuentas_mayor cm ON sc.cod_mayor = cm.cod_mayor "
                + "WHERE cm.cod_mayor = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codCuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        }
        return 0.0;
    }

    public double obtener_Ventas() throws SQLException {
        double ventastotal = obtenerSaldoAbsoluto("5101");
        return ventastotal;
    }

    // Insertar una transacción en el libro diario
    public void insertarTransaccion(int numeroPartida, String codSubcuenta, double monto, String concepto, String transaccion) throws SQLException {
        String query = "INSERT INTO libro_diario (numero_partida, cod_subcuenta, fecha, monto, concepto, transaccion) "
                + "VALUES (?, ?, CURRENT_DATE, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, numeroPartida);
            stmt.setString(2, codSubcuenta);
            stmt.setDouble(3, monto);
            stmt.setString(4, concepto);
            stmt.setString(5, transaccion);
            stmt.executeUpdate();
        }
    }

    // Obtener el siguiente número de partida
    private int obtenerSiguienteNumeroPartida() throws SQLException {
        String query = "SELECT COALESCE(MAX(numero_partida), 0) + 1 AS siguiente_partida FROM libro_diario";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("siguiente_partida");
            }
        }
        return 1;
    }

    public int obtenerNumeroPartida() throws SQLException {
        String query = "SELECT COALESCE(MAX(numero_partida), 0) AS partida_actual FROM libro_diario";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("partida_actual");
            }
        }
        return 1;
    }

    public void ventasNetas() throws SQLException {
        double rebajas = obtenerSaldoAbsoluto("4104");
        double devoluciones = obtenerSaldoAbsoluto("4103");
        double total = rebajas + devoluciones;

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "510101", total, "Ventas Netas", "Debe");
        insertarTransaccion(numeroPartida, "410401", rebajas, "Ventas Netas", "Haber");
        insertarTransaccion(numeroPartida, "410301", devoluciones, "Ventas Netas", "Haber");
    }

    public void comprasTotales() throws SQLException {
        double gastos = obtenerSaldoAbsoluto("4102");

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "410101", gastos, "Por liquidacion de gastos", "Debe");
        insertarTransaccion(numeroPartida, "410201", gastos, "Por liquidacion de gastos", "Haber");
    }

    public void comprasNetas() throws SQLException {
        double devoluciones = obtenerSaldoAbsoluto("5102");
        double rebajas = obtenerSaldoAbsoluto("5103");
        double total = devoluciones + rebajas;

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "510201", devoluciones, "liquidacion de reb. y dev.", "Debe");
        insertarTransaccion(numeroPartida, "510301", rebajas, "liquidacion de reb. y dev.", "Debe");
        insertarTransaccion(numeroPartida, "410101", total, "liquidacion de reb. y dev.", "Haber");
    }

    public void mercaderiaDisponible() throws SQLException {
        double inventario = obtenerSaldoAbsoluto("1106");

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "410101", inventario, "saldar cuenta de inventario", "Debe");
        insertarTransaccion(numeroPartida, "110601", inventario, "saldar cuenta de inventario", "Haber");
    }

    public void costoVenta(Double inventario) throws SQLException {

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "111201", inventario, "determinar costo de venta y apertura de invent. Final", "Debe");
        insertarTransaccion(numeroPartida, "410101", inventario, "determinar costo de venta y apertura de invent. Final", "Haber");
    }

    public void utilidadBruta() throws SQLException {
        double compras = obtenerSaldo("4101");
        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "510101", compras, "utilidad bruta", "Debe");
        insertarTransaccion(numeroPartida, "410101", compras, "utilidad bruta", "Haber");
    }

    public void liquidarVentas(double ventas) throws SQLException {
        Double vent = obtenerSaldoHaber("5101");

        double g_admin = obtenerSaldo("4301");
        double otros_gastos = obtenerSaldo("4304");
        double g_ventas = obtenerSaldo("4302");
        double g_finan = obtenerSaldo("4303");
        double totalGastos = g_admin + otros_gastos + g_ventas + g_finan;

        double ingresos_finan = obtenerSaldoHaber("5201");
        double dividendos = obtenerSaldoHaber("5202");
        double otros_ingresos = obtenerSaldoHaber("5203");
        double ingresos_extra = obtenerSaldoHaber("5204");
        double totalIngresos = ingresos_finan + dividendos + otros_ingresos + ingresos_extra;

        double ventitas = vent + totalIngresos - totalGastos;
        System.out.println(vent);
        if (ventas >= 150000) {
            double res = (ventitas * 0.07);
            double isr = (ventitas - res) * 0.30;
            double perdida = vent - res - isr;
            int numeroPartida = obtenerSiguienteNumeroPartida();
            insertarTransaccion(numeroPartida, "510101", vent, "Apertura de perdidas y ganancias", "Debe");
            insertarTransaccion(numeroPartida, "310201", res, "Apertura de perdidas y ganancias", "Haber");
            insertarTransaccion(numeroPartida, "211101", isr, "Apertura de perdidas y ganancias", "Haber");
            insertarTransaccion(numeroPartida, "610101", perdida, "Apertura de perdidas y ganancias", "Haber");

        } else {
            double res = (ventitas * 0.07);
            double isr = (ventitas - res) * 0.25;
            double perdida = vent - res - isr;
            int numeroPartida = obtenerSiguienteNumeroPartida();
            insertarTransaccion(numeroPartida, "510101", vent, "Apertura de perdidas y ganancias", "Debe");
            insertarTransaccion(numeroPartida, "310201", res, "Apertura de perdidas y ganancias", "Haber");
            insertarTransaccion(numeroPartida, "211101", isr, "Apertura de perdidas y ganancias", "Haber");
            insertarTransaccion(numeroPartida, "610101", perdida, "Apertura de perdidas y ganancias", "Haber");
        }
    }

    public void liquidarGastos() throws SQLException {
        double g_admin = obtenerSaldo("4301");
        double g_ventas = obtenerSaldo("4302");
        double g_finan = obtenerSaldo("4303");
        double total = g_admin + g_ventas + g_finan;

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "610101", total, "liquidar gastos", "Debe");
        insertarTransaccion(numeroPartida, "430141", g_ventas, "liquidar gastos", "Haber");
        insertarTransaccion(numeroPartida, "430201", g_admin, "liquidar gastos", "Haber");
        insertarTransaccion(numeroPartida, "430303", g_finan, "liquidar gastos", "Haber");
    }

    public void liquidarOtrosGastos() throws SQLException {
        double otros_gastos = obtenerSaldo("4304");
        double ingresos_finan = obtenerSaldoHaber("5201");
        double dividendos = obtenerSaldoHaber("5202");
        double otros_ingresos = obtenerSaldoHaber("5203");
        double ingresos_extra = obtenerSaldoHaber("5204");
        double suma = (ingresos_finan + dividendos + otros_ingresos + ingresos_extra);
        if (otros_gastos == 0 && dividendos == 0 && otros_ingresos == 0 && ingresos_extra == 0) {
            System.out.println("no existen otros gastos, ni ingresos financieros, para procesar la partida");
        } else if (otros_gastos > suma) {
            double total = otros_gastos - suma;
            int numeroPartida = obtenerSiguienteNumeroPartida();
            insertarTransaccion(numeroPartida, "610101", total, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520103", ingresos_finan, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520201", dividendos, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520302", otros_ingresos, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520404", ingresos_extra, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "430402", otros_gastos, "liquidar otros gastos y otros productos", "Haber");

        } else if (otros_gastos < suma) {
            double total = suma - otros_gastos;
            int numeroPartida = obtenerSiguienteNumeroPartida();
            insertarTransaccion(numeroPartida, "520103", ingresos_finan, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520201", dividendos, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520302", otros_ingresos, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "520404", ingresos_extra, "liquidar otros gastos y otros productos", "Debe");
            insertarTransaccion(numeroPartida, "610101", total, "liquidar otros gastos y otros productos", "Haber");
            insertarTransaccion(numeroPartida, "430402", otros_gastos, "liquidar otros gastos y otros productos", "Haber");
        }
    }

    public void utilidadEjercicio() throws SQLException {
        double pyg = obtenerSaldoHaber("6101");

        int numeroPartida = obtenerSiguienteNumeroPartida();
        insertarTransaccion(numeroPartida, "610101", pyg, "liquidando perdidas y ganancias", "Debe");
        insertarTransaccion(numeroPartida, "310301", pyg, "liquidando perdidas y ganancias", "Haber");
    }

    public void partidaCierre() throws SQLException {
        int numeroPartida = obtenerSiguienteNumeroPartida();
        ArrayList<PartidaCierre> lista1 = obtenerSaldoActivo();
        ArrayList<PartidaCierre> lista2 = obtenerSaldoPasivo();
        ArrayList<PartidaCierre> lista3 = obtenerSaldoPatrimonio();

        for (PartidaCierre x : lista2) {
            insertarTransaccion(numeroPartida, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE CIERRE", "Debe");
        }
        for (PartidaCierre x : lista3) {
            insertarTransaccion(numeroPartida, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE CIERRE", "Debe");
        }
        for (PartidaCierre x : lista1) {
            insertarTransaccion(numeroPartida, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE CIERRE", "Haber");
        }
    }
    
    private void partidaInicio() throws SQLException {
        int numeroPartida = obtenerNumeroPartida();
        ArrayList<PartidaCierre> lista1 = obtenerSaldoActivoCierre(numeroPartida);
        ArrayList<PartidaCierre> lista2 = obtenerSaldoPasivoCierre(numeroPartida);
        ArrayList<PartidaCierre> lista3 = obtenerSaldoPatrimonioCierre(numeroPartida);

        for (PartidaCierre x : lista1) {
            insertarTransaccion(numeroPartida, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE APERTURA", "Debe");
        }
        for (PartidaCierre x : lista2) {
            insertarTransaccion(numeroPartida, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE APERTURA", "Debe");
        }
        for (PartidaCierre x : lista3) {
            insertarTransaccion(numeroPartida, String.valueOf(x.getCodigo()), x.getMonto(), "PARTIDA DE APERTURA", "Haber");
        }
    }

    // Cerrar la conexión automáticamente
    public void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
