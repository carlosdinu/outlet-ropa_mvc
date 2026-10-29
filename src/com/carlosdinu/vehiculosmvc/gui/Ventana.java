package com.carlosdinu.vehiculosmvc.gui;

import com.carlosdinu.vehiculosmvc.base.Vehiculo;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;

public class Ventana {
    private JPanel panel1;
    public JRadioButton cocheRadioButton;
    public JRadioButton motoRadioButton;
    private JTextPane tipoVehiculoTextPane;
    public JTextField matriculaTxt;
    public JTextField marcaTxt;
    public JTextField modeloTxt;
    public JLabel plazasKmsLbl;
    public JTextField plazasKmsTxt;
    public JButton nuevoButton;
    public JButton exportarButton;
    public JButton importarButton;
    public JList list1;
    public DatePicker fechaMatriculacionDPicker;
    private JButton modificarButton;
    private JButton eliminarButton;

    //creado por mi
    public JFrame frame;
    public DefaultListModel<Vehiculo> dlmVehiculo;

    public JMenuItem itemImportar;
    public JMenuItem itemExportar;

    public Ventana() {
        frame = new JFrame("VehiculosMVC");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initComponents();
        crearMenu();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        initComponents();
    }

    public void crearMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");

        itemImportar = new JMenuItem("Importar");
        itemImportar.setActionCommand("Importar");

        itemExportar = new JMenuItem("Exportar");
        itemExportar.setActionCommand("Exportar");

        menuArchivo.add(itemImportar);
        menuArchivo.add(itemExportar);

        barra.add(menuArchivo);
        frame.setJMenuBar(barra);
    }

    public void initComponents() {
        dlmVehiculo= new DefaultListModel<Vehiculo>();
        list1.setModel(dlmVehiculo);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
