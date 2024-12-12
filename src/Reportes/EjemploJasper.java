package reportes;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import daos.Conexion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


public class EjemploJasper {

    public EjemploJasper(){

        try{

            JasperReport report;
            JasperPrint jprint=null;
            Conexion x = new Conexion();

            int options = Integer.parseInt(JOptionPane.showInputDialog("GENERAR INFORMES\n"
                    + "1 - MySQL DataSource\n"
                    + "otro - Salir"));
            
            
            switch(options){
            
                case 1:
                    report = (JasperReport) JRLoader.loadObject(getClass().getResource("/reportes/Comprobacion.jasper"));
                    jprint = JasperFillManager.fillReport(report, null,x.getConexion());                                                                    
                break;
                
                default:
                    System.out.println("Finalizando proceso...");
                break;
                
            }
            
            if(jprint!=null){            
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                view.setVisible(true);                        
            }
            
        }catch(HeadlessException | NumberFormatException | JRException ex){
            System.out.println(ex.getMessage());
        }
    
    
    }
    

    public static void main(String[] args) {

        
        EjemploJasper jasper = new EjemploJasper();
        

    }
    
}

