package com.example.test_de_impreison.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.test_de_impreison.Clase.Producto;

import java.util.ArrayList;


public class ProductosDB {

        private SQLiteDatabase db;
        private ProductosDB.DBHelper dbHelper;

        public ProductosDB(Context context) {
            dbHelper = new ProductosDB.DBHelper(context);
        }


        private void openReadableDB() {
            db = dbHelper.getReadableDatabase();
        }

        private void openWriteableDB() {
            db = dbHelper.getWritableDatabase();
        }

        private void closeDB() {
            if (db != null) {
                db.close();
            }
        }


        private ContentValues clienteMapperContentValues(Producto producto) {
            ContentValues cv = new ContentValues();
            cv.put(ConstantsDB.PRO_CODIGOPRODUCTO, producto.getCodigoProducto());
            cv.put(ConstantsDB.PRO_DESCRIPCION1, producto.getDescArticulo_1());
            cv.put(ConstantsDB.PRO_DESCRIPCION2, producto.getDescArticulo_2());
            cv.put(ConstantsDB.PRO_CODPROD, producto.getCodProd());
            cv.put(ConstantsDB.PRO_CODBARRAS, producto.getCodBarras());
            cv.put(ConstantsDB.PRO_PRECIO, producto.getPrecio());
            cv.put(ConstantsDB.PRO_STOCK, producto.getStock());
            cv.put(ConstantsDB.PRO_IP, producto.getIP());
            cv.put(ConstantsDB.PRO_PRODUCTO, producto.getProducto());
            cv.put(ConstantsDB.PRO_SUC, producto.getSuc());
            cv.put(ConstantsDB.PRO_MENSAJE, producto.getMensaje());
            cv.put(ConstantsDB.PRO_ESTADO, producto.getEstado());
            cv.put(ConstantsDB.PRO_OFFAVAILABLE, producto.getOff_available());
            cv.put(ConstantsDB.PRO_TXTOFERTA, producto.getTxt_oferta());
            return cv;
        }

    public void eliminarProductos() {
        this.openWriteableDB();
        db.delete(ConstantsDB.TABLA_PRODUCTO, null, null);
        this.closeDB();
    }


    public void updatecodigoproduto(Producto producto) {

        this.openWriteableDB();
        String where = ConstantsDB.PRO_CODIGOPRODUCTO + "= ?";
        db.update(ConstantsDB.TABLA_PRODUCTO, clienteMapperContentValues(producto), where, new String[]{String.valueOf(producto.getCodigoProducto())});
        db.close();
    }


    public Boolean validar(Producto producto) {

        boolean error= false;
        this.openReadableDB();
        String where = ConstantsDB.PRO_CODIGOPRODUCTO+ "= ?";
        String whered = ConstantsDB.PRO_CODIGOPRODUCTO;
        //String[] whereArgs = {email};
        Cursor c = db.query(ConstantsDB.TABLA_PRODUCTO, null, where, new String[]{producto.getCodProd()}, null, null, null, null);
        try{
            if( c.getCount()>0) {
                c.close();
                error = true;
            }

        }catch (Exception ex){
            Log.e("valiando" + "", "error al leer");
            error = true;
        }

        return error;
    }


    public void eliminarProducto(int codigoProducto) {
        this.openWriteableDB();
        String where = ConstantsDB.PRO_CODIGOPRODUCTO + "= ?";
        db.delete(ConstantsDB.TABLA_PRODUCTO, where, new String[]{String.valueOf(codigoProducto)});
        this.closeDB();
    }

    public long insertarProducto(Producto producto) {
        this.openWriteableDB();
        long rowID = db.insert(ConstantsDB.TABLA_PRODUCTO, null, clienteMapperContentValues(producto));
        this.closeDB();
        return rowID;
    }

        private static class DBHelper extends SQLiteOpenHelper {

            public DBHelper(Context context) {
                super(context, ConstantsDB.DB_NAME, null, ConstantsDB.DB_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {

                db.execSQL(ConstantsDB.TABLA_PRODUCTO_SQL);
                db.execSQL(ConstantsDB.TABLA_LIST_PRODUCTO_SQL);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        }

    public ArrayList loadProducto() {

        ArrayList<Producto> list = new ArrayList<>();
        String where = ConstantsDB.PRO_CODIGOPRODUCTO;
        this.openReadableDB();
        String[] campos = new String[]{ConstantsDB.PRO_CODIGOPRODUCTO,ConstantsDB.PRO_DESCRIPCION1,ConstantsDB.PRO_DESCRIPCION2, ConstantsDB.PRO_CODPROD, ConstantsDB.PRO_CODBARRAS,  ConstantsDB.PRO_PRECIO, ConstantsDB.PRO_STOCK, ConstantsDB.PRO_IP, ConstantsDB.PRO_PRODUCTO, ConstantsDB.PRO_SUC, ConstantsDB.PRO_MENSAJE,ConstantsDB.PRO_ESTADO,ConstantsDB.PRO_OFFAVAILABLE,ConstantsDB.PRO_TXTOFERTA };
        Cursor c = db.query(ConstantsDB.TABLA_PRODUCTO, campos, null, null, null, null, where +" DESC");

        try {
            while (c.moveToNext()) {
                Producto producto = new Producto();
                producto.setCodigoProducto(c.getInt(0));
                producto.setDescArticulo_1(c.getString(1));
                producto.setDescArticulo_2(c.getString(2));
                producto.setCodProd(c.getString(3));
                producto.setCodBarras(c.getString(4));
                producto.setPrecio(c.getString(5));
                producto.setStock(c.getString(6));
                producto.setIP(c.getString(7));
                producto.setProducto(c.getString(8));
                producto.setSuc(c.getString(9));
                producto.setMensaje(c.getString(10));
                producto.setEstado(c.getString(11));
                producto.setOff_available(c.getString(12));
                producto.setTxt_oferta(c.getString(13));
                list.add(producto);

            }
        } finally {
            c.close();
        }
        this.closeDB();
        return list;
    }

    }


