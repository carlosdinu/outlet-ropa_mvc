/*
        Clase heredada de Prenda, tiene dos constructores, getters y setters y un toString
 */

package com.carlosdinu.outlet.base;

import java.time.LocalDate;

public class Zapatilla extends Prenda {
    private double numero;

    public Zapatilla() {
        super();
    }

    public Zapatilla(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                    double precio, int descuento, String color, String descripcion,
                    double numero) {
        super(codigo, marca, nombre, fechaEntrada, precio, descuento, color, descripcion);
        this.numero = numero;
    }

    public double getNumero() {
        return numero;
    }

    public void setNumero(double numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return String.format(" ZAPATILLA  |  %s  |  %s %s  |  Nº %.1f  |  %.2f € (-%d%%)",
                getCodigo(), getMarca(), getNombre(), numero, getPrecio(), getDescuento());
    }
}
