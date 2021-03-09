package com.example.test_de_impreison.Clase;

import java.io.Serializable;

public class Producto2 implements Serializable {

    public Producto2(Integer codigo, String nombre, String moneda, String precio, String codBarras, String prioridad, String aux) {
        Codigo = codigo;
        Nombre = nombre;
        Moneda = moneda;
        Precio = precio;
        CodBarras = codBarras;
        Prioridad = prioridad;
        Aux = aux;
    }

    private Integer Codigo;

    public Integer getCodigo() {
        return Codigo;
    }

    public void setCodigo(Integer codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String moneda) {
        Moneda = moneda;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getCodBarras() {
        return CodBarras;
    }

    public void setCodBarras(String codBarras) {
        CodBarras = codBarras;
    }

    public String getPrioridad() {
        return Prioridad;
    }

    public void setPrioridad(String prioridad) {
        Prioridad = prioridad;
    }

    public String getAux() {
        return Aux;
    }

    public void setAux(String aux) {
        Aux = aux;
    }

    private String Nombre = null;
    private String Moneda = null;
    private String Precio = null;
    private String CodBarras = null;
    private String Prioridad = null;
    private String Aux = null;






}
