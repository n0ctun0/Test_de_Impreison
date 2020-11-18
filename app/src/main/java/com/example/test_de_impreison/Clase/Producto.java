package com.example.test_de_impreison.Clase;

import java.io.Serializable;

public class Producto implements Serializable {

    private Integer codigoProducto;
    private String DescArticulo_1 = null;
    private String DescArticulo_2 = null;
    private String CodProd = null;
    private String CodBarras = null;
    private String Precio = null;
    private String Stock = null;
    private String IP = null;
    private String Producto = null;
    private String Suc = null;
    private String Mensaje = null;
    private String Estado = null;
    private String Off_available = null;
    private String txt_oferta = null;

    public String getOff_available() {
        return Off_available;
    }

    public void setOff_available(String off_available) {
        Off_available = off_available;
    }

    public String getTxt_oferta() {
        return txt_oferta;
    }

    public void setTxt_oferta(String txt_oferta) {
        this.txt_oferta = txt_oferta;
    }

    public Producto() {
    }


    public Producto(String descArticulo1, String descripcion2, String codigoproducto, String codigobarras, String precio, String stock, String ip, String producto, String sucursal, String mensaje, String estado, String Off_available, String txt_oferta) {

        this.DescArticulo_1 = descArticulo1;
        this.DescArticulo_2 = descripcion2;
        this.CodProd = codigoproducto;
        this.CodBarras = codigobarras;
        this.Precio = precio;
        this.Stock = stock;
        this.IP = ip;
        this.Producto = producto;
        this.Suc = sucursal;
        this.Mensaje = mensaje;
        this.Estado = estado;
        this.Off_available = Off_available;
        this.txt_oferta = txt_oferta;

    }

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescArticulo_1() {
        return DescArticulo_1;
    }

    public void setDescArticulo_1(String descArticulo_1) {
        DescArticulo_1 = descArticulo_1;
    }

    public String getDescArticulo_2() {
        return DescArticulo_2;
    }

    public void setDescArticulo_2(String descArticulo_2) {
        DescArticulo_2 = descArticulo_2;
    }

    public String getCodProd() {
        return CodProd;
    }

    public void setCodProd(String codProd) {
        CodProd = codProd;
    }

    public String getCodBarras() {
        return CodBarras;
    }

    public void setCodBarras(String codBarras) {
        CodBarras = codBarras;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public String getSuc() {
        return Suc;
    }

    public void setSuc(String suc) {
        Suc = suc;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String toString() {
        return "Producto{" +
                "codigoProducto=" + codigoProducto +
                ", DescArticulo_1='" + DescArticulo_1 + '\'' +
                ", DescArticulo_2='" + DescArticulo_2 + '\'' +
                ", CodProd='" + CodProd + '\'' +
                ", Precio='" + Precio + '\'' +
                ", Off_available='" + Off_available + '\'' +
                ", txt_oferta='" + txt_oferta + '\'' +
                '}';
    }


}
