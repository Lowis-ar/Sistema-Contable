package Controladores;

import Vistas.VistaLibroDiario;
import Vistas.VistaMostrarLibroDiario;
import daos.DaoLibroDiario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import modelos.LibroDiario;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ControladorMostrarLibroDiario extends MouseAdapter implements ActionListener, MouseListener {

    VistaMostrarLibroDiario frmLibro;
    DaoLibroDiario daoLibro;
    LibroDiario libro;
    ArrayList<LibroDiario> listaPartidas;
    Map<Integer, ArrayList<LibroDiario>> partidasAgrupadas;
    int partidaActual;

    public ControladorMostrarLibroDiario(VistaMostrarLibroDiario frmLibro) {
        this.frmLibro = frmLibro;
        this.frmLibro.tbDatos.addMouseListener(this);
        this.frmLibro.lbBuscar.addActionListener(this);
        this.frmLibro.btnAnterior.addActionListener(this);
        this.frmLibro.btnSiguiente.addActionListener(this);
//        this.frmLibro.btnModificarPartida.addActionListener(this);
        daoLibro = new DaoLibroDiario();
        listaPartidas = daoLibro.selectAllLibroDiario();
        partidaActual = 0;
        agruparPartidas();
        cargarTablaLibroDiario();
        actualizarSumas();

        frmLibro.tfBuscarPartida.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarPartida();
                }
            }
        });
    }

    private void agruparPartidas() {
        partidasAgrupadas = new HashMap<>();
        for (LibroDiario libroDiario : listaPartidas) {
            partidasAgrupadas
                    .computeIfAbsent(libroDiario.getNumeroPartida(), k -> new ArrayList<>())
                    .add(libroDiario);
        }
    }

    public void cargarTablaLibroDiario() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Partida");
        model.addColumn("Fecha");
        model.addColumn("Código");
        model.addColumn("Cuenta");
        model.addColumn("Debe");
        model.addColumn("Haber");
        model.addColumn("Concepto");

        if (partidaActual >= 0 && partidaActual < partidasAgrupadas.size()) {
            ArrayList<LibroDiario> transacciones = new ArrayList<>(partidasAgrupadas.values()).get(partidaActual);
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(transacciones.get(0).getFecha());
            String concepto = transacciones.get(0).getConcepto();
            int numeroPartida = transacciones.get(0).getNumeroPartida();

            for (LibroDiario libroDiario : transacciones) {
                Object[] row = new Object[7];
                row[0] = numeroPartida;
                row[1] = fecha;
                row[2] = libroDiario.getCodSubcuenta();
                row[3] = libroDiario.getNombreCuenta();

                if ("DEBE".equalsIgnoreCase(libroDiario.getTransaccion())) {
                    row[4] = libroDiario.getMonto();
                    row[5] = null;
                } else if ("HABER".equalsIgnoreCase(libroDiario.getTransaccion())) {
                    row[4] = null;
                    row[5] = libroDiario.getMonto();
                }
                row[6] = concepto;
                model.addRow(row);
            }
        }

        frmLibro.tbDatos.setModel(model);
    }

    public void actualizarSumas() {
        double sumaDebe = 0;
        double sumaHaber = 0;
        DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Object debeObj = modeloTabla.getValueAt(i, 4);
            Object haberObj = modeloTabla.getValueAt(i, 5);
            if (debeObj != null && !debeObj.toString().isEmpty()) {
                sumaDebe += Double.parseDouble(debeObj.toString());
            }
            if (haberObj != null && !haberObj.toString().isEmpty()) {
                sumaHaber += Double.parseDouble(haberObj.toString());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        frmLibro.tfSumaDebe.setText(numberFormat.format(sumaDebe));
        frmLibro.tfSumaHaber.setText(numberFormat.format(sumaHaber));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frmLibro.lbBuscar) {
            buscarPartida();
        } else if (e.getSource() == frmLibro.btnAnterior) {
            mostrarPartidaAnterior();
        } else if (e.getSource() == frmLibro.btnSiguiente) {
            mostrarPartidaSiguiente();
//        } else if (e.getSource() == frmLibro.btnModificarPartida) {
//            VistaLibroDiario frm = new VistaLibroDiario(new JFrame(), true, "Modificar Partida");
//            ControladorLibroDiario controladorLibroDiario = new ControladorLibroDiario(frm);
//            frm.setVisible(true);
        }
    }

    private void buscarPartida() {
        String partidaBuscada = frmLibro.tfBuscarPartida.getText();
        if (!partidaBuscada.isEmpty()) {
            try {
                int numeroPartida = Integer.parseInt(partidaBuscada);
                for (int i = 0; i < listaPartidas.size(); i++) {
                    if (listaPartidas.get(i).getNumeroPartida() == numeroPartida) {
                        partidaActual = obtenerIndicePartidaAgrupada(numeroPartida);
                        cargarTablaLibroDiario();
                        actualizarSumas();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frmLibro, "Partida no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frmLibro, "Entrada inválida. Por favor ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frmLibro, "Por favor ingrese un número de partida para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int obtenerIndicePartidaAgrupada(int numeroPartida) {
        ArrayList<Integer> partidasAgrupadasList = new ArrayList<>(partidasAgrupadas.keySet());
        for (int i = 0; i < partidasAgrupadasList.size(); i++) {
            if (partidasAgrupadasList.get(i).equals(numeroPartida)) {
                return i;
            }
        }
        return -1;
    }

    private void mostrarPartidaAnterior() {
        if (partidaActual > 0) {
            partidaActual--;
            cargarTablaLibroDiario();
            actualizarSumas();
        }
    }

    private void mostrarPartidaSiguiente() {
        if (partidaActual < partidasAgrupadas.size() - 1) {
            partidaActual++;
            cargarTablaLibroDiario();
            actualizarSumas();
        }
    }

//    @Override
//    public void mouseClicked(java.awt.event.MouseEvent e) {
//        int row = frmLibro.tbDatos.rowAtPoint(e.getPoint());
//        int column = frmLibro.tbDatos.columnAtPoint(e.getPoint());
//        if (column == 0) {
//            frmLibro.btnModificarPartida.setEnabled(true);
//        } else {
//            frmLibro.btnModificarPartida.setEnabled(false);
//        }
//    }
}
