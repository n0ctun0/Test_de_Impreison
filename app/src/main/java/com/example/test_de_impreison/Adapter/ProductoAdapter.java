package com.example.test_de_impreison.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_de_impreison.Clase.Producto;
import com.example.test_de_impreison.R;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.NoteViewHolder> {

    private List<Producto> notes;
    private OnNoteSelectedListener onNoteSelectedListener;
    private OnNoteDetailListener onDetailListener;
    private OnPersonCheckedListener onPersonCheckedListener;

    private Context contex;

    public ProductoAdapter() {
        this.notes = new ArrayList<>();
    }

    public ProductoAdapter(List<Producto> notes) {
        this.notes = notes;
    }


    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View elementoTitular = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note_producto, parent, false);

        contex = elementoTitular.getContext();

        return new NoteViewHolder(elementoTitular);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder view, int pos) {
        view.bind(notes.get(pos));
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }


    public List<Producto> getNotes() {
        return notes;
    }

    public void setNotes(List<Producto> notes) {
        this.notes = notes;
    }

    public void setOnNoteSelectedListener(OnNoteSelectedListener onNoteSelectedListener) {
        this.onNoteSelectedListener = onNoteSelectedListener;
    }

    public void setOnDetailListener(OnNoteDetailListener onDetailListener) {
        this.onDetailListener = onDetailListener;
    }

    public void setOnPersonCheckedListener(OnPersonCheckedListener onPersonCheckedListener) {
        this.onPersonCheckedListener = onPersonCheckedListener;
    }



    public interface OnNoteSelectedListener {
        void onClick(Producto note);
    }

    public interface OnNoteDetailListener {
        void onDetail(Producto note);
    }

    public interface OnPersonCheckedListener {
        void onPersonChecked(Producto note);
    }

    public Producto getNoteAt(int position) {
        return notes.get(position);
    }




    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView codigoProducto;
        private TextView DescArticulo_1;
        private TextView DescArticulo_2;
        private TextView CodProd;
        private TextView CodBarras;
        private TextView Precio;
        private TextView Stock;
        private TextView IP;
        private TextView Producto;
        private TextView Suc;
        private TextView Mensaje;
        private TextView Estado;
        private TextView Oferta;
        private ImageView Checkimpreso;

        private LinearLayout layoutetiquet;

        public NoteViewHolder(View item) {
            super(item);

            codigoProducto = (TextView) item.findViewById(R.id.txt_codigoProducto);
            DescArticulo_1 = (TextView) item.findViewById(R.id.txt_DescArticulo_1);
            DescArticulo_2 = (TextView) item.findViewById(R.id.txt_DescArticulo_2);
            CodProd = (TextView) item.findViewById(R.id.txt_CodProd);
            CodBarras = (TextView) item.findViewById(R.id.txt_CodBarras);
            Precio = (TextView) item.findViewById(R.id.txt_Precio);
            Stock = (TextView) item.findViewById(R.id.txt_Stock);
            IP = (TextView) item.findViewById(R.id.txt_IP);
            Producto = (TextView) item.findViewById(R.id.txt_Producto);
            Suc = (TextView) item.findViewById(R.id.txt_Suc);
            Mensaje = (TextView) item.findViewById(R.id.txt_Mensaje);
            Estado = (TextView) item.findViewById(R.id.txt_Estado);
            Oferta = (TextView) item.findViewById(R.id.txt_oferta);
            layoutetiquet = item.findViewById(R.id.layoutetiqueta);
            Checkimpreso = item.findViewById(R.id.img_view);
        }


        public void bind(final Producto producto) {


            codigoProducto.setText(producto.getCodigoProducto().toString());
            DescArticulo_1.setText(producto.getDescArticulo_1().toString());
            DescArticulo_2.setText(producto.getDescArticulo_2().toString());
            CodProd.setText(producto.getCodProd().toString());
            CodBarras.setText(producto.getCodBarras().toString());
            Precio.setText(producto.getPrecio().toString());
            Stock.setText(producto.getStock().toString());
            IP.setText(producto.getIP().toString());
            Producto.setText(producto.getProducto().toString());
            Suc.setText(producto.getSuc().toString());
            Mensaje.setText(producto.getMensaje().toString());
            Estado.setText(producto.getEstado().toString());


                if (producto.getIP().toString().equals("NO")){
                    Checkimpreso.setImageResource(R.drawable.ic_printer);
                    layoutetiquet.setBackground(ContextCompat.getDrawable(contex, R.drawable.ic_tag_60x30));

                }else{
                    Checkimpreso.setImageResource(R.drawable.ic_replay_black_24dp);
                    layoutetiquet.setBackground(ContextCompat.getDrawable(contex, R.drawable.ic_tag_60x30_impreso));
                }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteSelectedListener != null) {
                        onNoteSelectedListener.onClick(producto);
                    }
                }
            });

        }

    }
}
