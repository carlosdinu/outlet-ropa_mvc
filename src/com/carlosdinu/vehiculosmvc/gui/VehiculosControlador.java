package com.carlosdinu.vehiculosmvc.gui;

import com.carlosdinu.vehiculosmvc.base.Coche;
import com.carlosdinu.vehiculosmvc.base.Moto;
import com.carlosdinu.vehiculosmvc.base.Vehiculo;
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

public class VehiculosControlador implements ActionListener, ListSelectionListener, WindowListener {
    private Ventana vista;
    private VehiculosModelo modelo;
    private File ultimaRutaExportada;

    public VehiculosControlador(Ventana vista, VehiculosModelo modelo) {
        this.vista=vista;
        this.modelo=modelo;
        try {
            cargarDatosConfiguracion();
        } catch (IOException e) {
            System.out.println("No es encontro el archivo "+e.getMessage());
        }
        addActionListener(this);
        addListSelectionListener(this);
        addWindowListener(this);
    }

    private boolean hayCamposVacios() {
        if (vista.plazasKmsTxt.getText().isEmpty() ||
                vista.marcaTxt.getText().isEmpty() ||
                vista.matriculaTxt.getText().isEmpty() ||
                vista.modeloTxt.getText().isEmpty() ||
                vista.fechaMatriculacionDPicker.getText().isEmpty()) {
            return true;
        }
        return false;
    }

    private void limpiarCampos() {
        vista.plazasKmsTxt.setText(null);
        vista.marcaTxt.setText(null);
        vista.matriculaTxt.setText(null);
        vista.modeloTxt.setText(null);
        vista.fechaMatriculacionDPicker.setText(null);
        vista.matriculaTxt.requestFocus();
    }

    public void refrescar() {
        vista.dlmVehiculo.clear();
        for (Vehiculo unVehiculo:modelo.obtenerVehiculos()) {
            vista.dlmVehiculo.addElement(unVehiculo);
        }
    }

    private void addActionListener(ActionListener listener) {
        vista.nuevoButton.addActionListener(listener);
        vista.importarButton.addActionListener(listener);
        vista.exportarButton.addActionListener(listener);
        vista.cocheRadioButton.addActionListener(listener);
        vista.motoRadioButton.addActionListener(listener);
    }

    private void addListSelectionListener(ListSelectionListener listener) {
        vista.list1.addListSelectionListener(listener);
    }

    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }

    private void cargarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        configuracion.load(new FileReader("vehiculos.conf"));
        ultimaRutaExportada=new File(configuracion.getProperty("ultimaRutaExportada"));
    }

    private void actualizarDatosConfiguracion(File ultimaRutaExportada) {
        this.ultimaRutaExportada=ultimaRutaExportada;
    }

    private void guardarDatosConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        configuracion.setProperty("ultimaRutaExportada",ultimaRutaExportada.getAbsolutePath());
        configuracion.store(new PrintWriter("vehiculos.conf"),"Datos configuracion vehiculos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Nuevo":
                if (hayCamposVacios()) {
                    Util.mensajeError("Los siguientes campos no pueden " +
                            "estar vacios: \nMatricula\nMarca\nModelo" +
                            "\nFecha Matriculacion\n"+vista.plazasKmsLbl.getText());
                    break;
                }
                if (modelo.existeMatricula(vista.matriculaTxt.getText())) {
                    Util.mensajeError("Ya existe un vehiculo con esa matricula");
                    break;
                }
                if (vista.cocheRadioButton.isSelected()) {
                    modelo.altaCoche(vista.matriculaTxt.getText(),vista.marcaTxt.getText(),
                            vista.modeloTxt.getText(),vista.fechaMatriculacionDPicker.getDate(),
                            Integer.parseInt(vista.plazasKmsTxt.getText()));
                } else {
                    modelo.altaMoto(vista.matriculaTxt.getText(),vista.marcaTxt.getText(),
                            vista.modeloTxt.getText(),vista.fechaMatriculacionDPicker.getDate(),
                            Double.parseDouble(vista.plazasKmsTxt.getText()));
                }
                limpiarCampos();
                refrescar();
                break;
            case "Importar":
                JFileChooser selectorFichero = Util.crearSelectorFichero(ultimaRutaExportada,
                        "Archivos XML","xml");
                int opt=selectorFichero.showOpenDialog(null);
                if (opt==JFileChooser.APPROVE_OPTION) {
                    try {
                        modelo.importarXML(selectorFichero.getSelectedFile());
                    } catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SAXException ex) {
                        ex.printStackTrace();
                    }
                    refrescar();
                }
                break;
            case "Exportar":
                JFileChooser selectorFichero2 = Util.crearSelectorFichero(ultimaRutaExportada,
                        "Archivos XML","xml");
                int opt2=selectorFichero2.showSaveDialog(null);
                if (opt2==JFileChooser.APPROVE_OPTION) {
                    try {
                        modelo.exportarXML(selectorFichero2.getSelectedFile());
                        actualizarDatosConfiguracion(selectorFichero2.getSelectedFile());
                    } catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                    } catch (TransformerException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "Moto":
                vista.plazasKmsLbl.setText("Kilometros");
                vista.plazasKmsTxt.setToolTipText("Introduce la cantidad de kilometros");
                break;
            case "Coche":
                vista.plazasKmsLbl.setText("Nº de plazas");
                vista.plazasKmsTxt.setToolTipText("Introduce el número de plazas");
                break;
        }

    }


    @Override
    public void windowClosing(WindowEvent e) {
        int resp= Util.mensajeConfirmacion("Deseas cerrar la ventana","Salir");
        if (resp==JOptionPane.OK_OPTION) {
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
        if (e.getValueIsAdjusting()) {
            Vehiculo vehiculoSeleccionado = (Vehiculo) vista.list1.getSelectedValue();
            vista.matriculaTxt.setText(vehiculoSeleccionado.getMatricula());
            vista.marcaTxt.setText(vehiculoSeleccionado.getMarca());
            vista.modeloTxt.setText(vehiculoSeleccionado.getModelo());
            vista.fechaMatriculacionDPicker.setDate(vehiculoSeleccionado.getFechaMatriculacion());
            if (vehiculoSeleccionado instanceof Coche) {
                vista.cocheRadioButton.doClick();
                vista.plazasKmsTxt.setText(String.valueOf(((Coche) vehiculoSeleccionado).getNumPlazas()));
            } else {
                vista.motoRadioButton.doClick();
                vista.plazasKmsTxt.setText(String.valueOf(((Moto) vehiculoSeleccionado).getKms()));
            }
        }
    }

    //no los uso
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }



}
