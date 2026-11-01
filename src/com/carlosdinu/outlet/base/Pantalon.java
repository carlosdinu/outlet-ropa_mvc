package com.carlosdinu.outlet.base;

import java.time.LocalDate;

public class Pantalon extends Prenda {
    private int tallaCintura;

    public Pantalon() {
        super();
    }

    public Pantalon(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                    double precio, int descuento, String color, String descripcion,
                    int tallaCintura) {
        super(codigo, marca, nombre, fechaEntrada, precio, descuento, color, descripcion);
        this.tallaCintura = tallaCintura;
    }

    public int getTallaCintura() {
        return tallaCintura;
    }

    public void setTallaCintura(int tallaCintura) {
        this.tallaCintura = tallaCintura;
    }

    @Override
    public String toString() {
        return "Pantalon: " + getCodigo() + " " + getMarca() + " " + getNombre();
    }
}