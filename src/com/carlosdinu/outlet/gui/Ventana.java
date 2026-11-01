package com.carlosdinu.outlet.gui;

import com.carlosdinu.outlet.base.Prenda;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;

public class Ventana {
    private JPanel panel1;
    private JPanel panelRadios;
    private JPanel panelDatos;
    private JPanel panelLista;
    private JPanel panelBuscar;
    private JPanel panelBotones;
    private JLabel tituloLbl;

    public JRadioButton camisetaRadioButton;
    public JRadioButton pantalonRadioButton;
    public JRadioButton zapatillaRadioButton;
    public JTextField codigoTxt;
    public JTextField marcaTxt;
    public JTextField nombreTxt;
    public JTextField precioTxt;
    public JTextField descuentoTxt;
    public JComboBox<String> colorCombo;
    public DatePicker fechaEntradaDPicker;
    public JLabel campoVariableLbl;
    public JTextField campoVariableTxt;
    public JTextArea descripcionTxtArea;
    public JTextField buscarTxt;
    public JList<Prenda> list1;
    public JButton nuevoButton;
    public JButton modificarButton;
    public JButton eliminarButton;

    public JFrame frame;
    public DefaultListModel<Prenda> dlmPrenda;
    public JMenuItem itemImportar;
    public JMenuItem itemExportar;
    public JMenuItem itemSalir;

    private static final Color FONDO = new Color(245, 240, 235);
    private static final Color PRIMARIO = new Color(40, 40, 40);
    private static final Color ACENTO = new Color(200, 70, 60);
    private static final Color BLANCO = Color.WHITE;
    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FUENTE_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public Ventana() {
        frame = new JFrame("Outlet de Ropa");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initComponents();
        panel1.setBackground(FONDO);
        aplicarEstilo(panel1);
        eliminarButton.setBackground(ACENTO);
        tituloLbl.setForeground(ACENTO);
        tituloLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        crearMenu();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initComponents() {
        dlmPrenda = new DefaultListModel<>();
        list1.setModel(dlmPrenda);
    }

    private void crearMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        itemImportar = new JMenuItem("Importar");
        itemExportar = new JMenuItem("Exportar");
        itemSalir = new JMenuItem("Salir");
        menuArchivo.add(itemImportar);
        menuArchivo.add(itemExportar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        barra.add(menuArchivo);
        frame.setJMenuBar(barra);
    }

    private void aplicarEstilo(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            c.setFont(FUENTE);

            if (c instanceof JButton) {
                JButton boton = (JButton) c;
                boton.setBackground(PRIMARIO);
                boton.setForeground(BLANCO);
                boton.setFont(FUENTE_BOLD);
                boton.setFocusPainted(false);
                boton.setOpaque(true);
                boton.setBorderPainted(false);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else if (c instanceof JRadioButton || c instanceof JPanel) {
                c.setBackground(FONDO);
            } else if (c instanceof JLabel) {
                c.setForeground(PRIMARIO);
                c.setFont(FUENTE_BOLD);
            } else if (c instanceof JTextField || c instanceof JTextArea) {
                ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARIO),
                        BorderFactory.createEmptyBorder(3, 5, 3, 5)));
            }

            if (c instanceof Container) {
                aplicarEstilo((Container) c);
            }
        }
    }
}