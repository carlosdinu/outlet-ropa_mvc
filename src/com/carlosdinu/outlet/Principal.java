/*
        Clase Principal, es la que ejecuta el Programa
 */

package com.carlosdinu.outlet;

import com.carlosdinu.outlet.gui.OutletControlador;
import com.carlosdinu.outlet.gui.OutletModelo;
import com.carlosdinu.outlet.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        Ventana vista = new Ventana();
        OutletModelo modelo = new OutletModelo();
        OutletControlador controlador = new OutletControlador(vista, modelo);
    }
}