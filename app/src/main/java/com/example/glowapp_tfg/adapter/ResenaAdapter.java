package com.example.glowapp_tfg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.modelos.ResenaModel;

import java.util.ArrayList;

public class ResenaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ResenaModel> listaResenas;

    public ResenaAdapter(Context context, ArrayList<ResenaModel> listaResenas) {
        this.context = context;
        this.listaResenas = listaResenas;
    }

    @Override
    public int getCount() {
        return listaResenas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaResenas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResenaModel resena = listaResenas.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resena, parent, false);
        }

        TextView tvProducto = convertView.findViewById(R.id.tvProducto);
        TextView tvMarca = convertView.findViewById(R.id.tvMarca);
        TextView tvTipoPiel = convertView.findViewById(R.id.tvTipoPiel);
        TextView tvComentario = convertView.findViewById(R.id.tvComentario);
        TextView tvPuntuacion = convertView.findViewById(R.id.tvPuntuacion);

        tvProducto.setText("Producto: " + resena.getProducto());
        tvMarca.setText("Marca: " + resena.getMarca());
        tvTipoPiel.setText("Tipo de piel: " + resena.getTipoPiel());
        tvComentario.setText("Comentario: " + resena.getComentario());
        tvPuntuacion.setText("Puntuación: " + resena.getPuntuacion() + " ★");

        return convertView;
    }
}
