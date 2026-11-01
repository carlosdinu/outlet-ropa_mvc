package com.carlosdinu.outlet.gui;

import com.carlosdinu.outlet.base.Prenda;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;

public class Ventana {
    public JFrame frame;

    public JRadioButton camisetaRadioButton, pantalonRadioButton, zapatillaRadioButton;
    public JTextField codigoTxt, marcaTxt, nombreTxt, precioTxt, descuentoTxt, campoVariableTxt;
    public JLabel campoVariableLbl;
    public JComboBox<String> colorCombo;
    public DatePicker fechaEntradaDPicker;
    public JTextArea descripcionTxtArea;
    public JButton nuevoButton, modificarButton, eliminarButton;
    public JList<Prenda> list1;
    public DefaultListModel<Prenda> dlmPrenda;
    public JMenuItem itemImportar, itemExportar, itemSalir;

    private static final Color FONDO       = new Color(245, 240, 235);
    private static final Color PRIMARIO    = new Color(40, 40, 40);
    private static final Color ACENTO      = new Color(200, 70, 60);
    private static final Color BLANCO      = Color.WHITE;
    private static final Font  FUENTE      = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font  FUENTE_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public Ventana() {
        frame = new JFrame("Outlet de Ropa");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(crearPanelRadios(), BorderLayout.NORTH);
        panel.add(crearPanelDatos(), BorderLayout.WEST);
        panel.add(crearPanelLista(), BorderLayout.CENTER);
        panel.add(crearPanelBotones(), BorderLayout.SOUTH);

        frame.setContentPane(panel);
        aplicarEstilo(panel);
        crearMenu();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel crearPanelRadios() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        camisetaRadioButton = new JRadioButton("Camiseta", true);
        pantalonRadioButton = new JRadioButton("Pantalon");
        zapatillaRadioButton = new JRadioButton("Zapatilla");

        ButtonGroup grupo = new ButtonGroup();   // solo uno marcado a la vez
        grupo.add(camisetaRadioButton);
        grupo.add(pantalonRadioButton);
        grupo.add(zapatillaRadioButton);

        p.add(new JLabel("Tipo de prenda:"));
        p.add(camisetaRadioButton);
        p.add(pantalonRadioButton);
        p.add(zapatillaRadioButton);
        return p;
    }

    private JPanel crearPanelDatos() {
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5)); // 0 filas = las que hagan falta, 2 columnas
        codigoTxt = new JTextField(15);
        marcaTxt = new JTextField();
        nombreTxt = new JTextField();
        precioTxt = new JTextField();
        descuentoTxt = new JTextField();
        colorCombo = new JComboBox<>(new String[]{"Negro", "Blanco", "Azul", "Rojo", "Verde", "Gris"});
        fechaEntradaDPicker = new DatePicker();
        campoVariableLbl = new JLabel("Talla");
        campoVariableTxt = new JTextField();

        p.add(new JLabel("Código"));        p.add(codigoTxt);
        p.add(new JLabel("Marca"));         p.add(marcaTxt);
        p.add(new JLabel("Nombre"));        p.add(nombreTxt);
        p.add(new JLabel("Precio (€)"));    p.add(precioTxt);
        p.add(new JLabel("Descuento (%)")); p.add(descuentoTxt);
        p.add(new JLabel("Color"));         p.add(colorCombo);
        p.add(new JLabel("Fecha entrada")); p.add(fechaEntradaDPicker);
        p.add(campoVariableLbl);            p.add(campoVariableTxt);

        JPanel contenedor = new JPanel(new BorderLayout(5, 5));
        descripcionTxtArea = new JTextArea(4, 20);
        descripcionTxtArea.setLineWrap(true);
        contenedor.add(p, BorderLayout.NORTH);
        contenedor.add(new JLabel("Descripción"), BorderLayout.CENTER);
        contenedor.add(new JScrollPane(descripcionTxtArea), BorderLayout.SOUTH);
        return contenedor;
    }

    private JScrollPane crearPanelLista() {
        dlmPrenda = new DefaultListModel<>();
        list1 = new JList<>(dlmPrenda);
        JScrollPane scroll = new JScrollPane(list1);
        scroll.setPreferredSize(new Dimension(350, 300));
        return scroll;
    }

    private JPanel crearPanelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        nuevoButton = new JButton("Nuevo");
        modificarButton = new JButton("Modificar");
        eliminarButton = new JButton("Eliminar");
        p.add(nuevoButton);
        p.add(modificarButton);
        p.add(eliminarButton);
        return p;
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
}