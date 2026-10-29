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

    //creado por mi
    public JFrame frame;
    public DefaultListModel<Vehiculo> dlmVehiculo;

    public Ventana() {
        frame = new JFrame("VehiculosMVC");
        frame.setContentPane(panel1);
        frame.setSize(1080, 720);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        initComponents();
    }

    public void initComponents() {
        dlmVehiculo= new DefaultListModel<Vehiculo>();
        list1.setModel(dlmVehiculo);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
