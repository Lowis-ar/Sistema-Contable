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
import java.text.ParseException;
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

    // Locale a usar para parseo/formatos (puedes cambiar si necesitas otra)
    private final Locale LOCALE_INPUT = Locale.getDefault();
    private final NumberFormat nfInput = NumberFormat.getNumberInstance(LOCALE_INPUT);
    private final NumberFormat nfOutput = NumberFormat.getNumberInstance(Locale.US);

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

        // configurar formatos de salida
        nfOutput.setMinimumFractionDigits(2);
        nfOutput.setMaximumFractionDigits(2);
    }

    public ControladorLibroDiario(VistaLibroDiario frmLibro, LibroDiario libro) {
        this(frmLibro); // reutiliza constructor principal
        this.libro = libro;
        this.frmLibro.tfFecha.setDate(this.libro.getFecha());
        this.frmLibro.tfCodigo.setText(String.valueOf(this.libro.getCodSubcuenta()));
        this.frmLibro.tfCuenta.setText(this.libro.getNombreCuenta());
        // mostrar monto con dos decimales
        nfOutput.setMinimumFractionDigits(2);
        nfOutput.setMaximumFractionDigits(2);
        this.frmLibro.tfMonto.setText(nfOutput.format(this.libro.getMonto()));
        try {
            this.frmLibro.cbTransaccion.setSelectedIndex(Integer.parseInt(this.libro.getTransaccion()));
        } catch (Exception ex) {
            // si la transaccion no es indice, dejar la selección por defecto
        }
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

    private BigDecimal parseMontoAdecimal(String texto) throws ParseException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ParseException("Campo vacío", 0);
        }
        Number n = nfInput.parse(texto.trim());
        // convertimos aseg. con String para no perder precision
        return new BigDecimal(n.toString());
    }

    private BigDecimal parseAmountObjectToBigDecimal(Object val) throws ParseException {
        if (val == null) {
            return BigDecimal.ZERO;
        }
        if (val instanceof Number) {
            return new BigDecimal(val.toString());
        } else {
            // tratar string con coma/punto según locale
            return parseMontoAdecimal(val.toString());
        }
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
            if (tfmont.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo monto no puede estar vacío.");
                return;
            }

            BigDecimal montoBD;
            try {
                montoBD = parseMontoAdecimal(tfmont).setScale(2, RoundingMode.HALF_UP);
                if (montoBD.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(null, "El monto debe ser un número positivo.");
                    return;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido para el monto.");
                return;
            }

            if (frmLibro.tfConcepto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo concepto no puede estar vacío.");
                return;
            }
            Date fechaSeleccionada = frmLibro.tfFecha.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fecha.");
                return;
            }

            // código de subcuenta: intentar int, si falla dejar como 0 (o manejar según tu modelo)
            int codigoSubcuenta = 0;
            try {
                codigoSubcuenta = Integer.parseInt(frmLibro.tfCodigo.getText().trim());
            } catch (NumberFormatException ex) {
                // si tu modelo acepta códigos no numéricos, necesitarías modificar LibroDiario.
                // Aquí notificamos y retornamos
                JOptionPane.showMessageDialog(null, "El código de subcuenta debe ser numérico.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nombreCuenta = frmLibro.tfCuenta.getText();
            String transaccion = frmLibro.cbTransaccion.getSelectedItem().toString();

            // Calculo de IVA correctamente:
            BigDecimal montoConIVA = montoBD;
            if (frmLibro.rbAgregarIVA.isSelected()) {
                // agregar IVA: monto + (monto * 0.13)
                BigDecimal iva = montoBD.multiply(new BigDecimal("0.13"));
                montoConIVA = montoBD.add(iva);
            } else if (frmLibro.rbExtraerIVA.isSelected()) {
                // extraer IVA: obtener base = monto / 1.13
                BigDecimal base = montoBD.divide(new BigDecimal("1.13"), 10);
                montoConIVA = base;
            }
            montoConIVA = montoConIVA.setScale(2, RoundingMode.HALF_UP);

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
            BigDecimal montoDebe = BigDecimal.ZERO;
            BigDecimal montoHaber = BigDecimal.ZERO;

            if ("Debe".equalsIgnoreCase(transaccion)) {
                montoDebe = montoConIVA;
                fila[3] = montoDebe.doubleValue(); // almacenar como Double para facilitar sumas
                fila[4] = null;
            } else if ("Haber".equalsIgnoreCase(transaccion)) {
                montoHaber = montoConIVA;
                fila[3] = null;
                fila[4] = montoHaber.doubleValue();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione tipo de transacción (Debe/Haber).");
                return;
            }
            DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
            modeloTabla.addRow(fila);
            listaLibroDiario.add(new LibroDiario(
                    Integer.parseInt(frmLibro.tfNumeroPartida.getText()),
                    fechaSeleccionada,
                    codigoSubcuenta,
                    nombreCuenta,
                    montoConIVA.doubleValue(),
                    transaccion,
                    concepto
            ));
            actualizarSumas();
            // limpiar selección de radio
            ButtonGroup group = new ButtonGroup();
            group.add(frmLibro.rbAgregarIVA);
            group.add(frmLibro.rbExtraerIVA);
            group.clearSelection();
            // limpiar campos
            frmLibro.tfCodigo.setText("");
            frmLibro.tfMonto.setText("");
            frmLibro.cbTransaccion.setSelectedIndex(0);
            frmLibro.tfCuenta.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error al capturar o agregar datos: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Ocurrió un error al agregar la fila: " + ex.getMessage());
        }
    }

    private void modificarFilaSeleccionada() {
        try {
            if (frmLibro.tfCodigo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un código de cuenta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tfmont = frmLibro.tfMonto.getText().trim();
            if (tfmont.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El campo monto no puede estar vacío.");
                return;
            }

            BigDecimal montoBD;
            try {
                montoBD = parseMontoAdecimal(tfmont).setScale(2, RoundingMode.HALF_UP);
                if (montoBD.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(null, "El monto debe ser un número positivo.");
                    return;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido para el monto.");
                return;
            }

            Object filaProp = frmLibro.tbDatos.getClientProperty("filaSeleccionada");
            int filaSeleccionada = -1;
            if (filaProp instanceof Integer) {
                filaSeleccionada = (Integer) filaProp;
            } else {
                filaSeleccionada = frmLibro.tbDatos.getSelectedRow();
            }
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(null, "Seleccione primero la fila a modificar.");
                return;
            }

            Date fechaSeleccionada = frmLibro.tfFecha.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fecha.");
                return;
            }

            int codigoSubcuenta;
            try {
                codigoSubcuenta = Integer.parseInt(frmLibro.tfCodigo.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El código de subcuenta debe ser numérico.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String nombreCuenta = frmLibro.tfCuenta.getText();
            String transaccion = frmLibro.cbTransaccion.getSelectedItem().toString();
            String concepto = frmLibro.tfConcepto.getText();

            BigDecimal montoConIVA = montoBD;
            if (frmLibro.rbAgregarIVA.isSelected()) {
                BigDecimal iva = montoBD.multiply(new BigDecimal("0.13"));
                montoConIVA = montoBD.add(iva);
            } else if (frmLibro.rbExtraerIVA.isSelected()) {
                BigDecimal base = montoBD.divide(new BigDecimal("1.13"), 10, RoundingMode.HALF_UP);
                montoConIVA = base;
            }
            montoConIVA = montoConIVA.setScale(2, RoundingMode.HALF_UP);

            DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formatoFecha.format(fechaSeleccionada);
            modeloTabla.setValueAt(fechaFormateada, filaSeleccionada, 0);
            modeloTabla.setValueAt(codigoSubcuenta, filaSeleccionada, 1);
            modeloTabla.setValueAt(nombreCuenta, filaSeleccionada, 2);
            if ("Debe".equalsIgnoreCase(transaccion)) {
                modeloTabla.setValueAt(montoConIVA.doubleValue(), filaSeleccionada, 3);
                modeloTabla.setValueAt(null, filaSeleccionada, 4);
            } else {
                modeloTabla.setValueAt(null, filaSeleccionada, 3);
                modeloTabla.setValueAt(montoConIVA.doubleValue(), filaSeleccionada, 4);
            }
            modeloTabla.setValueAt(concepto, filaSeleccionada, 5);

            // Actualizar datos en el ArrayList si existe
            if (filaSeleccionada < listaLibroDiario.size()) {
                LibroDiario libro = listaLibroDiario.get(filaSeleccionada);
                libro.setFecha(fechaSeleccionada);
                libro.setCodSubcuenta(codigoSubcuenta);
                libro.setNombreCuenta(nombreCuenta);
                libro.setMonto(montoConIVA.doubleValue());
                libro.setTransaccion(transaccion);
                libro.setConcepto(concepto);
            }

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
                    if (filaSeleccionada < listaLibroDiario.size()) {
                        listaLibroDiario.remove(filaSeleccionada);
                    }
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
            BigDecimal totalDebe = BigDecimal.ZERO;
            BigDecimal totalHaber = BigDecimal.ZERO;
            DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
            for (int i = 0; i < frmLibro.tbDatos.getRowCount(); i++) {
                Object debeObj = modeloTabla.getValueAt(i, 3);
                Object haberObj = modeloTabla.getValueAt(i, 4);
                if (debeObj != null && !debeObj.toString().trim().isEmpty()) {
                    BigDecimal d = parseAmountObjectToBigDecimal(debeObj);
                    totalDebe = totalDebe.add(d);
                }
                if (haberObj != null && !haberObj.toString().trim().isEmpty()) {
                    BigDecimal h = parseAmountObjectToBigDecimal(haberObj);
                    totalHaber = totalHaber.add(h);
                }
            }
            // comparación con tolerancia de 0.01
            if (totalDebe.subtract(totalHaber).abs().compareTo(new BigDecimal("0.01")) > 0) {
                JOptionPane.showMessageDialog(null, "El total del debe y el haber no son iguales. No se puede procesar.");
                return;
            }
            // Limpia la lista (datos Anteriores en la tabla) para evitar duplicados
            listaLibroDiario.clear();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                //Capturando los datos actuales de la tabla filas modificadas
                Date fecha = formatoFecha.parse(modeloTabla.getValueAt(i, 0).toString());
                int codigoSubcuenta = Integer.parseInt(modeloTabla.getValueAt(i, 1).toString());
                String nombreCuenta = modeloTabla.getValueAt(i, 2).toString();
                Object colDebe = modeloTabla.getValueAt(i, 3);
                Object colHaber = modeloTabla.getValueAt(i, 4);
                BigDecimal montoBD;
                String transaccion;
                if (colDebe != null && !colDebe.toString().trim().isEmpty()) {
                    montoBD = parseAmountObjectToBigDecimal(colDebe).setScale(2, RoundingMode.HALF_UP);
                    transaccion = "Debe";
                } else {
                    montoBD = parseAmountObjectToBigDecimal(colHaber).setScale(2, RoundingMode.HALF_UP);
                    transaccion = "Haber";
                }
                String concepto = modeloTabla.getValueAt(i, 5) != null ? modeloTabla.getValueAt(i, 5).toString() : "";

                // Agregar al ArrayList
                listaLibroDiario.add(new LibroDiario(
                        Integer.parseInt(frmLibro.tfNumeroPartida.getText()),
                        fecha,
                        codigoSubcuenta,
                        nombreCuenta,
                        montoBD.doubleValue(),
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
        listaLibroDiario.clear();
    }

    public void actualizarSumas() {
        BigDecimal sumaDebe = BigDecimal.ZERO;
        BigDecimal sumaHaber = BigDecimal.ZERO;
        DefaultTableModel modeloTabla = (DefaultTableModel) frmLibro.tbDatos.getModel();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Object debeObj = modeloTabla.getValueAt(i, 3);
            Object haberObj = modeloTabla.getValueAt(i, 4);
            try {
                if (debeObj != null && !debeObj.toString().trim().isEmpty()) {
                    sumaDebe = sumaDebe.add(parseAmountObjectToBigDecimal(debeObj));
                }
                if (haberObj != null && !haberObj.toString().trim().isEmpty()) {
                    sumaHaber = sumaHaber.add(parseAmountObjectToBigDecimal(haberObj));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        nfOutput.setMinimumFractionDigits(2);
        nfOutput.setMaximumFractionDigits(2);
        frmLibro.tfSumaDebe.setText(nfOutput.format(sumaDebe.doubleValue()));
        frmLibro.tfSumaHaber.setText(nfOutput.format(sumaHaber.doubleValue()));
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
                Object debeObj = modeloTabla.getValueAt(fila, 3);
                Object haberObj = modeloTabla.getValueAt(fila, 4);
                String concepto = modeloTabla.getValueAt(fila, 5) != null ? modeloTabla.getValueAt(fila, 5).toString() : "";
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
                try {
                    if (debeObj != null && !debeObj.toString().trim().isEmpty()) {
                        this.frmLibro.tfMonto.setText(nfOutput.format(parseAmountObjectToBigDecimal(debeObj).doubleValue()));
                        this.frmLibro.cbTransaccion.setSelectedItem("Debe");
                    } else {
                        this.frmLibro.tfMonto.setText(nfOutput.format(parseAmountObjectToBigDecimal(haberObj).doubleValue()));
                        this.frmLibro.cbTransaccion.setSelectedItem("Haber");
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    this.frmLibro.tfMonto.setText(debeObj != null ? debeObj.toString() : (haberObj != null ? haberObj.toString() : ""));
                }
                this.frmLibro.tfConcepto.setText(concepto);
                this.frmLibro.btnModificar.setEnabled(true);
                this.frmLibro.btnEliminar.setEnabled(true);
                this.frmLibro.btnAgregar.setEnabled(false);
                this.frmLibro.tbDatos.putClientProperty("filaSeleccionada", fila);
            }
        }
    }
}
