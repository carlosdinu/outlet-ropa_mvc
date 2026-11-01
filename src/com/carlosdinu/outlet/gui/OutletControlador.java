package com.carlosdinu.outlet.gui;

import com.carlosdinu.outlet.base.Camiseta;
import com.carlosdinu.outlet.base.Pantalon;
import com.carlosdinu.outlet.base.Prenda;
import com.carlosdinu.outlet.base.Zapatilla;
import org.xml.sax.SAXException;
import util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.Properties;

public class OutletControlador implements ActionListener, ListSelectionListener, WindowListener {
    private Ventana vista;
    private OutletModelo modelo;
    private File ultimaRutaExportada;

    public OutletControlador(Ventana vista, OutletModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        try {
            cargarDatosConfiguracion();
        } catch (IOException e) {
            System.out.println("No se encontro el archivo " + e.getMessage());
        }
        addActionListener(this);
        addListSelectionListener(this);
        addWindowListener(this);
    }

    private boolean hayCamposVacios() {
        return vista.codigoTxt.getText().isEmpty() ||
                vista.marcaTxt.getText().isEmpty() ||
                vista.nombreTxt.getText().isEmpty() ||
                vista.precioTxt.getText().isEmpty() ||
                vista.descuentoTxt.getText().isEmpty() ||
                vista.campoVariableTxt.getText().isEmpty() ||
                vista.fechaEntradaDPicker.getText().isEmpty();
    }

    private void limpiarCampos() {
        vista.codigoTxt.setText(null);
        vista.marcaTxt.setText(null);
        vista.nombreTxt.setText(null);
        vista.precioTxt.setText(null);
        vista.descuentoTxt.setText(null);
        vista.campoVariableTxt.setText(null);
        vista.descripcionTxtArea.setText(null);
        vista.colorCombo.setSelectedIndex(0);
        vista.fechaEntradaDPicker.setText(null);
        vista.codigoTxt.requestFocus();
    }

    public void refrescar() {
        vista.dlmPrenda.clear();
        for (Prenda unaPrenda : modelo.obtenerPrendas()) {
            vista.dlmPrenda.addElement(unaPrenda);
        }
    }

    private void addActionListener(ActionListener listener) {
        vista.nuevoButton.addActionListener(listener);
        vista.modificarButton.addActionListener(listener);
        vista.eliminarButton.addActionListener(listener);
        vista.camisetaRadioButton.addActionListener(listener);
        vista.pantalonRadioButton.addActionListener(listener);
        vista.zapatillaRadioButton.addActionListener(listener);
        vista.itemImportar.addActionListener(listener);
        vista.itemExportar.addActionListener(listener);
        vista.itemSalir.addActionListener(listener);
    }

