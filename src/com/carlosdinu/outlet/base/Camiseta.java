package com.carlosdinu.outlet.base;

import java.time.LocalDate;

public class Camiseta extends Prenda {
    private String talla;

    public Camiseta() {
        super();
    }

    public Camiseta(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                    double precio, int descuento, String color, String descripcion,
                    String talla) {
        super(codigo, marca, nombre, fechaEntrada, precio, descuento, color, descripcion);
        this.talla = talla;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    @Override
    public String toString() {
        return "Coche:" + getCodigo()+" "+getMarca()+" "+ getNombre() ;
    }
}
