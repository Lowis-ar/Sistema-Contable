package utilidades;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 *
 * @author kevin
 */
public class BackupDatabase {

    public void createBackup() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione dónde guardar el respaldo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("SQL Files (*.sql)", "sql"));
            fileChooser.setApproveButtonText("Guardar");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
         
                File selectedFile = fileChooser.getSelectedFile();

                String backupFile = selectedFile.getAbsolutePath();
                if (!backupFile.endsWith(".sql")) {
                    backupFile += ".sql";
                }

                String mysqldumpPath = "C:/xampp/mysql/bin/mysqldump.exe";

                String command = mysqldumpPath + " -uroot --databases sic";

                Process process = Runtime.getRuntime().exec(command);

                InputStream inputStream = process.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(backupFile);

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                fileOutputStream.close();

                JOptionPane.showMessageDialog(null, "El respaldo se realizó correctamente en: " + backupFile);
            } else {
                JOptionPane.showMessageDialog(null, "Operación cancelada por el usuario.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al realizar el backup.");
        }
    }
}
