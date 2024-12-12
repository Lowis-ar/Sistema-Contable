package Controladores;

import Reportes.Jasper;
import javax.swing.JFrame;
import Vistas.VistaBalanceGenerales;
import daos.Conexion;
import daos.DaoBalanceGeneral;
import daos.DaoEstadoResultado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelos.BalanceGeneral;
import modelos.EstadoResultado;

/**
 *
 * @author Luis
 */
public class ControladorBalanceGeneral implements ActionListener {

    Calendar calendario = Calendar.getInstance();
    int anio = calendario.get(Calendar.YEAR);

    String activo = "ACTIVO CORRIENTE", aN = "ACTIVO NO CORRIENTE", pasivo = "PASIVO CORRIENTE", pN = "PASIVO NO CORRIENTE", patrimonio = "CAPITAL CONTABLE";
    int tamaño = 0;

    DecimalFormat decimal = new DecimalFormat("#.##");

    VistaBalanceGenerales vista = new VistaBalanceGenerales(new JFrame(), true);
    DaoBalanceGeneral dao = new DaoBalanceGeneral();

    DaoEstadoResultado daoResultado = new DaoEstadoResultado();
    EstadoResultado estado = new EstadoResultado();

    Jasper jasper = new Jasper();

    DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel dtm2 = new DefaultTableModel();

    ArrayList<BalanceGeneral> listaActivos = new ArrayList();
    ArrayList<BalanceGeneral> lisAcNocorrientes = new ArrayList();
    ArrayList<BalanceGeneral> listaPasivos = new ArrayList();
    ArrayList<BalanceGeneral> listaPasivosNoCorrientes = new ArrayList();
    ArrayList<BalanceGeneral> listaPatrimonio = new ArrayList();

    Double reservaLegal = getReservaLegal();
    Double ResEjercicio = getResultadoEjercicio();
    Double impuesto = impuestoSobreRenta();

    Conexion con = new Conexion();

    public ControladorBalanceGeneral(VistaBalanceGenerales v) {
        this.vista = v;
        this.vista.Btn_Guardar.addActionListener(this);
        this.vista.Btn_Imprimir.addActionListener(this);
    }

