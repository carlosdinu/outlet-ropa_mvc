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
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OutletControlador implements ActionListener, ListSelectionListener, WindowListener, KeyListener {
    private Ventana vista;
    private OutletModelo modelo;
    private File ultimaRutaExportada;

    /*
        OutletControlador, este metodo se encarga de cargar los datos, una vez cargados, se agrega el actionListener,
        se agrega un KeyListener a buscarTxt un SelectionListener a la lista y un windowListener.
     */
    public OutletControlador(Ventana vista, OutletModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        try {
            cargarDatosConfiguracion();
        } catch (IOException e) {
            System.out.println("No se encontro el archivo " + e.getMessage());
        }
        addActionListener(this);
        vista.buscarTxt.addKeyListener(this);
        addListSelectionListener(this);
        addWindowListener(this);
    }

    /*
        hayCamposVacios, este metodo se encarga de verificar si existe algun campo vacio
     */
    private boolean hayCamposVacios() {
        return vista.codigoTxt.getText().isEmpty() ||
                vista.marcaTxt.getText().isEmpty() ||
                vista.nombreTxt.getText().isEmpty() ||
                vista.precioTxt.getText().isEmpty() ||
                vista.descuentoTxt.getText().isEmpty() ||
                vista.campoVariableTxt.getText().isEmpty() ||
                vista.fechaEntradaDPicker.getText().isEmpty();
    }

    /*
        limpiarCampos, este metodo se encarga de eliminar el contenido de un campo
     */
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

    /*
        refrescar, se encarga de actualizar los datos
     */
    public void refrescar() {
        vista.dlmPrenda.clear();
        for (Prenda unaPrenda : modelo.buscarPrendas(vista.buscarTxt.getText())) {
            vista.dlmPrenda.addElement(unaPrenda);
        }
    }

    /*
        addActionListener, agrega los listener a los buttons, ya sean normales o radioButtons
     */
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

    /*
        addListSelectionListener, agrega un listener a la lista
     */
    private void addListSelectionListener(ListSelectionListener listener) {
        vista.list1.addListSelectionListener(listener);
    }

    /*
        addWindowListener, agrega un listener a la ventana de la app
     */
    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }

    /*
        cargarDatosConfiguracion, se encarga de cargar la ultima ruta exportada
     */
    private void cargarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        try (FileReader lector = new FileReader("outlet.conf")) {
            configuracion.load(lector);
        }
        ultimaRutaExportada = new File(configuracion.getProperty("ultimaRutaExportada"));
    }

    /*
        actualizarDatosConfiguracion, se encarga de actualizar la ultima ruta exportada en outlet.conf
     */
    private void actualizarDatosConfiguracion(File ultimaRutaExportada) {
        this.ultimaRutaExportada = ultimaRutaExportada;
    }

    /*
        guardarDatosConfiguracion, se encarga de guardar la ultimaRutaExportada, unicamente si NO es NULL
     */
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

    /*
        darDeAlta, metodo encargado de dar de alta un tipo de Prenda, se encarga de recoger los text de los campos
        y dependiendo del tipo de prenda, utiliza el get especifico de la prenda para finalizar el proceso de dado de alta,
        si no se rellena un campo, devuelve error y false para que el programa no añada nada incompleto.
     */
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

    /*
        actionPerformed, se encarga de dar comandos a la App, segun el boton pulsado, se dispone de 9 botones;
        Nuevo, Modificar, Eliminar, Importar, Exportar, Salir, Camiseta, Pantalon y Zapatilla

     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            // Nuevo se encarga de verificar si hay campos vacios, si los hay devuelve error, si no continua verificando
            // si existe el codigo, del mismo modo, si existe no deja continuar y devuelve error, por ultimo, si no se cumplen estos errores
            // se da de alta la prenda, se limpia los campos de texto y se refresca la lista.
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
            // Modificar se encarga de permitir al usuario poder cambiar un valor o varios, para ello primero debe estar seleccionada la prenda
            // si no devolvera error, seguidamente que los campos esten todos rellenados, y por ultimo que el codigo sea el mismo, una vez modificada,
            // se elimina la prenda anterior y se da de alta la nueva modificada, por ultimo se refresca.
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
            // Eliminar se encarga de borrar el dato seleccionado, para ello ese dato debe existir y estar seleccionado,
            // una vez seleccionado, el usuario recibira una nueva ventana de seguridad, esperando su confirmacion.
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
            // Importar se encarga de extraer los datos de un archivo .xml para usarlos en el programa
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
            // Exportar se encarga de guardar los datos en un fichero .xml
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
            // Salir se usa como un boton para decorar el JMenuBar, teniendo la misma funcion que la X de la ventana, cerrarla
            case "Salir":
                windowClosing(null);
                break;
            // Camiseta es el boton encargado de decirle al programa el tipo de Prenda que estamos guardando.
            case "Camiseta":
                vista.campoVariableLbl.setText("Talla");
                vista.campoVariableTxt.setToolTipText("Introduce la talla (S, M, L, XL...)");
                break;
            // Pantalon es el boton encargado de decirle al programa el tipo de Prenda que estamos guardando.
            case "Pantalon":
                vista.campoVariableLbl.setText("Talla cintura");
                vista.campoVariableTxt.setToolTipText("Introduce la talla de cintura (38, 40...)");
                break;
            // Zapatilla es el boton encargado de decirle al programa el tipo de Prenda que estamos guardando.
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

    @Override
    public void keyReleased(KeyEvent e) {
        refrescar();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }
}