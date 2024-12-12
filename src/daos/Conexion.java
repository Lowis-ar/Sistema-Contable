package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author guill
 */
public class Conexion {

    private Connection conexion;

    private String url = "jdbc:mysql://localhost:3306/sic";
    private String usuario = "root";
    private String password = "";//root

    public Connection getConexion() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("conectando a la BD");
        } catch (SQLException ex) {
        } catch (Exception e) {

        }
        return this.conexion;
    }

    public void cerrarConexiones() {
        if (conexion != null) {
            try {
                this.conexion.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
