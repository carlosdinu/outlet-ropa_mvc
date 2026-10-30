package com.carlosdinu.outlet.base;

import java.time.LocalDate;

public class Zapatilla extends Prenda {
    private double numero;

    public Zapatilla() {
        super();
    }

    public Zapatilla(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                    double precio, int descuento, String color, String descripcion,
                    String talla) {
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
        return "Moto:" + getCodigo()+" "+getMarca()+" "+ getNombre() ;
    }
}
