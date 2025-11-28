package Controladores;

import Reportes.Jasper;
import javax.swing.JFrame;
import Vistas.VistaBalanceGenerales;
import daos.DaoBalanceGeneral;
import daos.DaoBalanceGeneral.BalanceData;
import daos.DaoEstadoResultado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelos.BalanceGeneral;

public class ControladorBalanceGeneral implements ActionListener {

    private final VistaBalanceGenerales vista;
    private final DaoBalanceGeneral dao = new DaoBalanceGeneral();
    private final DaoEstadoResultado daoResultado = new DaoEstadoResultado();
    private final Jasper jasper = new Jasper();

    private final Calendar calendario = Calendar.getInstance();
    private final int anio = calendario.get(Calendar.YEAR);

    DecimalFormat df = new DecimalFormat("#.##");

    public ControladorBalanceGeneral(VistaBalanceGenerales v) {
        this.vista = v;
        this.vista.Btn_Guardar.addActionListener(this);
        this.vista.Btn_Imprimir.addActionListener(this);
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setAnio.setText(String.valueOf(anio));
        cargarTablas();
        vista.setVisible(true);
    }

    private void cargarTablas() {
        // Obtener datos procesados del DAO
        BalanceData data = dao.cargarBalance(anio);

        // Construcción de tablas
        DefaultTableModel ac = new DefaultTableModel(new Object[]{"Cuenta", "Monto", "Tipo"}, 0);
        DefaultTableModel pc = new DefaultTableModel(new Object[]{"Cuenta", "Monto", "Tipo"}, 0);

        // Activos Corriente
        ac.addRow(new Object[]{"ACTIVO CORRIENTE", "", df.format(data.totalAC)});
        for (BalanceGeneral b : data.AC) ac.addRow(new Object[]{b.getCuenta(), b.getMonto(), ""});

        // Activo No Corriente
        ac.addRow(new Object[]{"ACTIVO NO CORRIENTE", "", df.format(data.totalANC)});
        for (BalanceGeneral b : data.ANC) ac.addRow(new Object[]{b.getCuenta(), b.getMonto(), ""});

        // Pasivo Corriente
        pc.addRow(new Object[]{"PASIVO CORRIENTE", "", df.format(data.totalPC)});
        for (BalanceGeneral b : data.PC) pc.addRow(new Object[]{b.getCuenta(), b.getMonto(), ""});

        // Pasivo No Corriente
        pc.addRow(new Object[]{"PASIVO NO CORRIENTE", "", df.format(data.totalPNC)});
        for (BalanceGeneral b : data.PNC) pc.addRow(new Object[]{b.getCuenta(), b.getMonto(), ""});

        // Capital
        pc.addRow(new Object[]{"CAPITAL CONTABLE", "", df.format(data.totalCAP)});
        for (BalanceGeneral b : data.CAP) pc.addRow(new Object[]{b.getCuenta(), b.getMonto(), ""});

        vista.tabla_activos.setModel(ac);
        vista.tabla_pasivos.setModel(pc);

        double totalActivos = data.totalAC + data.totalANC;
        double totalPasivos = data.totalPC + data.totalPNC + data.totalCAP;

        vista.totalActivos.setText(df.format(totalActivos));
        vista.totalPasivos.setText(df.format(totalPasivos));
    }

    private boolean guardarBalance() {
        int n = dao.generarNumeroBalance();

        if (!dao.iniciarBalance(n, anio)) {
            JOptionPane.showMessageDialog(vista, "Error al iniciar balance general");
            return false;
        }

        BalanceData data = dao.cargarBalance(anio);

        // Guardar detalles por tipo
        insertarLista(data.AC, n, 1);
        insertarLista(data.ANC, n, 2);
        insertarLista(data.PC, n, 3);
        insertarLista(data.PNC, n, 4);
        insertarLista(data.CAP, n, 5);

        double totalActivos = data.totalAC + data.totalANC;
        double totalPasivos = data.totalPC + data.totalPNC + data.totalCAP;

        boolean ok = dao.insertarTotales(
                data.totalAC, data.totalANC,
                data.totalPC, data.totalPNC,
                data.totalCAP,
                totalActivos, totalPasivos,
                n
        );

        if (!ok) {
            JOptionPane.showMessageDialog(vista, "Error guardando totales");
            return false;
        }

        JOptionPane.showMessageDialog(vista, "Balance guardado correctamente");
        return true;
    }

    private void insertarLista(java.util.ArrayList<BalanceGeneral> lista, int n, int tipo) {
        for (BalanceGeneral b : lista) {
            dao.insertarDetalle(b.getCuenta(), Double.parseDouble(b.getMonto()), n, tipo);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.Btn_Guardar) {
            guardarBalance();
        }
        if (e.getSource() == vista.Btn_Imprimir) {
            vista.setVisible(false);
            jasper.Reporte(1);
        }
    }
}
