/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelos.LibroMayor;
import modelos.SubCuentas;
import modelos.detallesMayor;




public class DaoMayor {
    
    private Conexion conexion;
    private ArrayList<LibroMayor> listaLibroMayor;
 
    private ArrayList<detallesMayor>listDetallesSub;
    private ResultSet rs = null;
    private PreparedStatement ps;
    private Connection accesoDB;
   
     private static final String Mostar = "SELECT \n" +
"    cm.cod_mayor AS codigo_cuenta_mayor,\n" +
"    cm.nombre AS nombre_cuenta_mayor,\n" +
"    SUM(CASE WHEN ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL(10,2)) ELSE 0 END) AS debe,\n" +
"    SUM(CASE WHEN ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL(10,2)) ELSE 0 END) AS haber,\n" +
"    CASE \n" +
"        WHEN cm.naturaleza = 'DEUDOR' THEN \n" +
"            SUM(CASE WHEN ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL(10,2)) ELSE 0 END) \n" +
"            - \n" +
"            SUM(CASE WHEN ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL(10,2)) ELSE 0 END)\n" +
"        WHEN cm.naturaleza = 'ACREEDOR' THEN \n" +
"            SUM(CASE WHEN ld.transaccion = 'Haber' THEN CAST(ld.monto AS DECIMAL(10,2)) ELSE 0 END) \n" +
"            - \n" +
"            SUM(CASE WHEN ld.transaccion = 'Debe' THEN CAST(ld.monto AS DECIMAL(10,2)) ELSE 0 END)\n" +
"        ELSE 0\n" +
"    END AS saldo\n" +
"FROM \n" +
"    cuentas_mayor cm\n" +
"JOIN \n" +
"    subcuentas sc ON cm.cod_mayor = sc.cod_mayor\n" +
"JOIN \n" +
"    libro_diario ld ON sc.cod_subcuenta = ld.cod_subcuenta\n" +
"GROUP BY \n" +
"    cm.cod_mayor, cm.nombre, cm.naturaleza\n" +
"ORDER BY \n" +
"    cm.cod_mayor;";
     
     
      private static final String MostarSub = "SELECT"
              + " cm.cod_mayor AS id_cuenta_mayor,"
              + " sc.cod_subcuenta AS id_subcuenta,"
              + " sc.nombre AS nombre_subcuenta,"
              + " ld.numero_partida AS numero_partida,"
              + " ld.fecha AS fecha_agregado,"
              + " ld.concepto AS concepto,"
              + " ld.monto AS monto,"
              + " ld.transaccion AS transaccion FROM cuentas_mayor cm INNER JOIN subcuentas sc ON cm.cod_mayor = sc.cod_mayor INNER JOIN libro_diario ld ON sc.cod_subcuenta = ld.cod_subcuenta ORDER BY cm.cod_mayor, sc.cod_subcuenta, ld.numero_partida;";
   
      private static String DeleteTablas="Delete from like ?";

   public boolean insertar(LibroMayor obj){
    String sql ="INSERT INTO libromayor(\n"
            + "	codigo, nombre, debe, haber, saldo)\n"
            + "	VALUES (?, ?, ?, ?,  ?);";
    return insertarRegristro(sql,obj);
}
   
    public boolean insertarSub(detallesMayor obj){
    String sql ="INSERT INTO detalles_mayor(\n"
            + "	codigoM,fecha,codigoS,nombre, numeroPartida, concepto, monto,transaccion)\n"
            + "	VALUES (?, ?, ?, ?,  ?,?,?,?);";
    return insertarRegristroSub(sql,obj);
}
    
    
    
