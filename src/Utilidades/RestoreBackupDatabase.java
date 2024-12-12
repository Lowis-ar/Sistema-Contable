package utilidades;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 *
 * @author kevin
 */
public class RestoreBackupDatabase {

    public void restoreBackup() {
        new Thread(() -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccione el archivo de respaldo para restaurar");
                fileChooser.setFileFilter(new FileNameExtensionFilter("SQL Files (*.sql)", "sql"));
                fileChooser.setApproveButtonText("Restaurar");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    File selectedFile = fileChooser.getSelectedFile();

                    if (selectedFile.exists() && selectedFile.getName().endsWith(".sql")) {

                        String mysqlPath = "C:/xampp/mysql/bin/mysql.exe";

                        // Comando para eliminar la base de datos si existe
                        String dropDbCommand = mysqlPath + " -uroot -e \"DROP DATABASE IF EXISTS sic\"";

                        ProcessBuilder dropDbProcessBuilder = new ProcessBuilder("cmd", "/c", dropDbCommand);
                        dropDbProcessBuilder.redirectErrorStream(true);

                        Process dropDbProcess = dropDbProcessBuilder.start();

                        BufferedReader dropDbReader = new BufferedReader(new InputStreamReader(dropDbProcess.getInputStream()));

                        String dropDbLine;
                        while ((dropDbLine = dropDbReader.readLine()) != null) {
                            System.out.println(dropDbLine);
                        }

                        int dropDbExitCode = dropDbProcess.waitFor();

                        if (dropDbExitCode == 0) {
                            System.out.println("Base de datos 'sic' eliminada exitosamente.");
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, "Error al eliminar la base de datos 'sic'.");
                            });
                            return;
                        }

                        // Comando para crear la base de datos
                        String createDbCommand = mysqlPath + " -uroot -e \"CREATE DATABASE IF NOT EXISTS sic\"";

                        ProcessBuilder createDbProcessBuilder = new ProcessBuilder("cmd", "/c", createDbCommand);
                        createDbProcessBuilder.redirectErrorStream(true);

                        Process createDbProcess = createDbProcessBuilder.start();

                        BufferedReader createDbReader = new BufferedReader(new InputStreamReader(createDbProcess.getInputStream()));

                        String createDbLine;
                        while ((createDbLine = createDbReader.readLine()) != null) {
                            System.out.println(createDbLine);
                        }

                        int createDbExitCode = createDbProcess.waitFor();

                        if (createDbExitCode == 0) {
                            System.out.println("Base de datos 'sic' verificada/creada exitosamente.");
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, "Error al crear/verificar la base de datos 'sic'.");
                            });
                            return;
                        }

                        // Comando para restaurar la base de datos desde el archivo .sql
                        String restoreCommand = mysqlPath + " -uroot sic -e \"source " + selectedFile.getAbsolutePath() + "\"";

                        ProcessBuilder restoreProcessBuilder = new ProcessBuilder("cmd", "/c", restoreCommand);
                        restoreProcessBuilder.redirectErrorStream(true);

                        Process restoreProcess = restoreProcessBuilder.start();

                        BufferedReader restoreReader = new BufferedReader(new InputStreamReader(restoreProcess.getInputStream()));

                        String restoreLine;
                        while ((restoreLine = restoreReader.readLine()) != null) {
                            System.out.println(restoreLine);
                        }

                        int restoreExitCode = restoreProcess.waitFor();

                        if (restoreExitCode == 0) {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, "Se restauró la base de datos correctamente.");
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, "Error al restaurar el backup.");
                            });
                        }
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, "El archivo seleccionado no es válido o no tiene extensión .sql.");
                        });
                    }
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Operación cancelada por el usuario.");
                    });
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Hubo un error al restaurar la base de datos.");
                });
            }
        }).start();
    }
}
