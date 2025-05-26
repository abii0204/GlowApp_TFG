package com.example.glowapp_tfg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.modelos.InformacionModel;
import com.example.glowapp_tfg.modelos.SubseccionModel;

import java.util.ArrayList;

public class InformacionAdapter extends RecyclerView.Adapter<InformacionAdapter.ViewHolder> {

    private ArrayList<InformacionModel> lista;

    public InformacionAdapter(ArrayList<InformacionModel> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_informacion, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InformacionModel info = lista.get(position);

        holder.textoTitulo.setText(info.getTitulo());
        holder.contenedorSubsecciones.removeAllViews(); // Limpiar antes de añadir

        for (SubseccionModel sub : info.getSubsecciones()) {
            View subView = LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_subseccion, holder.contenedorSubsecciones, false);

            TextView textoSubtitulo = subView.findViewById(R.id.textoSubtitulo);
            TextView textoContenido = subView.findViewById(R.id.textoContenido);
            ImageView imagenInformacion = subView.findViewById(R.id.imagenInformacion); // Nuevo

            textoSubtitulo.setText(sub.getSubtitulo());
            textoContenido.setText(sub.getContenido());

            // Mostrar imagen solo una vez por sección (después de la última subsección)
            if (sub == info.getSubsecciones().get(info.getSubsecciones().size() - 1)) {
                imagenInformacion.setVisibility(View.VISIBLE);
                switch (info.getTitulo()) {
                    case "Consejos de estilo de vida":
                        imagenInformacion.setImageResource(R.drawable.consejos);
                        break;
                    case "Mascarillas recomendadas":
                        imagenInformacion.setImageResource(R.drawable.mascarilla);
                        break;
                    case "Errores comunes que debes evitar":
                        imagenInformacion.setImageResource(R.drawable.errores);
                        break;
                    case "Rutina básica de cuidado de la piel":
                        imagenInformacion.setImageResource(R.drawable.rutina);
                        break;
                    case "Ingredientes que deberías buscar":
                        imagenInformacion.setImageResource(R.drawable.ingredientes);
                        break;
                }
            }

            holder.contenedorSubsecciones.addView(subView);
        }

        holder.botonExpandir.setOnClickListener(v -> {
            if (holder.contenedorSubsecciones.getVisibility() == View.GONE) {
                holder.contenedorSubsecciones.setVisibility(View.VISIBLE);
            } else {
                holder.contenedorSubsecciones.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoTitulo;
        LinearLayout contenedorSubsecciones;
        ImageButton botonExpandir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoTitulo = itemView.findViewById(R.id.textoTitulo);
            contenedorSubsecciones = itemView.findViewById(R.id.contenedorSubsecciones);
            botonExpandir = itemView.findViewById(R.id.botonExpandir);
        }
    }
}