    private void addListSelectionListener(ListSelectionListener listener) {
        vista.list1.addListSelectionListener(listener);
    }

    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }

    private void cargarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        try (FileReader lector = new FileReader("outlet.conf")) {
            configuracion.load(lector);
        }
        ultimaRutaExportada = new File(configuracion.getProperty("ultimaRutaExportada"));
    }

    private void actualizarDatosConfiguracion(File ultimaRutaExportada) {
        this.ultimaRutaExportada = ultimaRutaExportada;
    }

    private void guardarDatosConfiguracion() throws IOException {
        if (ultimaRutaExportada == null) {
            return;
        }
        Properties configuracion = new Properties();
        configuracion.setProperty("ultimaRutaExportada", ultimaRutaExportada.getAbsolutePath());
        try (PrintWriter escritor = new PrintWriter("outlet.conf")) {
            configuracion.store(escritor, "Datos configuracion outlet");
        }
    }

    private boolean darDeAlta() {
        try {
            String codigo = vista.codigoTxt.getText();
            String marca = vista.marcaTxt.getText();
            String nombre = vista.nombreTxt.getText();
            double precio = Double.parseDouble(vista.precioTxt.getText());
            int descuento = Integer.parseInt(vista.descuentoTxt.getText());
            String color = (String) vista.colorCombo.getSelectedItem();
            String descripcion = vista.descripcionTxtArea.getText();

            if (vista.camisetaRadioButton.isSelected()) {
                modelo.altaCamiseta(codigo, marca, nombre, vista.fechaEntradaDPicker.getDate(),
                        precio, descuento, color, descripcion,
                        vista.campoVariableTxt.getText());
            } else if (vista.pantalonRadioButton.isSelected()) {
                modelo.altaPantalon(codigo, marca, nombre, vista.fechaEntradaDPicker.getDate(),
                        precio, descuento, color, descripcion,
                        Integer.parseInt(vista.campoVariableTxt.getText()));
            } else {
                modelo.altaZapatilla(codigo, marca, nombre, vista.fechaEntradaDPicker.getDate(),
                        precio, descuento, color, descripcion,
                        Double.parseDouble(vista.campoVariableTxt.getText()));
            }
            return true;
        } catch (NumberFormatException ex) {
            Util.mensajeError("Precio, descuento y " + vista.campoVariableLbl.getText()
                    + " deben ser numeros validos");
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Nuevo":
                if (hayCamposVacios()) {
                    Util.mensajeError("Rellena todos los campos (la descripcion es opcional)");
                    break;
                }
                if (modelo.existeCodigo(vista.codigoTxt.getText())) {
                    Util.mensajeError("Ya existe una prenda con ese codigo");
                    break;
                }
                if (darDeAlta()) {
                    limpiarCampos();
                    refrescar();
                }
                break;

            case "Modificar":
                Prenda aModificar = vista.list1.getSelectedValue();
                if (aModificar == null) {
                    Util.mensajeError("Selecciona primero una prenda de la lista");
                    break;
                }
                if (hayCamposVacios()) {
                    Util.mensajeError("Rellena todos los campos (la descripcion es opcional)");
                    break;
                }
                if (!aModificar.getCodigo().equals(vista.codigoTxt.getText())
                        && modelo.existeCodigo(vista.codigoTxt.getText())) {
                    Util.mensajeError("Ya existe una prenda con ese codigo");
                    break;
                }
                modelo.eliminarPrenda(aModificar);
                if (darDeAlta()) {
                    limpiarCampos();
                } else {
                    modelo.obtenerPrendas().add(aModificar);
                }
                refrescar();
                break;

            case "Eliminar":
                Prenda seleccionada = vista.list1.getSelectedValue();
                if (seleccionada == null) {
                    Util.mensajeError("Selecciona primero una prenda de la lista");
                    break;
                }
                int resp = Util.mensajeConfirmacion("¿Eliminar " + seleccionada.getCodigo() + "?", "Eliminar");
                if (resp == JOptionPane.YES_OPTION) {
                    modelo.eliminarPrenda(seleccionada);
                    limpiarCampos();
                    refrescar();
                }
                break;

            case "Importar":
                JFileChooser selectorFichero = Util.crearSelectorFichero(ultimaRutaExportada,
                        "Archivos XML", "xml");
                int opt = selectorFichero.showOpenDialog(null);
                if (opt == JFileChooser.APPROVE_OPTION) {
                    try {
                        modelo.importarXML(selectorFichero.getSelectedFile());
                    } catch (ParserConfigurationException | IOException | SAXException ex) {
                        Util.mensajeError("No se pudo importar el fichero");
                        ex.printStackTrace();
                    }
                    refrescar();
                }
                break;

            case "Exportar":
                JFileChooser selectorFichero2 = Util.crearSelectorFichero(ultimaRutaExportada,
                        "Archivos XML", "xml");
                int opt2 = selectorFichero2.showSaveDialog(null);
                if (opt2 == JFileChooser.APPROVE_OPTION) {
                    File fichero = selectorFichero2.getSelectedFile();
                    // si el usuario no pone .xml, se lo añadimos
                    if (!fichero.getName().toLowerCase().endsWith(".xml")) {
                        fichero = new File(fichero.getAbsolutePath() + ".xml");
                    }
                    try {
                        modelo.exportarXML(fichero);
                        actualizarDatosConfiguracion(fichero);
                    } catch (ParserConfigurationException | TransformerException ex) {
                        Util.mensajeError("No se pudo exportar el fichero");
                        ex.printStackTrace();
                    }
                }
                break;

            case "Salir":
                windowClosing(null);
                break;

            case "Camiseta":
                vista.campoVariableLbl.setText("Talla");
                vista.campoVariableTxt.setToolTipText("Introduce la talla (S, M, L, XL...)");
                break;
            case "Pantalon":
                vista.campoVariableLbl.setText("Talla cintura");
                vista.campoVariableTxt.setToolTipText("Introduce la talla de cintura (38, 40...)");
                break;
            case "Zapatilla":
                vista.campoVariableLbl.setText("Nº calzado");
                vista.campoVariableTxt.setToolTipText("Introduce el numero (42, 42.5...)");
                break;
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int resp = Util.mensajeConfirmacion("¿Deseas cerrar la ventana?", "Salir");
        if (resp == JOptionPane.YES_OPTION) {
            try {
                guardarDatosConfiguracion();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Prenda prendaSeleccionada = vista.list1.getSelectedValue();
        if (e.getValueIsAdjusting() || prendaSeleccionada == null) {
            return;
        }
        vista.codigoTxt.setText(prendaSeleccionada.getCodigo());
        vista.marcaTxt.setText(prendaSeleccionada.getMarca());
        vista.nombreTxt.setText(prendaSeleccionada.getNombre());
        vista.precioTxt.setText(String.valueOf(prendaSeleccionada.getPrecio()));
        vista.descuentoTxt.setText(String.valueOf(prendaSeleccionada.getDescuento()));
        vista.colorCombo.setSelectedItem(prendaSeleccionada.getColor());
        vista.descripcionTxtArea.setText(prendaSeleccionada.getDescripcion());
        vista.fechaEntradaDPicker.setDate(prendaSeleccionada.getFechaEntrada());

        if (prendaSeleccionada instanceof Camiseta) {
            vista.camisetaRadioButton.doClick();
            vista.campoVariableTxt.setText(((Camiseta) prendaSeleccionada).getTalla());
        } else if (prendaSeleccionada instanceof Pantalon) {
            vista.pantalonRadioButton.doClick();
            vista.campoVariableTxt.setText(String.valueOf(((Pantalon) prendaSeleccionada).getTallaCintura()));
        } else {
            vista.zapatillaRadioButton.doClick();
            vista.campoVariableTxt.setText(String.valueOf(((Zapatilla) prendaSeleccionada).getNumero()));
        }
    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }

    @Override
    public void windowClosed(WindowEvent e) { }
}