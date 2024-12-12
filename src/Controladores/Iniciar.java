package controladores;

import Controladores.ControladorLogin;
import Vistas.Login;

/**
 *
 * @author kevin
 */
public class Iniciar {

    public static void main(String[] args) {
        Login login = new Login();
        ControladorLogin ctrl = new ControladorLogin(login);
        login.setVisible(true);

    }
}
