package Reportes;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import daos.ConnectionPool;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Jasper {

    public void Reporte(int n) {

        try {

            JasperReport report;
            JasperPrint jprint = null;

            switch (n) {

                case 1 -> {
                    report = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/BalanceGeneral.jasper"));
                    jprint = JasperFillManager.fillReport(report, null, ConnectionPool.getInstance().getConnection());
                }

                case 2 -> {
                    report = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/Comprobacion.jasper"));
                    jprint = JasperFillManager.fillReport(report, null, ConnectionPool.getInstance().getConnection());
                }
                
                case 3 -> {
                    report = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/EResultados.jasper"));
                    jprint = JasperFillManager.fillReport(report, null, ConnectionPool.getInstance().getConnection());
                }
                case 4 -> {
                    report = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/JRdetallesMayor.jasper"));
                    jprint = JasperFillManager.fillReport(report, null, ConnectionPool.getInstance().getConnection());
                }
                case 5 -> {
                    report = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/JRmayor.jasper"));
                    jprint = JasperFillManager.fillReport(report, null, ConnectionPool.getInstance().getConnection());
                }


                default ->
                    System.out.println("Finalizando proceso...");

            }

            if (jprint != null) {
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                view.setVisible(true);
            }

        } catch (HeadlessException | NumberFormatException | JRException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Jasper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void main(String[] args) {

        Jasper jasper = new Jasper();
        jasper.Reporte(4);
    }

}