    public DaoMayor() {
    this.conexion = new Conexion(); // Inicializa la conexión aquí.
}

     
      public ArrayList<LibroMayor> seleccionaDatos()  {

        this.listaLibroMayor = new ArrayList();

        try {
            this.conexion = new Conexion();
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(Mostar);
            this.rs = ps.executeQuery();

            LibroMayor obj = null;
            while (this.rs.next()) {
                obj = new LibroMayor();
                obj.setCodigo(rs.getString("codigo_cuenta_mayor"));
                obj.setNombre(rs.getString("nombre_cuenta_mayor"));
                
                obj.setDebe(rs.getDouble("debe"));
                obj.setHaber(rs.getDouble("haber"));
                obj.setSaldo(rs.getDouble("saldo"));
                this.listaLibroMayor.add(obj);
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.listaLibroMayor;
    }
      
      
      public ArrayList<detallesMayor> seleccionaDatosSub()  {

        this.listDetallesSub = new ArrayList();

        try {
            this.conexion = new Conexion();
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(MostarSub);
            this.rs = ps.executeQuery();

            detallesMayor obj = null;
            while (this.rs.next()) {
                obj = new detallesMayor();
                 obj.setIdMayor(rs.getString("id_cuenta_mayor"));
                 obj.setIdSubCuenta(rs.getString("id_subcuenta"));
                 obj.setNombreSub(rs.getString("nombre_subcuenta"));
                  obj.setPartida(rs.getInt("numero_partida"));
                obj.setFecha(rs.getDate("fecha_agregado"));
              
                obj.setConcepto(rs.getString("concepto"));
                obj.setMonto(rs.getDouble("monto"));
                obj.setTransaccion(rs.getString("transaccion"));
                this.listDetallesSub.add(obj);
            }
            this.conexion.cerrarConexiones();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.listDetallesSub;
    }
      
        private boolean insertarRegristro(String sql,LibroMayor obj) {
        try {
            
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(sql);
            this.ps.setString(1, obj.getCodigo());
            this.ps.setString(2, obj.getNombre());
            this.ps.setDouble(3, obj.getDebe());
            this.ps.setDouble(4, obj.getHaber());
            this.ps.setDouble(5, obj.getSaldo());
          
            

            ps.execute();
            
             this.conexion.cerrarConexiones();
             
           return true;

           

        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        
    }
        
         private boolean insertarRegristroSub(String sql,detallesMayor obj) {
        try {
            
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(sql);
            this.ps.setString(1, obj.getIdMayor());
            this.ps.setDate(2, new java.sql.Date(obj.getFecha().getTime()));
            this.ps.setString(3, obj.getIdSubCuenta());
            this.ps.setString(4, obj.getNombreSub());
            this.ps.setInt(5, obj.getPartida());
             this.ps.setString(6, obj.getConcepto());
            this.ps.setDouble(7, obj.getMonto());
            this.ps.setString(8, obj.getTransaccion());
          
            

            ps.execute();
            
             this.conexion.cerrarConexiones();
             
           return true;

           

        } catch (SQLException e) {
            e.printStackTrace();
           return false;
        }
        
    }
      
        public ArrayList<detallesMayor> MostrarDetalles(String busqueda) {
        ArrayList<detallesMayor> listaDetalles = new ArrayList<>();

        try {
            this.accesoDB = this.conexion.getConexion();
            this.ps = this.accesoDB.prepareStatement(
                    "SELECT \n" +
"    cm.cod_mayor AS id_cuenta_mayor,\n" +
"    sc.cod_subcuenta AS id_subcuenta,\n" +
"    sc.nombre AS nombre_subcuenta,\n" +
"    ld.numero_partida AS numero_partida,\n" +
"    ld.fecha AS fecha_agregado,\n" +
"    ld.concepto AS concepto,\n" +
"    ld.monto AS monto,\n" +
"    ld.transaccion AS transaccion\n" +
"FROM \n" +
"    cuentas_mayor cm\n" +
"INNER JOIN \n" +
"    subcuentas sc ON cm.cod_mayor = sc.cod_mayor\n" +
"INNER JOIN \n" +
"    libro_diario ld ON sc.cod_subcuenta = ld.cod_subcuenta\n" +
"WHERE \n" +
"    cm.nombre LIKE ? \n" +
"ORDER BY \n" +
"    cm.cod_mayor, sc.cod_subcuenta, ld.numero_partida;"
            );

            String queryBusqueda = "%" + busqueda + "%";
            for (int i = 1; i <= 1; i++) {
                this.ps.setString(i, queryBusqueda);
            }

            this.rs = ps.executeQuery();

            detallesMayor obj;
            while (this.rs.next()) {
                obj = new detallesMayor();
               
                obj.setIdMayor(rs.getString("id_cuenta_mayor"));
                 obj.setIdSubCuenta(rs.getString("id_subcuenta"));
                 obj.setNombreSub(rs.getString("nombre_subcuenta"));
                  obj.setPartida(rs.getInt("numero_partida"));
                obj.setFecha(rs.getDate("fecha_agregado"));
              
                obj.setConcepto(rs.getString("concepto"));
                obj.setMonto(rs.getDouble("monto"));
                obj.setTransaccion(rs.getString("transaccion"));
               
                listaDetalles.add(obj);
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

        return listaDetalles;
    }
        
        public boolean eliminarTodos(String nombreTabla) {
        String sql = "DELETE FROM " + nombreTabla;
        
        try {
            this.accesoDB = this.conexion.getConexion();
            PreparedStatement ps = this.accesoDB.prepareStatement(sql);
            ps.executeUpdate();
            this.conexion.cerrarConexiones();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (this.accesoDB != null) {
                    this.accesoDB.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
      
      
   
}
