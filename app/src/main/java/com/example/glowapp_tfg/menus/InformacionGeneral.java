package com.example.glowapp_tfg.menus;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InformacionGeneral extends AppCompatActivity {

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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigationI);
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
}