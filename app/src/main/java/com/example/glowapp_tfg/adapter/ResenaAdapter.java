package com.example.glowapp_tfg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.modelos.ResenaModel;

import java.util.ArrayList;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ViewHolder> {

    private ArrayList<ResenaModel> listaResenas;

    public ResenaAdapter(ArrayList<ResenaModel> listaResenas) {
        this.listaResenas = listaResenas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResenaModel resena = listaResenas.get(position);
        holder.tvNombreUsuario.setText(resena.getNombreUsuario());
        holder.tvFecha.setText(resena.getFecha());
        holder.tvProducto.setText("Producto: " + resena.getProducto());
        holder.tvMarca.setText("Marca: " + resena.getMarca());
        holder.tvTipoPiel.setText("Tipo de piel: " + resena.getTipoPiel());
        holder.tvComentario.setText("Comentario: " + resena.getComentario());
        holder.tvPuntuacion.setText("Puntuación: " + resena.getPuntuacion() + " ★");
    }

    @Override
    public int getItemCount() {
        return listaResenas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUsuario, tvFecha, tvProducto, tvMarca, tvTipoPiel, tvComentario, tvPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuarioResena);
            tvFecha = itemView.findViewById(R.id.tvFechaResena);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvTipoPiel = itemView.findViewById(R.id.tvTipoPiel);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            tvPuntuacion = itemView.findViewById(R.id.tvPuntuacion);
        }
    }
}
