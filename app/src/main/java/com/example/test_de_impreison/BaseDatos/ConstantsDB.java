package com.example.test_de_impreison.BaseDatos;

public class ConstantsDB {
    //General
    public static final String DB_NAME = "sanroque12.db";
    public static final int DB_VERSION = 12;

    //TABLA PRODUCTOS

    public static final String TABLA_PRODUCTO = "Producto";
    public static final String PRO_CODIGOPRODUCTO = "_codigoproducto";
    public static final String PRO_DESCRIPCION1 = "DescArticulo_1";
    public static final String PRO_DESCRIPCION2 = "DescArticulo_2";
    public static final String PRO_CODPROD = "CodProd";
    public static final String PRO_CODBARRAS= "CodBarras";
    public static final String PRO_PRECIO = "Precio";
    public static final String PRO_STOCK = "Stock";
    public static final String PRO_IP = "IP";
    public static final String PRO_PRODUCTO = "Producto";
    public static final String PRO_SUC = "Suc";
    public static final String PRO_MENSAJE= "Mensaje";
    public static final String PRO_ESTADO = "Estado";
    public static final String PRO_OFFAVAILABLE = "Off_available";
    public static final String PRO_TXTOFERTA  = "txt_oferta";




    public static final String TABLA_LIST_PRODUCTO = "ListProducto";
    public static final String LIST_CODIGOPRODUCTO = "_codigoproducto";
    public static final String LIST_DESCRIPCION1 = "DescArticulo_1";
    public static final String LIST_DESCRIPCION2 = "DescArticulo_2";
    public static final String LIST_CODPROD = "CodProd";
    public static final String LIST_CODBARRAS= "CodBarras";
    public static final String LIST_PRECIO = "Precio";
    public static final String LIST_STOCK = "Stock";
    public static final String LIST_OFFAVAILABLE = "off_available";
    public static final String LIST_IP = "IP";
    public static final String LIST_SUC = "Suc";
    public static final String LIST_ESTADO = "Estado";
    public static final String LIST_TXTOFERTA  = "txt_oferta";

    public static final String TABLA_LOGIN = "Login";
    public static final String LOGIN_CODIGOLOGIN = "_codigologin";
    public static final String LOGIN_LOGIN= "Login";
    public static final String LOGIN_PASSWORD= "Password";
    public static final String LOGIN_PASSWORDROOT= "Passwordroot";


    public static final String TABLA_RUBRO = "Rubro";
    public static final String RUBRO_CODIGO = "_codigoRubro";
    public static final String RUBRO_NUMERO= "Numero";
    public static final String RUBRO_DESCRIPCION= "Descripcion";



    public static final String TABLA_PRODUCTO_SQL =
            "CREATE TABLE  " + TABLA_PRODUCTO + "(" +
                    PRO_CODIGOPRODUCTO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PRO_DESCRIPCION1   + " TEXT," +
                    PRO_DESCRIPCION2 + " TEXT," +
                    PRO_CODPROD + " TEXT," +
                    PRO_CODBARRAS + " TEXT," +
                    PRO_PRECIO + " TEXT," +
                    PRO_STOCK + " TEXT," +
                    PRO_IP + " TEXT," +
                    PRO_PRODUCTO + " TEXT," +
                    PRO_SUC + " TEXT," +
                    PRO_MENSAJE + " TEXT," +
                    PRO_ESTADO + " TEXT," +
                    PRO_OFFAVAILABLE + " TEXT," +
                    PRO_TXTOFERTA   + " TEXT);" ;

    public static final String TABLA_LIST_PRODUCTO_SQL =
            "CREATE TABLE  " + TABLA_LIST_PRODUCTO + "(" +
                    LIST_CODIGOPRODUCTO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LIST_DESCRIPCION1   + " TEXT," +
                    LIST_DESCRIPCION2 + " TEXT," +
                    LIST_CODPROD + " TEXT," +
                    LIST_CODBARRAS + " TEXT," +
                    LIST_PRECIO + " TEXT," +
                    LIST_STOCK + " TEXT," +
                    LIST_OFFAVAILABLE + " TEXT," +
                    LIST_IP + " TEXT," +
                    LIST_SUC + " TEXT," +
                    LIST_ESTADO + " TEXT," +
                    LIST_TXTOFERTA   + " TEXT);" ;




    public static final String TABLA_LOGIN_SQL =
            "CREATE TABLE  " + TABLA_LOGIN + "(" +
                    LOGIN_CODIGOLOGIN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LOGIN_LOGIN   + " TEXT," +
                    LOGIN_PASSWORD + " TEXT," +
                    LOGIN_PASSWORDROOT  + " TEXT);" ;


    public static final String TABLA_RUBRO_SQL =
            "CREATE TABLE  " + TABLA_RUBRO+ "(" +
                    RUBRO_CODIGO + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RUBRO_NUMERO   + " TEXT," +
                    RUBRO_DESCRIPCION  + " TEXT);" ;


}
