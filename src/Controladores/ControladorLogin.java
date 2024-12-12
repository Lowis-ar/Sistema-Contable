package Controladores;

import Vistas.Login;
import ds.desktop.notify.DesktopNotify;
import ds.desktop.notify.NotifyTheme;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vistas.Dashboard;

/**
 *
 * @author kevin
 */
public class ControladorLogin implements ActionListener {

    Login login;
    Dashboard dashboard;
    
    private static final String USUARIO_ADMIN = "admin";
    private static final String CLAVE_ADMIN = "admin";

    public ControladorLogin(Login login) {
        this.login = login;
        this.login.btnIngresar.addActionListener(this);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.login.btnIngresar) {
            verificarAcceso();
        }
    }

    public void verificarAcceso() {
        String userInput = login.tfUsuario.getText();
        String claveInput = new String(login.jPasswordField1.getPassword());

        if (userInput.isEmpty() || claveInput.isEmpty()) {
            DesktopNotify.setDefaultTheme(NotifyTheme.Red);
            DesktopNotify.showDesktopMessage("Error", "Debe ingresar usuario y contraseña", DesktopNotify.INFORMATION, 4000);
        } else {
            if (userInput.equals(USUARIO_ADMIN) && claveInput.equals(CLAVE_ADMIN)) {
                dashboard = new Dashboard();
                dashboard.setVisible(true);
                login.dispose();
            } else {
                DesktopNotify.setDefaultTheme(NotifyTheme.Red);
                DesktopNotify.showDesktopMessage("Acceso Denegado", "Usuario o clave incorrecta", DesktopNotify.INFORMATION, 4000);
            }
        }
    }

}
