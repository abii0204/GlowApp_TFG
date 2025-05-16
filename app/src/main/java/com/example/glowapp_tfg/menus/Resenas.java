package com.example.glowapp_tfg.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.adapter.ResenaAdapter;
import com.example.glowapp_tfg.dao.ResenaDAO;
import com.example.glowapp_tfg.modelos.ResenaModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Resenas extends AppCompatActivity {

    ImageButton ibInsertarResena;
    ListView listViewResenas;
    private int idUsuario;
    ResenaDAO resenaDAO;

    private ArrayList<ResenaModel> listaResenas;
    private ResenaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resenas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getIntExtra("usuario_id", -1);
        id();
        resenaDAO = new ResenaDAO();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigationR);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_resenas) {
                return true;
            } else if (id == R.id.menu_principal) {
                startActivity(new Intent(Resenas.this, MenuPrincipal.class));
                finish();
                return true;
            } else if (id == R.id.menu_info) {
                startActivity(new Intent(Resenas.this, InformacionGeneral.class));
                finish();
                return true;
            } else {
                return false;
            }
        });

        // Inicializar y cargar todas las reseñas
        listaResenas = resenaDAO.obtenerTodasLasResenas();
        adapter = new ResenaAdapter(this, listaResenas);
        listViewResenas.setAdapter(adapter);

        ibInsertarResena.setOnClickListener(v -> mostrarPopupResena());
    }

    public void id() {
        ibInsertarResena = findViewById(R.id.ibInsertarResena);
        listViewResenas = findViewById(R.id.listViewResenas);
    }

    public void mostrarPopupResena() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_resena, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        AlertDialog dialog = builder.create();
        dialog.show();

        popupView.setTag(dialog);
    }

    public void guardarResena(View view) {
        View popupView = (View) view.getParent().getParent(); // sube hasta la vista raíz
        AlertDialog dialog = (AlertDialog) popupView.getTag();

        EditText etProducto = popupView.findViewById(R.id.etProducto);
        EditText etMarca = popupView.findViewById(R.id.etMarca);
        EditText etTipoPiel = popupView.findViewById(R.id.etTipoPiel);
        EditText etComentario = popupView.findViewById(R.id.etComentario);
        RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);

        String producto = etProducto.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String tipoPiel = etTipoPiel.getText().toString().trim();
        String comentario = etComentario.getText().toString().trim();
        int puntuacion = (int) ratingBar.getRating();

        if (producto.isEmpty() || marca.isEmpty()) {
            Toast.makeText(this, "Producto y marca son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        ResenaModel nuevaResena = new ResenaModel(idUsuario, producto, marca, tipoPiel, comentario, puntuacion);
        boolean exito = resenaDAO.insertarResena(nuevaResena);

        if (exito) {
            listaResenas.clear();
            listaResenas.addAll(resenaDAO.obtenerTodasLasResenas());
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Reseña guardada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar la reseña", Toast.LENGTH_SHORT).show();
        }

        dialog.dismiss();
    }
}
