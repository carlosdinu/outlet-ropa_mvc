package com.carlosdinu.outlet.base;

import java.time.LocalDate;

public abstract class Prenda {
    private String codigo;
    private String marca;
    private String nombre;
    private LocalDate fechaEntrada;
    private double precio;
    private int descuento;
    private String color;
    private String descripcion;

    public Prenda() {

    }

    public Prenda(String codigo, String marca, String nombre, LocalDate fechaEntrada,
                  double precio, int descuento, String color, String descripcion) {
        this.codigo = codigo;
        this.marca = marca;
        this.nombre = nombre;
        this.fechaEntrada = fechaEntrada;
        this.precio = precio;
        this.descuento = descuento;
        this.color = color;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
