package com.example.glowapp_tfg.menus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.adapter.InformacionAdapter;
import com.example.glowapp_tfg.dao.InformacionDAO;
import com.example.glowapp_tfg.modelos.InformacionModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class InformacionGeneral extends AppCompatActivity {


    private RecyclerView recyclerInformacion;

    private String tipoPielUsuario;
    private int idUsuario;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_general);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("datosGuardados", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", 0);

        recyclerInformacion = findViewById(R.id.recyclerInformacion);
        recyclerInformacion.setLayoutManager(new LinearLayoutManager(this));

        obtenerTipoPielYMostrarInfo();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigationI);
        bottomNav.setSelectedItemId(R.id.menu_info);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_info) {
                return true;
            } else if (id == R.id.menu_resenas) {
                Intent intent = new Intent(InformacionGeneral.this, Resenas.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_principal) {
                Intent intent = new Intent(InformacionGeneral.this, MenuPrincipal.class);
                startActivity(intent);
                finish();
                return true;
            } else {
                return false;
            }
        });

    }


    private void obtenerTipoPielYMostrarInfo() {
        new Thread(() -> {
            InformacionDAO informacionDAO = new InformacionDAO();
            tipoPielUsuario = informacionDAO.obtenerTipoPielUsuario(idUsuario);
            ArrayList<InformacionModel> secciones = informacionDAO.obtenerInformacionPorCategoria(tipoPielUsuario);

            runOnUiThread(() -> mostrarSecciones(secciones));
        }).start();
    }

    private void mostrarSecciones(ArrayList<InformacionModel> lista) {
        InformacionAdapter adapter = new InformacionAdapter(lista);
        recyclerInformacion.setAdapter(adapter);
    }

}
