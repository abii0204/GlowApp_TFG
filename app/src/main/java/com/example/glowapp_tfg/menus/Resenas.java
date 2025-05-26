package com.example.glowapp_tfg.menus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.adapter.ResenaAdapter;
import com.example.glowapp_tfg.dao.ResenaDAO;
import com.example.glowapp_tfg.modelos.ResenaModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Resenas extends AppCompatActivity {

    ImageButton ibInsertarResena, botonmasFiltros;
    RecyclerView recyclerViewResenas;
    EditText etFiltroTipoPiel, etFiltroProducto, etFiltroMarca;
    Button btnBuscarTipoPiel, btnBuscarProducto, btnBuscarMarca, btnBorrarFiltros;

    LinearLayout filtros;

    private AlertDialog dialogResena;

    private int idUsuario;
    ResenaDAO resenaDAO;

    private ArrayList<ResenaModel> listaResenas;
    private ResenaAdapter adapter;

    SharedPreferences sharedPreferences;

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

        sharedPreferences = getSharedPreferences("datosGuardados", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", 0);

        id();
        resenaDAO = new ResenaDAO();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigationR);
        bottomNav.setSelectedItemId(R.id.menu_resenas);

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

        botonmasFiltros.setOnClickListener(v -> {
            if (filtros.getVisibility() == View.GONE) {
                filtros.setVisibility(View.VISIBLE);
            } else {
                filtros.setVisibility(View.GONE);
            }
        });



        btnBuscarTipoPiel.setOnClickListener(v -> {
            String tipo = etFiltroTipoPiel.getText().toString().trim();
            if (!tipo.isEmpty()) {
                listaResenas.clear();
                listaResenas.addAll(resenaDAO.buscarPorTipoPiel(tipo));
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Introduce un tipo de piel", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuscarProducto.setOnClickListener(v -> {
            String producto = etFiltroProducto.getText().toString().trim();
            if (!producto.isEmpty()) {
                listaResenas.clear();
                listaResenas.addAll(resenaDAO.buscarPorProducto(producto));
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Introduce un producto", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuscarMarca.setOnClickListener(v -> {
            String marca = etFiltroMarca.getText().toString().trim();
            if (!marca.isEmpty()) {
                listaResenas.clear();
                listaResenas.addAll(resenaDAO.buscarPorMarca(marca));
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Introduce una marca", Toast.LENGTH_SHORT).show();
            }
        });

        btnBorrarFiltros.setOnClickListener(v -> {
            etFiltroTipoPiel.setText("");
            etFiltroProducto.setText("");
            etFiltroMarca.setText("");

            listaResenas.clear();
            listaResenas.addAll(resenaDAO.obtenerTodasLasResenas());
            adapter.notifyDataSetChanged();
        });




        listaResenas = resenaDAO.obtenerTodasLasResenas();
        adapter = new ResenaAdapter(listaResenas);
        recyclerViewResenas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResenas.setHasFixedSize(false);
        recyclerViewResenas.setNestedScrollingEnabled(false);
        recyclerViewResenas.setAdapter(adapter);


        ibInsertarResena.setOnClickListener(v -> mostrarPopupResena());
    }

    public void id() {
        ibInsertarResena = findViewById(R.id.ibInsertarResena);
        recyclerViewResenas = findViewById(R.id.recyclerViewResenas);

        etFiltroTipoPiel = findViewById(R.id.etFiltroTipoPiel);
        etFiltroProducto = findViewById(R.id.etFiltroProducto);
        etFiltroMarca = findViewById(R.id.etFiltroMarca);

        btnBuscarTipoPiel = findViewById(R.id.btnBuscarTipoPiel);
        btnBuscarProducto = findViewById(R.id.btnBuscarProducto);
        btnBuscarMarca = findViewById(R.id.btnBuscarMarca);
        btnBorrarFiltros = findViewById(R.id.btnBorrarFiltros);

        botonmasFiltros = findViewById(R.id.botonmas);
        filtros = findViewById(R.id.filtrosLayout);


    }



    public void mostrarPopupResena() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_resena, null);

        Spinner spinnerTipoPiel = popupView.findViewById(R.id.spTipoPielPopUp);
        String[] tiposPiel = {"Elige tu tipo de piel", "Normal", "Grasa", "Seca", "Mixta", "Sensible"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposPiel);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPiel.setAdapter(adapterSpinner);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        // Guardas la instancia del AlertDialog en la variable miembro
        dialogResena = builder.create();
        dialogResena.show();
    }

    public void guardarResena(View view) {
        // Aquí asumes que alertDialog NO es nulo y está visible
        if (dialogResena == null) {
            Toast.makeText(this, "Error: diálogo no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        View popupView = dialogResena.getWindow().getDecorView();

        EditText etProducto = popupView.findViewById(R.id.etProducto);
        EditText etMarca = popupView.findViewById(R.id.etMarca);
        EditText etComentario = popupView.findViewById(R.id.etComentario);
        RatingBar ratingBar = popupView.findViewById(R.id.ratingBar);
        Spinner spinnerTipoPiel = popupView.findViewById(R.id.spTipoPielPopUp);

        String producto = etProducto.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String tipoPiel = spinnerTipoPiel.getSelectedItem().toString();
        String comentario = etComentario.getText().toString().trim();
        int puntuacion = (int) ratingBar.getRating();

        if (producto.isEmpty() || marca.isEmpty()) {
            Toast.makeText(this, "Producto y marca son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tipoPiel.equals("Elige tu tipo de piel")) {
            Toast.makeText(this, "Selecciona un tipo de piel válido", Toast.LENGTH_SHORT).show();
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

        dialogResena.dismiss();
        dialogResena = null;
    }
}
