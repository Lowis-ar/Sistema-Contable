package Controladores;

import Vistas.VistaLibroDiario;
import Vistas.VistaMostrarLibroDiario;
import daos.DaoLibroDiario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelos.LibroDiario;
import modelos.SubCuentas;

public class ControladorLibroDiario extends MouseAdapter implements ActionListener,
        MouseListener {

    VistaLibroDiario frmLibro;
    DaoLibroDiario daoLibro;
    LibroDiario libro;
    private ArrayList<LibroDiario> listaLibroDiario = new ArrayList<>();
    private String conceptoGlobal = "";

    public ControladorLibroDiario(VistaLibroDiario vista) {
        this.frmLibro = vista;
        this.frmLibro.btnAgregar.addActionListener(this);
        this.frmLibro.btnModificar.addActionListener(this);
        this.frmLibro.btnEliminar.addActionListener(this);
        this.frmLibro.btnProcesarPartida.addActionListener(this);
        this.frmLibro.tbDatos.addMouseListener(this);
        daoLibro = new DaoLibroDiario();
        mostrarNumeroPartida();
        mostrarPartidaAnterior();
        cargarSubCuentas();
    }

    public ControladorLibroDiario(VistaLibroDiario frmLibro, LibroDiario libro) {
        this.frmLibro = frmLibro;
        this.frmLibro.btnAgregar.addActionListener(this);
        this.frmLibro.btnModificar.addActionListener(this);
        this.frmLibro.btnEliminar.addActionListener(this);
        this.frmLibro.btnProcesarPartida.addActionListener(this);
        this.libro = libro;
        daoLibro = new DaoLibroDiario();
        this.frmLibro.tfFecha.setDate(this.libro.getFecha());
        this.frmLibro.tfCodigo.setText(String.valueOf(this.libro.getCodSubcuenta()));
        this.frmLibro.tfCuenta.setText(this.libro.getNombreCuenta());
        this.frmLibro.tfMonto.setText(Double.toString(this.libro.getMonto()));
        this.frmLibro.cbTransaccion.setSelectedIndex(Integer.parseInt(this.libro.getTransaccion()));
        this.frmLibro.tfConcepto.setText(this.libro.getConcepto());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frmLibro.btnAgregar) {
            agregarDatosTabla();
        } else if (e.getSource() == frmLibro.btnModificar) {
            modificarFilaSeleccionada();
        } else if (e.getSource() == frmLibro.btnEliminar) {
            eliminarFilaSeleccionada();
        } else if (e.getSource() == frmLibro.btnProcesarPartida) {
            procesarPartida();
        }
    }

    private void cargarSubCuentas() {
        Runnable buscarSubCuenta = () -> {
            String codigoIngresado = frmLibro.tfCodigo.getText().trim();
            if (codigoIngresado.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frmLibro,
                        "El código de cuenta está vacío. Ingrese un código para realizar la búsqueda.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            SubCuentas subCuentaEncontrada = null;
            ArrayList<SubCuentas> listaSubCuentas = daoLibro.obtenerSubCuentas();
            for (SubCuentas subCuenta : listaSubCuentas) {
                if (subCuenta.getCod_subcuenta().equals(codigoIngresado)) {
                    subCuentaEncontrada = subCuenta;
                    break;
                }
            }
            if (subCuentaEncontrada != null) {
                frmLibro.tfCuenta.setText(subCuentaEncontrada.getNombre_sub());
            } else {
                JOptionPane.showMessageDialog(
                        frmLibro,
                        "El código de cuenta ingresado no existe en la base de datos.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );
                frmLibro.tfCuenta.setText("");
            }
        };
        //Para usar la tecla Enter
        frmLibro.tfCodigo.addActionListener(e -> buscarSubCuenta.run());
        //Para dar click en el icono de busqueda
        frmLibro.lbBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                buscarSubCuenta.run();
            }
        });
    }

    public void agregarDatosTabla() {
        try {
            if (frmLibro.tfCodigo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un código de cuenta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (frmLibro.tfCuenta.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe buscar la cuenta antes de continuar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tfmont = frmLibro.tfMonto.getText().trim();
            double mont = 0;
            if (tfmont.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo monto no puede estar vacío.");
                return;
            }
            try {
                mont = Double.parseDouble(tfmont);
                if (mont <= 0) {
                    JOptionPane.showMessageDialog(null, "El monto debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido para el monto.");
                return;
            }
            if (frmLibro.tfConcepto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo concepto no puede estar vacío.");
                return;
            }
            Date fechaSeleccionada = frmLibro.tfFecha.getDate();
            int codigoSubcuenta = Integer.parseInt(frmLibro.tfCodigo.getText());
            String nombreCuenta = frmLibro.tfCuenta.getText();
            String transaccion = frmLibro.cbTransaccion.getSelectedItem().toString();
            BigDecimal montoBigDecimal = new BigDecimal(mont).setScale(2, RoundingMode.DOWN);
            double montoTruncado = montoBigDecimal.doubleValue();
            double montoConIVA = montoTruncado;
            if (frmLibro.rbAgregarIVA.isSelected()) {
                double iva = montoTruncado * 0.13;
                montoConIVA = iva;
            } else if (frmLibro.rbExtraerIVA.isSelected()) {
                double ivaExtraido = (montoTruncado / 1.13) * 0.13;
                montoConIVA = montoTruncado - ivaExtraido;
            }
            BigDecimal montoConIVABigDecimal = new BigDecimal(montoConIVA).setScale(2, RoundingMode.HALF_UP);
            double montoConIVATruncado = montoConIVABigDecimal.doubleValue();
            if (!frmLibro.tfConcepto.getText().isEmpty()) {
                conceptoGlobal = frmLibro.tfConcepto.getText();
                frmLibro.tfConcepto.setEnabled(false);
            }
            String concepto = conceptoGlobal;
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formatoFecha.format(fechaSeleccionada);
            Object[] fila = new Object[7];
            fila[0] = fechaFormateada;
            fila[1] = codigoSubcuenta;
            fila[2] = nombreCuenta;
            fila[5] = concepto;
            double montoDebe = 0;
            double montoHaber = 0;

            if ("Debe".equals(transaccion)) {
                montoDebe = montoConIVATruncado;
                fila[3] = String.format("%.2f", montoConIVATruncado);
                fila[4] = "";
            } else if ("Haber".equals(transaccion)) {
                montoHaber = montoConIVATruncado;
                fila[3] = "";
                fila[4] = String.format("%.2f", montoConIVATruncado);
            }
            DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
            modeloTabla.addRow(fila);
            listaLibroDiario.add(new LibroDiario(
                    Integer.parseInt(frmLibro.tfNumeroPartida.getText()),
                    fechaSeleccionada,
                    codigoSubcuenta,
                    nombreCuenta,
                    montoConIVATruncado,
                    transaccion,
                    concepto
            ));
            actualizarSumas();
            ButtonGroup group = new ButtonGroup();
            group.add(frmLibro.rbAgregarIVA);
            group.add(frmLibro.rbExtraerIVA);
            group.clearSelection();
            frmLibro.tfCodigo.setText("");
            frmLibro.tfMonto.setText("");
            frmLibro.cbTransaccion.setSelectedIndex(0);
            frmLibro.tfCuenta.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error al capturar o agregar datos: " + ex.getMessage());
        }
    }

    private void modificarFilaSeleccionada() {
        try {
            if (frmLibro.tfCodigo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un código de cuenta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tfmont = frmLibro.tfMonto.getText().trim();
            double mont = 0;
            if (tfmont.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo monto no puede estar vacío.");
                return;
            }
            try {
                mont = Double.parseDouble(tfmont);
                if (mont <= 0) {
                    JOptionPane.showMessageDialog(null, "El monto debe ser un número positivo.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido para el monto.");
                return;
            }
            int filaSeleccionada = (int) frmLibro.tbDatos.getClientProperty("filaSeleccionada");
            Date fechaSeleccionada = frmLibro.tfFecha.getDate();
            int codigoSubcuenta = Integer.parseInt(frmLibro.tfCodigo.getText());
            String nombreCuenta = frmLibro.tfCuenta.getText();
            double monto = Double.parseDouble(frmLibro.tfMonto.getText());
            String transaccion = frmLibro.cbTransaccion.getSelectedItem().toString();
            String concepto = frmLibro.tfConcepto.getText();
            BigDecimal montoBigDecimal = new BigDecimal(mont).setScale(2, RoundingMode.HALF_UP);
            double montoTruncado = montoBigDecimal.doubleValue();
            double montoConIVA = montoTruncado;
            if (frmLibro.rbAgregarIVA.isSelected()) {
                double iva = montoTruncado * 0.13;
                montoConIVA = iva + montoTruncado;
            } else if (frmLibro.rbExtraerIVA.isSelected()) {
                double ivaExtraido = (montoTruncado / 1.13) * 0.13;
                montoConIVA = montoTruncado - ivaExtraido;
            }
            BigDecimal montoConIVABigDecimal = new BigDecimal(montoConIVA).setScale(2, RoundingMode.HALF_UP);
            double montoConIVATruncado = montoConIVABigDecimal.doubleValue();
            DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formatoFecha.format(fechaSeleccionada);
            modeloTabla.setValueAt(fechaFormateada, filaSeleccionada, 0);
            modeloTabla.setValueAt(codigoSubcuenta, filaSeleccionada, 1);
            modeloTabla.setValueAt(nombreCuenta, filaSeleccionada, 2);
            modeloTabla.setValueAt("Debe".equals(transaccion) ? montoConIVATruncado : "", filaSeleccionada, 3);
            modeloTabla.setValueAt("Haber".equals(transaccion) ? montoConIVATruncado : "", filaSeleccionada, 4);
            modeloTabla.setValueAt(concepto, filaSeleccionada, 5);
            // Actualizar datos en el ArrayList
            LibroDiario libro = listaLibroDiario.get(filaSeleccionada);
            libro.setFecha(fechaSeleccionada);
            libro.setCodSubcuenta(codigoSubcuenta);
            libro.setNombreCuenta(nombreCuenta);
            libro.setMonto(montoConIVATruncado);
            libro.setTransaccion(transaccion);
            libro.setConcepto(concepto);
            JOptionPane.showMessageDialog(null, "Fila modificada correctamente.");
            actualizarSumas();
            ButtonGroup group = new ButtonGroup();
            group.add(frmLibro.rbAgregarIVA);
            group.add(frmLibro.rbExtraerIVA);
            group.clearSelection();
            frmLibro.tfCodigo.setText("");
            frmLibro.tfCuenta.setText("");
            frmLibro.tfMonto.setText("");
            frmLibro.cbTransaccion.setSelectedIndex(0);
            frmLibro.btnModificar.setEnabled(false);
            frmLibro.btnEliminar.setEnabled(false);
            frmLibro.btnAgregar.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al modificar la fila: " + ex.getMessage());
        }
    }

    private void eliminarFilaSeleccionada() {
        try {
            int filaSeleccionada = frmLibro.tbDatos.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        frmLibro,
                        "¿Estás seguro de que deseas eliminar esta fila?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmacion == JOptionPane.YES_OPTION) {
                    DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
                    modeloTabla.removeRow(filaSeleccionada);
                    listaLibroDiario.remove(filaSeleccionada);
                    JOptionPane.showMessageDialog(frmLibro, "Fila eliminada correctamente.");
                    actualizarSumas();
                    ButtonGroup group = new ButtonGroup();
                    group.add(frmLibro.rbAgregarIVA);
                    group.add(frmLibro.rbExtraerIVA);
                    group.clearSelection();
                    frmLibro.tfCodigo.setText("");
                    frmLibro.tfCuenta.setText("");
                    frmLibro.tfMonto.setText("");
                    frmLibro.cbTransaccion.setSelectedIndex(0);
                    frmLibro.btnEliminar.setEnabled(false);
                    frmLibro.btnModificar.setEnabled(false);
                    frmLibro.btnAgregar.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(frmLibro, "Por favor, selecciona una fila para eliminar.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frmLibro, "Error al eliminar la fila: " + ex.getMessage());
        }
    }

    public void procesarPartida() {
        try {
            if (frmLibro.tbDatos.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No se puede procesar la partida. No hay registros en la tabla.");
                return;
            }
            // Para verificar si el debe y el haber son iguales
            double totalDebe = 0.0;
            double totalHaber = 0.0;
            for (int i = 0; i < frmLibro.tbDatos.getRowCount(); i++) {
                String debe = frmLibro.tbDatos.getValueAt(i, 3).toString();
                String haber = frmLibro.tbDatos.getValueAt(i, 4).toString();
                if (!debe.isEmpty()) {
                    totalDebe += Double.parseDouble(debe);
                }
                if (!haber.isEmpty()) {
                    totalHaber += Double.parseDouble(haber);
                }
            }
            if (totalDebe != totalHaber) {
                JOptionPane.showMessageDialog(null, "El total del debe y el haber no son iguales. No se puede procesar.");
                return;
            }
            // Limpia la lista (datos Anteriores en la tabla) para evitar duplicados
            listaLibroDiario.clear();
            //Recorro el ArrayList Nuevamente para obtener los datos de las filas actualizadas 
            DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                //Capturando los datos actuales de la tabla filas modificadas
                Date fecha = formatoFecha.parse(modeloTabla.getValueAt(i, 0).toString());
                int codigoSubcuenta = Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
                String nombreCuenta = modeloTabla.getValueAt(i, 2).toString();
                double monto = modeloTabla.getValueAt(i, 3).toString().isEmpty()
                        ? Double.parseDouble(modeloTabla.getValueAt(i, 4).toString())
                        : Double.parseDouble(modeloTabla.getValueAt(i, 3).toString());
                String transaccion = modeloTabla.getValueAt(i, 3).toString().isEmpty() ? "Haber" : "Debe";
                String concepto = modeloTabla.getValueAt(i, 5).toString();
                // Agregar al ArrayList
                listaLibroDiario.add(new LibroDiario(
                        Integer.parseInt(frmLibro.tfNumeroPartida.getText()),
                        fecha,
                        codigoSubcuenta,
                        nombreCuenta,
                        monto,
                        transaccion,
                        concepto
                ));
            }
            // Guardando los datos en la base de datos
            int numeroPartida = daoLibro.obtenerUltimaPartida();
            numeroPartida++;
            frmLibro.tfNumeroPartida.setText(String.valueOf(numeroPartida));
            for (LibroDiario libro : listaLibroDiario) {
                libro.setNumeroPartida(numeroPartida);
                String resultado = daoLibro.insertarLibroDiario(libro);
                if ("exito".equals(resultado)) {
                    System.out.println("Partida guardada correctamente: " + libro);
                } else {
                    System.out.println("Error al guardar la partida: " + libro);
                }
            }
            JOptionPane.showMessageDialog(null, "Partida procesada correctamente.");
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea agregar otra partida?", "Agregar Partida", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.NO_OPTION) {
                // Si la respuesta es "No", redirigir a VistaMostrarLibroDiario
                VistaMostrarLibroDiario vista = new VistaMostrarLibroDiario();
                ControladorMostrarLibroDiario ctrl = new ControladorMostrarLibroDiario(vista);
                vista.setVisible(true); // Método que muestra el contenido de la vista
            }
            limpiarCampos();
            frmLibro.tfPartidaAnterior.setText(String.valueOf(numeroPartida));
            frmLibro.tfNumeroPartida.setText(String.valueOf(numeroPartida + 1));
            frmLibro.tfConcepto.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al procesar las partidas: " + e.getMessage());
        }
    }

    public void limpiarCampos() {
        frmLibro.tfCodigo.setText("");
        frmLibro.tfCuenta.setText("");
        frmLibro.tfMonto.setText("");
        frmLibro.cbTransaccion.setSelectedIndex(0);
        frmLibro.tfConcepto.setText("");
        frmLibro.tfSumaDebe.setText("");
        frmLibro.tfSumaHaber.setText("");
        DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
        modeloTabla.setRowCount(0);
    }

    public void actualizarSumas() {
        double sumaDebe = 0;
        double sumaHaber = 0;
        DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String debe = modeloTabla.getValueAt(i, 3).toString();
            String haber = modeloTabla.getValueAt(i, 4).toString();
            if (!debe.isEmpty()) {
                sumaDebe += Double.parseDouble(debe);
            }
            if (!haber.isEmpty()) {
                sumaHaber += Double.parseDouble(haber);
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        frmLibro.tfSumaDebe.setText(numberFormat.format(sumaDebe));
        frmLibro.tfSumaHaber.setText(numberFormat.format(sumaHaber));
    }

    public void mostrarNumeroPartida() {
        try {
            int ultimaPartida = daoLibro.obtenerUltimaPartida();
            int nuevoNumeroPartida = ultimaPartida + 1;
            frmLibro.tfNumeroPartida.setText(String.valueOf(nuevoNumeroPartida));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener el número de partida: " + ex.getMessage());
        }
    }

    public void mostrarPartidaAnterior() {
        try {
            int ultimaPartida = daoLibro.obtenerUltimaPartida();
            frmLibro.tfPartidaAnterior.setText(String.valueOf(ultimaPartida));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error al obtener el último número de partida: " + ex.getMessage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.frmLibro.tbDatos) {
            int fila = this.frmLibro.tbDatos.getSelectedRow();
            if (fila >= 0) {
                // Cargar datos de la fila seleccionada a los campos de la vista
                DefaultTableModel modeloTabla = (DefaultTableModel) this.frmLibro.tbDatos.getModel();
                String fecha = modeloTabla.getValueAt(fila, 0).toString();
                int codigoSubcuenta = Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString());
                String nombreCuenta = modeloTabla.getValueAt(fila, 2).toString();
                String debe = modeloTabla.getValueAt(fila, 3).toString();
                String haber = modeloTabla.getValueAt(fila, 4).toString();
                String concepto = modeloTabla.getValueAt(fila, 5).toString();
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date fechaSeleccionada = formatoFecha.parse(fecha);
                    this.frmLibro.tfFecha.setDate(fechaSeleccionada);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // Aquí obtengo el código de la cuenta para cargarlo en el JTextField al modificar
                String codigoSubcuentaStr = String.valueOf(codigoSubcuenta);
                this.frmLibro.tfCodigo.setText(codigoSubcuentaStr);
                this.frmLibro.tfCuenta.setText(nombreCuenta);
                this.frmLibro.tfMonto.setText(!debe.isEmpty() ? debe : haber);
                this.frmLibro.cbTransaccion.setSelectedItem(!debe.isEmpty() ? "Debe" : "Haber");
                this.frmLibro.tfConcepto.setText(concepto);
                this.frmLibro.btnModificar.setEnabled(true);
                this.frmLibro.btnEliminar.setEnabled(true);
                this.frmLibro.btnAgregar.setEnabled(false);
                this.frmLibro.tbDatos.putClientProperty("filaSeleccionada", fila);
            }
        }
    }
}
