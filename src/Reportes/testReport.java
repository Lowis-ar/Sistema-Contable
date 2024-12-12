package reportes;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.JRExporterParameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class testReport {

    public static void main(String[] args) {
        try {
            // Ruta del archivo .jasper (reporte compilado)
            String reporteJasper = "src/reportes/Comprobacion.jasper"; // Cambia esta ruta
            // Salida del archivo PDF
            String salidaPDF = "src/reportes/reporte.pdf"; // Cambia esta ruta

            // Verificar si el archivo Jasper existe
            File archivoJasper = new File(reporteJasper);
            if (!archivoJasper.exists()) {
                throw new IllegalArgumentException("El archivo .jasper no se encuentra en la ruta especificada: " + reporteJasper);
            }

            // Parámetros del reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("TITULO", "Reporte de Prueba"); // Ejemplo de parámetro

            // Fuente de datos vacía (para pruebas sin base de datos)
            JRDataSource dataSource = new JREmptyDataSource();

            // Llenar el reporte con los parámetros y la fuente de datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporteJasper, parametros, dataSource);

            // Exportar a PDF
            exportarReporteAPDF(jasperPrint, salidaPDF);

            System.out.println("¡Reporte generado con éxito en: " + salidaPDF);

        } catch (Exception e) {
            System.err.println("Ocurrió un error al generar el reporte:");
            e.printStackTrace();
        }
    }

    // Método para exportar el reporte a un archivo PDF
    private static void exportarReporteAPDF(JasperPrint jasperPrint, String salidaPDF) throws JRException {
        File archivoPDF = new File(salidaPDF);
        if (!archivoPDF.getParentFile().exists()) {
            archivoPDF.getParentFile().mkdirs(); // Crear directorios si no existen
        }

        // Exportador de PDF
        JRPdfExporter exporter = new JRPdfExporter();

        // Configuración del exportador (para versiones antiguas)
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, salidaPDF);

        // Exportar
        exporter.exportReport();
    }
}