    public void setModels() {
        
        this.vista.setAnio.setText(String.valueOf(anio));
        
        //--para mostrar activos
        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilita la edición de celdas
            }
        };

        dtm.addColumn("Activos");
        dtm.addColumn("Total Activos");
        dtm.addColumn("Total:");

        vista.tabla_activos.setModel(dtm);

        //--para mostrar pasivos
        dtm2 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilita la edición de celdas
            }
        };

        dtm2.addColumn("Pasivos");
        dtm2.addColumn("Total Pasivos");
        dtm2.addColumn("Total:");

        vista.tabla_pasivos.setModel(dtm2);

        listaActivos = dao.CargarBalanceGeneralActivos(activo, anio);
        lisAcNocorrientes = dao.CargarBalanceGeneralActivos(aN, anio);
        listaPasivos = dao.CargarBalanceGeneralPasivos(pasivo, anio);
        listaPasivosNoCorrientes = dao.CargarBalanceGeneralPasivos(pN, anio);
        listaPatrimonio = dao.CargarBalanceGeneralPasivos(patrimonio, anio);

        agregarATabla();
    }

    public void agregarATabla() {
        Double total_AC = 0.00;
        int aux = 0;

        //CONSULTAR EL TOTAL DE ACTIVOS CORRIENTES
        for (int i = 0; i < listaActivos.size(); i++) {
            total_AC = total_AC + Double.parseDouble(listaActivos.get(i).getMonto());
        }

        //CONSULTAR CADA CUENTA DE ACTIVOS CORRIENTES
        dtm.addRow(new Object[]{"ACTIVO CORRIENTE", " ", total_AC});
        for (int i = 0; i < listaActivos.size(); i++) {
            dtm.addRow(new Object[]{listaActivos.get(i).getCuenta(), listaActivos.get(i).getMonto(), " "});
        }

        //CONSULTAR EL TOTAL DE ACTIVOS NO CORRIENTES
        Double total_ANC = 0.00;
        for (int i = 0; i < lisAcNocorrientes.size(); i++) {
            total_ANC = total_ANC + Double.parseDouble(lisAcNocorrientes.get(i).getMonto());
        }
        //CONSULTAR CADA CUENTA DE ACTIVOS NO CORRIENTES
        dtm.addRow(new Object[]{"ACTIVO NO CORRIENTE", " ", total_ANC});
        for (int i = 0; i < lisAcNocorrientes.size(); i++) {
            dtm.addRow(new Object[]{lisAcNocorrientes.get(i).getCuenta(), lisAcNocorrientes.get(i).getMonto(), " "});
        }

        //CONSULTAR EL TOTAL DE PASIVOS CORRIENTES
        Double total_PC = 0.00;
        for (int i = 0; i < listaPasivos.size(); i++) {
            Double a = total_PC + Double.parseDouble(listaPasivos.get(i).getMonto());
            total_PC = Double.parseDouble(String.format("%.2f", a));
        }
        //CONSULTAR CADA CUENTA DE PASIVOS CORRIENTES
        dtm2.addRow(new Object[]{"PASIVO CORRIENTE", " ", total_PC + impuesto});
        for (int i = 0; i < listaPasivos.size(); i++) {
            dtm2.addRow(new Object[]{listaPasivos.get(i).getCuenta(), listaPasivos.get(i).getMonto(), " "});
        }
        dtm2.addRow(new Object[]{"IMPUESTO POR PAGAR", impuesto, " "});

        //CONSULTAR EL TOTAL DE PASIVOS NO CORRIENTES
        Double total_PNC = 0.00;
        for (int i = 0; i < listaPasivosNoCorrientes.size(); i++) {
            Double b = total_PNC + Double.parseDouble(listaPasivosNoCorrientes.get(i).getMonto());
            total_PNC = Double.parseDouble(String.format("%.2f", b));
        }
        //CONSULTAR CADA CUENTA DE PASIVOS CORRIENTES
        dtm2.addRow(new Object[]{"PASIVO NO CORRIENTE", " ", total_PNC});
        for (int i = 0; i < listaPasivosNoCorrientes.size(); i++) {
            dtm2.addRow(new Object[]{listaPasivosNoCorrientes.get(i).getCuenta(), listaPasivosNoCorrientes.get(i).getMonto(), " "});
        }

        //CONSULTAR EL TOTAL DE CAPITAL
        Double total_CAP = 0.00;
        for (int i = 0; i < listaPatrimonio.size(); i++) {
            Double c = total_CAP + Double.parseDouble(listaPatrimonio.get(i).getMonto());
            total_CAP = Double.parseDouble(String.format("%.2f", c));
        }
        //CONSULTAR CADA CUENTA DE TOTAL DE CAPITAL
        dtm2.addRow(new Object[]{"CAPITAL CONTABLE", " ", total_CAP + reservaLegal + ResEjercicio});
        if (!listaPatrimonio.isEmpty()) {
            for (int i = 0; i < listaPatrimonio.size(); i++) {
                dtm2.addRow(new Object[]{listaPatrimonio.get(i).getCuenta(), listaPatrimonio.get(i).getMonto(), " "});
            }
        }
        dtm2.addRow(new Object[]{"RESERVA LEGAL", reservaLegal, " "});
        dtm2.addRow(new Object[]{"RESULTADO DEL EJERCICIO", ResEjercicio, " "});

        Double total_activos = (total_AC + total_ANC);
        Double total_pasivos = (total_PC + total_PNC + total_CAP + impuesto + reservaLegal + ResEjercicio);

        vista.totalActivos.setText(String.format("%.2f", total_activos));
        vista.totalPasivos.setText(String.format("%.2f", total_pasivos));

    }

    public Boolean GuardarBalance() {

        int rowActivo = vista.tabla_activos.getModel().getRowCount();
        int columnActivo = vista.tabla_activos.getModel().getColumnCount();

        int rowPasivo = vista.tabla_pasivos.getModel().getRowCount();
        int columnPasivo = vista.tabla_pasivos.getModel().getColumnCount();

        int n = dao.GetNBalance();

        String exito = "";

        dao.IniciarBalanceGeneral(n, anio);

        if (exito == "fracaso") {
            JOptionPane.showMessageDialog(vista, "hola");
        }

        Double totalActivoCorriente = 0.00, totalActivoNoCorriente = 0.00,
                totalPasivoCorriente = 0.00, totalPasivoNoCorriente = 0.00,
                totalPatrimonio = 0.00, totalActivo = Double.parseDouble(vista.totalActivos.getText()), totalPasivo = Double.parseDouble(vista.totalPasivos.getText());

        int tipo_cuenta = 0;

        for (int i = 0; i < rowActivo; i++) {
            for (int j = 0; j < columnActivo; j++) {
                if (vista.tabla_activos.getValueAt(i, j) == "ACTIVO CORRIENTE") {
                    totalActivoCorriente = Double.parseDouble(String.valueOf(vista.tabla_activos.getValueAt(i, j + 2)));
                    j = j + 2;
                    tipo_cuenta = 1;
                } else if (vista.tabla_activos.getValueAt(i, j) == "ACTIVO NO CORRIENTE") {
                    totalActivoNoCorriente = Double.parseDouble(String.valueOf(vista.tabla_activos.getValueAt(i, j + 2)));
                    j = j + 2;
                    tipo_cuenta = 2;
                } else {
                    if (vista.tabla_activos.getValueAt(i, j) != " ") {
                        if (tipo_cuenta > 0) {
                            exito = dao.IngresardetallesBalanceGeneral(String.valueOf(vista.tabla_activos.getValueAt(i, j)), Double.parseDouble(String.valueOf(vista.tabla_activos.getValueAt(i, j + 1))), n, tipo_cuenta);
                            j = j + 1;
                        }
                    }
                }
            }
        }

        if (exito == "fracaso") {
            JOptionPane.showMessageDialog(vista, "Error al guardar detalles del activo");
            return false;
        }

        for (int i = 0; i < rowPasivo; i++) {
            for (int j = 0; j < columnPasivo; j++) {
                if (vista.tabla_pasivos.getValueAt(i, j) == "PASIVO CORRIENTE") {
                    totalPasivoCorriente = Double.parseDouble(String.valueOf(vista.tabla_pasivos.getValueAt(i, j + 2)));
                    j = j + 2;
                    tipo_cuenta = 3;
                } else if (vista.tabla_pasivos.getValueAt(i, j) == "PASIVO NO CORRIENTE") {
                    totalPasivoNoCorriente = Double.parseDouble(String.valueOf(vista.tabla_pasivos.getValueAt(i, j + 2)));
                    j = j + 2;
                    tipo_cuenta = 4;
                } else if (vista.tabla_pasivos.getValueAt(i, j) == "CAPITAL CONTABLE") {
                    totalPatrimonio = Double.parseDouble(String.valueOf(vista.tabla_pasivos.getValueAt(i, j + 2)));
                    j = j + 2;
                    tipo_cuenta = 5;
                } else {
                    if (vista.tabla_pasivos.getValueAt(i, j) != " ") {
                        if (tipo_cuenta > 2) {
                            exito = dao.IngresardetallesBalanceGeneral(String.valueOf(vista.tabla_pasivos.getValueAt(i, j)), Double.parseDouble(String.valueOf(vista.tabla_pasivos.getValueAt(i, j + 1))), n, tipo_cuenta);
                            j = j + 1;
                        }
                    }
                }
            }
        }

        if (exito == "fracaso") {
            JOptionPane.showMessageDialog(vista, "Error al guardar detalles del pasivo");
            return false;
        }

        dao.IngresarTotalesBalanceGeneral(totalActivoCorriente, totalActivoNoCorriente, totalPasivoCorriente, totalPasivoNoCorriente, totalPatrimonio, totalActivo, totalPasivo, n);

        JOptionPane.showMessageDialog(vista, "Balance guardado con éxito");

        return true;

    }

    public void iniciar() {
        vista.setLocationRelativeTo(vista);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.Btn_Guardar) {
            GuardarBalance();
        } else if (e.getSource() == vista.Btn_Imprimir) {
            vista.setVisible(false);
            jasper.Reporte(1);
        }
    }

    public Double getDato() {
        if (daoResultado.select_ventas() != null) {
            Double ivfinal = (Double) 200000.00;
            estado = (daoResultado.select_ventas_totales());
        String vn = estado.getVentas_Totales();

        estado = (daoResultado.select_costo_de_venta());
        String cv = estado.getCosto_Ventas();

        Double a = (Double.parseDouble(vn) - (Double.parseDouble(cv) - ivfinal));

        Double utilidadBruta = Double.parseDouble(String.format("%.2f", a));

        System.out.println(utilidadBruta);

        estado = daoResultado.select_gastos_admin();
        String ga = estado.getGastos_Admin();

        estado = daoResultado.select_gasto_venta();
        String gv = estado.getGastos_Ventas();

        Double b = utilidadBruta - (Double.parseDouble(ga) + Double.parseDouble(gv));

        Double utilidadOperacion = Double.parseDouble(String.format("%.2f", b));

        EstadoResultado e = new EstadoResultado();
        e = daoResultado.select_ingresos_finan();
        String inf = e.getIngresos_Finan();

        estado = daoResultado.select_gasto_finan();
        String gf = estado.getGastos_Finan();

        Double utilidadAntes;
        if (Double.parseDouble(inf) == Double.parseDouble(gf)) {
            utilidadAntes = utilidadOperacion;

        } else if (Double.parseDouble(inf) < Double.parseDouble(gf)) {
            utilidadAntes = utilidadOperacion - (Double.parseDouble(inf) - Double.parseDouble(gf));

        } else {
            utilidadAntes = utilidadOperacion + (Double.parseDouble(inf) - Double.parseDouble(gf));

        }
        if (daoResultado.select_ventas() != null) {
            Double ventas = Double.parseDouble(daoResultado.select_ventas());
        } else {
            Double ventas = 0.00;
            System.out.println(ventas);
        }

        return utilidadAntes;
        }else{
           Double ivfinal = 0.00;
           estado = (daoResultado.select_ventas_totales());
        String vn = estado.getVentas_Totales();

        estado = (daoResultado.select_costo_de_venta());
        String cv = estado.getCosto_Ventas();

        Double a = (Double.parseDouble(vn) - (Double.parseDouble(cv) - ivfinal));

        Double utilidadBruta = Double.parseDouble(String.format("%.2f", a));

        System.out.println(utilidadBruta);

        estado = daoResultado.select_gastos_admin();
        String ga = estado.getGastos_Admin();

        estado = daoResultado.select_gasto_venta();
        String gv = estado.getGastos_Ventas();

        Double b = utilidadBruta - (Double.parseDouble(ga) + Double.parseDouble(gv));

        Double utilidadOperacion = Double.parseDouble(String.format("%.2f", b));

        EstadoResultado e = new EstadoResultado();
        e = daoResultado.select_ingresos_finan();
        String inf = e.getIngresos_Finan();

        estado = daoResultado.select_gasto_finan();
        String gf = estado.getGastos_Finan();

        Double utilidadAntes;
        if (Double.parseDouble(inf) == Double.parseDouble(gf)) {
            utilidadAntes = utilidadOperacion;

        } else if (Double.parseDouble(inf) < Double.parseDouble(gf)) {
            utilidadAntes = utilidadOperacion - (Double.parseDouble(inf) - Double.parseDouble(gf));

        } else {
            utilidadAntes = utilidadOperacion + (Double.parseDouble(inf) - Double.parseDouble(gf));

        }
        if (daoResultado.select_ventas() != null) {
            Double ventas = Double.parseDouble(daoResultado.select_ventas());
        } else {
            Double ventas = 0.00;
            System.out.println(ventas);
        }

        return utilidadAntes;
        }
        
    }

    private Double getReservaLegal() {
        Double utilidadAntes = getDato();

        Double c = (Double) ((utilidadAntes) * 0.07);
        Double reservaLegal = Double.parseDouble(String.format("%.2f", c));

        return reservaLegal;
    }

    private Double impuestoSobreRenta() {
        Double isr;

//        Double ventas = Double.parseDouble(daoResultado.select_ventas());
        if (daoResultado.select_ventas() != null) {
            Double ventas = Double.parseDouble(daoResultado.select_ventas());
            Double utilidadAntes = getDato();

            if (ventas < 1500000) {
                Double d = (Double) ((utilidadAntes - reservaLegal) * 0.25);
                isr = Double.parseDouble(String.format("%.2f", d));
            } else {
                Double d = (Double) ((utilidadAntes - reservaLegal) * 0.25);
                isr = Double.parseDouble(String.format("%.2f", d));
            }
            return isr;
        } else {
            Double ventas = 0.00;
            System.out.println(ventas);
            Double utilidadAntes = getDato();

            if (ventas < 1500000) {
                Double d = (Double) ((utilidadAntes - reservaLegal) * 0.25);
                isr = Double.parseDouble(String.format("%.2f", d));
            } else {
                Double d = (Double) ((utilidadAntes - reservaLegal) * 0.25);
                isr = Double.parseDouble(String.format("%.2f", d));
            }
            return isr;
        }

    }

    private Double getResultadoEjercicio() {
        Double utilidadAntes = getDato();
        Double isr = impuestoSobreRenta();
        Double reserva = getReservaLegal();

        Double ue = utilidadAntes - isr - reserva;
        return ue;
    }

}
