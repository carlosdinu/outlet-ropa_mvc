package com.carlosdinu.vehiculosmvc;

import com.carlosdinu.vehiculosmvc.gui.VehiculosControlador;
import com.carlosdinu.vehiculosmvc.gui.VehiculosModelo;
import com.carlosdinu.vehiculosmvc.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        Ventana vista = new Ventana();
        VehiculosModelo modelo = new VehiculosModelo();
        VehiculosControlador controlador = new VehiculosControlador(vista,modelo);
    }
}
