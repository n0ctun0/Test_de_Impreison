package com.example.test_de_impreison.configuracion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.test_de_impreison.R;

import java.util.ArrayList;

public class ListaDispositivosBTAdapter extends ArrayAdapter<DispositivoBT> {

    Activity context;
    ArrayList<DispositivoBT> lista;

    public ListaDispositivosBTAdapter(Activity context, int textViewResourceId, ArrayList<DispositivoBT> lista) {
        super(context, textViewResourceId, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults resultado = new FilterResults();
                resultado.values = lista;
                return resultado;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence filtro, FilterResults resultado) {
                lista = (ArrayList<DispositivoBT>) resultado.values;
                ListaDispositivosBTAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = inflater.inflate(R.layout.lista_dispositivos_bt, parent, false);

        TextView nombreBT = (TextView) rowView.findViewById(R.id.lblNombreDispositivo);
        TextView macBT = (TextView) rowView.findViewById(R.id.lblMACDispositivo);
//        TextView tipoBT = (TextView) rowView.findViewById(R.id.lblTipoDispositivo);

        nombreBT.setText(lista.get(position).getNombreDispositivo());
        macBT.setText(lista.get(position).getMacDispositivo());
//        tipoBT.setText(lista.get(position).getTipoDispositivo());

        return rowView;
    }


}
