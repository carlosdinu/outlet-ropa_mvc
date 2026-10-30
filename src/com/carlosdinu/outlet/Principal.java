package com.carlosdinu.outlet;

import com.carlosdinu.outlet.gui.VehiculosControlador;
import com.carlosdinu.outlet.gui.VehiculosModelo;
import com.carlosdinu.outlet.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        Ventana vista = new Ventana();
        VehiculosModelo modelo = new VehiculosModelo();
        VehiculosControlador controlador = new VehiculosControlador(vista,modelo);
    }
}
