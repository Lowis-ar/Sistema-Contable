package daos;

import java.math.BigDecimal;
import java.util.ArrayList;
import modelos.LibroDiario;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import modelos.SubCuentas;

/**
 *
 * @author kevin
 */
public class DaoLibroDiario {

    Conexion conexion;
    private ArrayList<LibroDiario> listaLibroDiario;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
    private LibroDiario libroDiario;

    private static final String INSERT_LIBRO_DIARIO = "INSERT INTO libro_diario"
            + "(numero_partida, fecha, cod_subcuenta, concepto, monto, transaccion) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ALL_LIBRO_DIARIO = "SELECT ld.numero_partida, ld.fecha, ld.cod_subcuenta, s.nombre AS nombreCuenta, ld.concepto, ld.monto, ld.transaccion FROM libro_diario ld INNER JOIN subcuentas s ON ld.cod_subcuenta = s.cod_subcuenta ";

    private static final String UPDATE_LIBRO_DIARIO = "UPDATE libro_diario SET fecha=?, cod_subcuenta=?, concepto=?, monto=?, transaccion=? WHERE numero_partida=? ";
    
    private static final String MOSTRAS_N_PARTIDA = "UPDATE libro_diario SET fecha=?, cod_subcuenta=?, concepto=?, monto=?, transaccion=? WHERE numero_partida=? ";

    public DaoLibroDiario() {
        this.conexion = new Conexion();
    }

    public String insertarLibroDiario(LibroDiario libroDiario) throws SQLException, ClassNotFoundException {
        String resultado;
        int resultadoInsertar;
        try {
            this.conexion = new Conexion();
            this.conexion.getConexion();
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(INSERT_LIBRO_DIARIO);
            this.ps.setInt(1, libroDiario.getNumeroPartida());
            this.ps.setDate(2, new java.sql.Date(libroDiario.getFecha().getTime()));
            this.ps.setInt(3, libroDiario.getCodSubcuenta());
            this.ps.setString(4, libroDiario.getConcepto());
            this.ps.setBigDecimal(5, BigDecimal.valueOf(libroDiario.getMonto()));
            this.ps.setString(6, libroDiario.getTransaccion());

            System.out.println("libro_diario_insertar: " + libroDiario);

            resultadoInsertar = this.ps.executeUpdate();
            this.conexion.cerrarConexiones();

            if (resultadoInsertar > 0) {
                resultado = "exito";
            } else {
                resultado = "error_insertar_libro_diario";
            }

        } catch (SQLException e) {
            resultado = "error_excepcion";
            System.out.println("Fallo al insertar: " + e.getErrorCode());
            e.printStackTrace();
        }

        return resultado;
    }

    public ArrayList<LibroDiario> selectAllLibroDiario() {
        ArrayList<LibroDiario> listaLibrosDiarios = new ArrayList<>();
        try {
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(SELECT_ALL_LIBRO_DIARIO);
            this.rs = this.ps.executeQuery();

            while (rs.next()) {
                LibroDiario libro = new LibroDiario(
                        rs.getInt("numero_partida"),
                        rs.getDate("fecha"),
                        rs.getInt("cod_subcuenta"),
                        rs.getString("nombreCuenta"), 
                        rs.getDouble("monto"),
                        rs.getString("transaccion"),
                        rs.getString("concepto")
                );
                listaLibrosDiarios.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener todos los registros del libro diario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (accesoDB != null) {
                    accesoDB.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listaLibrosDiarios;
    }

    public String updateLibroDiario(LibroDiario libroDiario) {
        String resultado;
        try {
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(UPDATE_LIBRO_DIARIO);
            this.ps.setDate(1, new java.sql.Date(libroDiario.getFecha().getTime()));
            this.ps.setInt(2, libroDiario.getCodSubcuenta());
            this.ps.setString(3, libroDiario.getConcepto());
            this.ps.setBigDecimal(4, BigDecimal.valueOf(libroDiario.getMonto()));
            this.ps.setString(5, libroDiario.getTransaccion());
            this.ps.setInt(6, libroDiario.getNumeroPartida());

            int filasActualizadas = this.ps.executeUpdate();
            if (filasActualizadas > 0) {
                resultado = "exito";
            } else {
                resultado = "error_actualizar_libro_diario";
            }
        } catch (SQLException e) {
            resultado = "error_excepcion";
            e.printStackTrace();
            System.out.println("Error al actualizar el libro diario: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (accesoDB != null) {
                    accesoDB.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return resultado;
    }

    public ArrayList<LibroDiario> selectLibroDiarioByPartida(int numeroPartida) {
    ArrayList<LibroDiario> listaLibrosDiarios = new ArrayList<>();
    try {
        this.accesoDB = conexion.getConexion();
        
        String SELECT_LIBRO_DIARIO_PARTIDA = "SELECT * FROM libro_diario WHERE numero_partida = ?";
        this.ps = this.accesoDB.prepareStatement(SELECT_LIBRO_DIARIO_PARTIDA);
        this.ps.setInt(1, numeroPartida);

        this.rs = this.ps.executeQuery();

        while (rs.next()) {
            LibroDiario libro = new LibroDiario(
                    rs.getInt("numero_partida"),
                    rs.getDate("fecha"),
                    rs.getInt("cod_subcuenta"),
                    rs.getString("nombreCuenta"), 
                    rs.getDouble("monto"),
                    rs.getString("transaccion"),
                    rs.getString("concepto")
            );
            listaLibrosDiarios.add(libro);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error al obtener los registros del libro diario para la partida " + numeroPartida + ": " + e.getMessage());
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (accesoDB != null) {
                accesoDB.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    return listaLibrosDiarios;
}

    public ArrayList<SubCuentas> obtenerSubCuentas() {
        ArrayList<SubCuentas> listaSubCuentas = new ArrayList<>();
        String sql = "SELECT cod_subcuenta, nombre FROM subcuentas ORDER BY cod_subcuenta";
        try {
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(sql);
            this.rs = this.ps.executeQuery();
            while (rs.next()) {
                SubCuentas subCuenta = new SubCuentas();
                subCuenta.setCod_subcuenta(rs.getString("cod_subcuenta"));
                subCuenta.setNombre_sub(rs.getString("nombre"));
                listaSubCuentas.add(subCuenta);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener subcuentas: " + ex.getMessage());
        }
        return listaSubCuentas;
    }

    public int obtenerUltimaPartida() {
        int ultimaPartida = 0;
        String sql = "SELECT MAX(numero_partida) AS ultimaPartida FROM libro_diario";
        try {
            conexion = new Conexion();
            this.accesoDB = conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(sql);
            this.rs = this.ps.executeQuery();

            if (rs.next()) {
                ultimaPartida = rs.getInt("ultimaPartida");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al consultar la última partida: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (accesoDB != null) {
                    accesoDB.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return ultimaPartida;
    }
}
